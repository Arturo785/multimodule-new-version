package com.example.tracker_presentation.tracker_overview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.core.util.UiEvent
import com.example.core_ui.LocalSpacing
import com.example.core.R
import com.example.tracker_presentation.tracker_overview.components.AddButton
import com.example.tracker_presentation.tracker_overview.components.DaySelector
import com.example.tracker_presentation.tracker_overview.components.ExpandableMeal
import com.example.tracker_presentation.tracker_overview.components.NutrientsHeader
import com.example.tracker_presentation.tracker_overview.components.TrackedFoodItem


// the same is explained in the age screen
@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit, // this ones are the parameters passed to the search arguments route in the mainActivity
    viewModel: TrackerOverviewViewModel = hiltViewModel() // the same is explained in the age screen
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    // to allow the content to be scrollable
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium) // to not touch the bottom of the screen
    ) {
        item {
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            DaySelector(
                // when modifying the date the composable recomposes and shows the change of date
                // the events are the thing triggered by the user in the UI
                date = viewModel.state.date,
                onPreviousDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick) },
                onNextDayClick = { viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick) }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        // be sure to use the items() items: List<T>,
        items(state.meals) { currentMeal ->
            ExpandableMeal(
                meal = currentMeal,
                onToggleClick = {
                    // we pass our event of our viewModel with our current meal
                    viewModel.onEvent(
                        TrackerOverviewEvent.OnToggleMealClick(
                            currentMeal
                        )
                    )
                },
                content = {
                    // to put the add button below all the list
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
                    ) {
                        // we loop each of our tracked foods
                        state.trackedFoods.forEach { trackedFood ->
                            TrackedFoodItem(
                                trackedFood = trackedFood,
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        TrackerOverviewEvent
                                            .OnDeleteTrackedFoodClick(trackedFood)
                                    )
                                }
                            )
                            // space between every tracked food
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        }
                        // below all the list of tracked foods
                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                currentMeal.name.asString(context)
                            ),
                            onClick = {
                                // we use the fun passed to navigate to the search screen that is in mainActivity
                                // sends the data to be used by the callback passed
                                onNavigateToSearch.invoke(
                                    currentMeal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}