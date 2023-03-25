package com.example.tracker_presentation.search

// the state of the UI shown
data class SearchState(
    val query: String = "",
    val isHintVisible: Boolean = false,
    val isSearching: Boolean = false,
    val trackableFood: List<TrackableFoodUiState> = emptyList() // this list contains the state of every individual trackable food
)