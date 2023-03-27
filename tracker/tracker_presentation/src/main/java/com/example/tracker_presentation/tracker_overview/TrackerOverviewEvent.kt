package com.example.tracker_presentation.tracker_overview

import com.example.tracker_domain.model.TrackedFood


// all the actions that can be triggered by the user
sealed class TrackerOverviewEvent {
    object OnNextDayClick: TrackerOverviewEvent()
    object OnPreviousDayClick: TrackerOverviewEvent()
    data class OnToggleMealClick(val meal: Meal): TrackerOverviewEvent()
    data class OnDeleteTrackedFoodClick(val trackedFood: TrackedFood): TrackerOverviewEvent()
}