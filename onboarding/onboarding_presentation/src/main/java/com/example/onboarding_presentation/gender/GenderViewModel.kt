package com.example.onboarding_presentation.gender

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.model.Gender
import com.example.core.domain.preferences.Preferences
import com.example.core.navigation.Route
import com.example.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {

    private var _selectedGender = MutableStateFlow<Gender>(Gender.Male)
    var selectedGender = _selectedGender.asStateFlow()

    // collect in composables like this
    // val example = selectedGender.collectAsState()

    // for one time events, better than sharedFlow because awaits for subscribers, more info in my flow notion page
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    //https://stackoverflow.com/questions/66669612/mutablestateflow-difference-between-value-and-emit
    fun onGenderClick(gender: Gender) {
        _selectedGender.value = gender
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveGender(_selectedGender.value)
            _uiEvent.send(UiEvent.Navigate(Route.AGE))
        }
    }


}