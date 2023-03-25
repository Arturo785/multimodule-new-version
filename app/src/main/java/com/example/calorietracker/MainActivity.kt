package com.example.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
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
                            WelcomeScreen { routeSelected -> // the lambda passed to it
                                navController.navigate(routeSelected.route)
                            }
                            // we could also make use of an extension function in this package navigate to do it like this
                            //  WelcomeScreen(onNavigate = navController::navigate)

                        }

                        composable(Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
                        }
                        composable(Route.GENDER) {
                            // the viewModel is a default parameter created on the Screen
                            GenderScreen(onNavigate = { routeSelected -> // the lambda passed to it
                                navController.navigate(routeSelected.route)
                            })
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState, // to show the snackbar
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })

                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen(
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
                        }
                        composable(Route.GOAL) {
                            GoalScreen(
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
                        }

                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigate = { routeSelected -> // the lambda passed to it
                                    navController.navigate(routeSelected.route)
                                })
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