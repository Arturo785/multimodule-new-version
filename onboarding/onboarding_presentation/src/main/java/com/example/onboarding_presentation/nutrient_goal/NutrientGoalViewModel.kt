package com.example.onboarding_presentation.nutrient_goal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.use_case.FilterOutDigitsUseCase
import com.example.core.util.UiEvent
import com.example.onboarding_domain.use_case.ValidateNutrients
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutrientGoalViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDigitsUseCase: FilterOutDigitsUseCase, // both from core module
    private val validateNutrients: ValidateNutrients
) : ViewModel() {

    //  state = the information shown to the user
    //  event = the information triggered by the user
    var state by mutableStateOf(NutrientGoalState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEventTriggered(event: NutrientGoalEvent) {
        when (event) {
            is NutrientGoalEvent.OnCarbRatioEnter -> {
                // validate and set into the state
                state = state.copy(
                    carbsRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnFatRatioEnter -> {
                // validate and set into the state
                state = state.copy(
                    fatRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            is NutrientGoalEvent.OnProteinRatioEnter -> {
                // validate and set into the state
                state = state.copy(
                    proteinRatio = filterOutDigitsUseCase(event.ratio)
                )
            }
            NutrientGoalEvent.OnNextClick -> {
                val result = validateNutrients(
                    carbsRatioText = state.carbsRatio,
                    proteinRatioText = state.proteinRatio,
                    fatRatioText = state.fatRatio
                )

                when (result) {
                    is ValidateNutrients.ResultNutrients.Success -> {
                        preferences.saveCarbRatio(result.carbsRatio)
                        preferences.saveProteinRatio(result.proteinRatio)
                        preferences.saveFatRatio(result.fatRatio)

                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                    is ValidateNutrients.ResultNutrients.Error -> {
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.ShowSnackbar(result.message))
                        }
                    }
                }
            }
        }
    }

}