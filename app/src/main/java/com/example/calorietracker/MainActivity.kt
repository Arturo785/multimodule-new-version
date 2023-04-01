package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calorietracker.ui.theme.CalorieTrackerTheme
import com.example.core.domain.preferences.Preferences
import com.example.core.navigation.Route
import com.example.core_ui.LocalSpacing
import com.example.onboarding_presentation.activity.ActivityScreen
import com.example.onboarding_presentation.age.AgeScreen
import com.example.onboarding_presentation.gender.GenderScreen
import com.example.onboarding_presentation.goal.GoalScreen
import com.example.onboarding_presentation.height.HeightScreen
import com.example.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.example.onboarding_presentation.weight.WeightScreen
import com.example.onboarding_presentation.welcome.WelcomeScreen
import com.example.tracker_presentation.search.SearchScreen
import com.example.tracker_presentation.search.util.dayOfMonthKey
import com.example.tracker_presentation.search.util.mealNameKey
import com.example.tracker_presentation.search.util.monthKey
import com.example.tracker_presentation.search.util.yearKey
import com.example.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     /*   CompositionLocalProvider(LocalSpacing provides NewDimens()) {
            // things in this scope will use NewDimens
        }*/

       // LocalSpacing.current
        // this thing outside the scope of CompositionLocalProvider
        // will use
        // val LocalSpacing = compositionLocalOf { Dimensions() }
        // the default defined value

        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()

        setContent {
            CalorieTrackerTheme {
                // we need our navController from compose
                val navController = rememberNavController()
                // to show the snackbar on the UI
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) { padding ->
                    //https://stackoverflow.com/questions/72084865/content-padding-parameter-it-is-not-used

                    // all the navigation events are managed in here instead of having the event in every screen in order
                    // to the modules not be tight coupled with events that do not belong to them and be reusable
                    // this way we only say that the navigation was successful and that the one that implements the module
                    // main activity in this case is the responsible of doing the navigation and passing the callbacks to the functions
                    // our navHost
                    NavHost(
                        navController = navController,
                        // manages if we already did the onboarding or not
                        startDestination = if (shouldShowOnboarding) {
                            Route.WELCOME
                        } else Route.TRACKER_OVERVIEW,
                        modifier = Modifier.padding(padding)
                    ) {

                        // each of our routes allowed
                        composable(Route.WELCOME) {
                            WelcomeScreen { navController.navigate(Route.GENDER) }
                            // we could also make use of an extension function in this package navigate to do it like this
                            //  WelcomeScreen(onNavigate = navController::navigate)

                        }

                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNextClick = { navController.navigate(Route.HEIGHT) }
                            )
                        }
                        composable(Route.GENDER) {
                            // the viewModel is a default parameter created on the Screen
                            GenderScreen(onNextClick = {
                                navController.navigate(Route.AGE)
                            })
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                })
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNextClick = {
                                    navController.navigate(Route.ACTIVITY)
                                })
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNextClick = {
                                    navController.navigate(Route.TRACKER_OVERVIEW)
                                })

                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(onNextClick = {
                                navController.navigate(Route.GOAL)
                            })
                        }
                        composable(Route.GOAL) {
                            GoalScreen(onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            })
                        }

                        composable(Route.TRACKER_OVERVIEW) {
                            // this is the callback we pass to the screen that gets executed when the success is triggered in the viewModel
                            // this is the callback passed, the data is sent from inside the screen and controlled what happens after in here

                            // this totally matches the route below
                            TrackerOverviewScreen(
                                onNavigateToSearch = { mealName, day, month, year -> // params received from the screen
                                    // what happens with our callback
                                    navController.navigate(
                                        Route.SEARCH + "/$mealName" +
                                                "/$day" +
                                                "/$month" +
                                                "/$year"
                                    )
                                }
                            )
                        }
                        composable(
                            // our route and arguments like URL, remember it only allows primitive date types
                            // we use our "basic route"  + the arguments we need for it
                            // in the arguments section we provide each of them and their type
                            route = Route.SEARCH + "/{$mealNameKey}/{$dayOfMonthKey}/{$monthKey}/{$yearKey}",
                            arguments = listOf(
                                navArgument(mealNameKey) {
                                    type = NavType.StringType
                                },
                                navArgument(dayOfMonthKey) {
                                    type = NavType.IntType
                                },
                                navArgument(monthKey) {
                                    type = NavType.IntType
                                },
                                navArgument(yearKey) {
                                    type = NavType.IntType
                                },
                            )
                        ) {
                            // the retrieved arguments passed
                            val mealName = it.arguments?.getString(mealNameKey)!!
                            val dayOfMonth = it.arguments?.getInt(dayOfMonthKey)!!
                            val month = it.arguments?.getInt(monthKey)!!
                            val year = it.arguments?.getInt(yearKey)!!

                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}