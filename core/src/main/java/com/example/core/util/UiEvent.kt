package com.example.core.util


// used to pop up the backstack or navigation
sealed class UiEvent {
    object Success : UiEvent()
    object NavigateUp: UiEvent()
    data class ShowSnackbar(val message: UiText): UiEvent()
}