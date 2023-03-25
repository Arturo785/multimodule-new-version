package com.example.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.use_case.FilterOutDigitsUseCase
import com.example.core.util.UiEvent
import com.example.core.util.UiText
import com.example.tracker_domain.use_case.TrackerUseCases
import com.example.core.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var jobSearch: Job? = Job()


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChange -> {
                state = state.copy(query = event.query)
            }
            is SearchEvent.OnAmountForFoodChange -> {
                // an alternative to this is to use a composableList that reacts so singular changes inside the list
                // and not to total list change
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        // if is the food we want to change in specific
                        if (it.food == event.food) {
                            it.copy(amount = filterOutDigitsUseCase(event.amount))
                        } else it // leave it as it is
                    }
                )
            }
            is SearchEvent.OnSearch -> {
                // jobSearch?.cancel() // cancels previous search to debounce
                // debounce not needed because search happens only when clicked search
                executeSearch()
            }
            is SearchEvent.OnToggleTrackableFood -> {
                // an alternative to this is to use a composableList that reacts so singular changes inside the list
                // and not to total list change
                state = state.copy(
                    trackableFood = state.trackableFood.map {
                        // if is the food we want to toggle in specific
                        if (it.food == event.food) {
                            it.copy(isExpanded = !it.isExpanded)
                        } else it// leave it as it is
                    }
                )
            }
            is SearchEvent.OnSearchFocusChange -> {
                state = state.copy(
                    isHintVisible = !event.isFocused && state.query.isBlank()
                )
            }
            is SearchEvent.OnTrackFoodClick -> {
                trackFood(event)
            }
        }
    }

    private fun executeSearch() {
        // debounce not needed because search happens only when clicked search
        // jobSearch = viewModelScope.launch {
        viewModelScope.launch {
            //delay(5000) // to allow the search to be debounced a little bit and not search for every typed char

            // cleans the current list of the available food to track and displays the spinner loading state
            state = state.copy(
                isSearching = true,
                trackableFood = emptyList()
            )
            // we use our useCase inside the usecases provider
            trackerUseCases
                .searchFood(state.query)
                .onSuccess { foods ->
                    // populate the list of available food to track and and cancels the loading spinner state
                    state = state.copy(
                        isSearching = false,
                        trackableFood = foods.map {
                            TrackableFoodUiState(it)
                        },
                        query = "" // also cleans the query when success
                    )
                }
                .onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(
                        UiEvent.ShowSnackbar(
                            UiText.StringResource(R.string.error_something_went_wrong)
                        )
                    )
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFood.find { it.food == event.food }
            // sends use case to save into the meal the food selected and the amount
            // if food or amount is unavailable the operation does not complete and returns like normal return
            //return@launch is like a normal return but because we are in the launch block we specify from which block we return
            trackerUseCases.trackFood(
                food = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                mealType = event.mealType,
                date = event.date
            )
            _uiEvent.send(UiEvent.NavigateUp) // pops the backstack
        }
    }
}