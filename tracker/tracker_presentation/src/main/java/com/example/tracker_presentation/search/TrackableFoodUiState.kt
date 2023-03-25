package com.example.tracker_presentation.search

import com.example.tracker_domain.model.TrackableFood

// the state of every trackable food on the list of the state UI
data class TrackableFoodUiState(
    val food: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)