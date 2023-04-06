package com.example.calorietracker

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.example.calorietracker.repository.TrackerRepositoryFake
import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.Gender
import com.example.core.domain.model.GoalType
import com.example.core.domain.model.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.use_case.FilterOutDigitsUseCase
import com.example.core.navigation.Route
import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_domain.use_case.CalculateMealNutrients
import com.example.tracker_domain.use_case.DeleteTrackedFood
import com.example.tracker_domain.use_case.GetFoodsForDate
import com.example.tracker_domain.use_case.SearchFood
import com.example.tracker_domain.use_case.TrackFood
import com.example.tracker_domain.use_case.TrackerUseCases
import com.example.tracker_presentation.search.SearchScreen
import com.example.tracker_presentation.search.SearchViewModel
import com.example.tracker_presentation.search.util.dayOfMonthKey
import com.example.tracker_presentation.search.util.mealNameKey
import com.example.tracker_presentation.search.util.monthKey
import com.example.tracker_presentation.search.util.yearKey
import com.example.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.example.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt


@ExperimentalComposeUiApi
@ExperimentalCoilApi
@HiltAndroidTest
class TrackerOverviewE2E {

    // rules are for for adding extra behaviour for every test case that is run
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // with this we can set the composable content
    //@get:Rule
    // val composeRule = createAndroidComposeRule<MainActivity>()

    // seems to work with this and
    //debugImplementation("androidx.compose.ui:ui-test-manifest:1.3.0-rc01")
    //although same logic
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()


    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences

    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        // mock our sharedPreferences
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 180,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        //hiltRule.inject() injects dependencies needed, not needed in here
        repositoryFake = TrackerRepositoryFake()

        // our holder for use cases that will contain all of them with our mocked repository and preferences
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )

        // our viewModels
        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )

        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            filterOutDigitsUseCase = FilterOutDigitsUseCase()
        )

        // we use our compose rule to put compose into this class
        // basically we are creating the whole structure of our mainActivity per all our test to be done
        // we only care to test this routes available in here
        // we are testing the tracker overview in this part so we forget about the onboarding one

        // but here we create our own structure of what we want to see and with which items
        composeRule.activity.setContent {
            // to show snackbar
            val scaffoldState = rememberScaffoldState()
            // our navController
            navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) { padding ->
                // we create our navHost
                NavHost(
                    navController = navController,
                    startDestination = Route.TRACKER_OVERVIEW,
                    modifier = Modifier.padding(padding)
                ) {
                    // and now each of our different routes available
                    composable(Route.TRACKER_OVERVIEW) {
                        TrackerOverviewScreen(
                            onNavigateToSearch = { mealName, day, month, year ->
                                navController.navigate(
                                    Route.SEARCH + "/$mealName" +
                                            "/$day" +
                                            "/$month" +
                                            "/$year"
                                )
                            },
                            viewModel = trackerOverviewViewModel // we need to pass our fake viewModels
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
                            },
                            viewModel = searchViewModel // we need to pass our fake viewModels
                        )
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        // we populate our fake repository to have the data we expect
        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            )
        )

        // expected macros same as the one above
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        // .onNodeWithText = // finds every node in the screen with that text literally like "Add Breakfast"
        // .onNodeWithContentDescription = // finds every node in the screen with that content description literally like "Breakfast"
        // .onNodeWithTag = = // this is a tag that we apply to our composable in an explicit way

        // starts with the toggle options hidden
        composeRule
            .onNodeWithText("Add Breakfast")
            .assertDoesNotExist()
        // clicks the button of breakfast
        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()
        // add breakfast should appear
        composeRule
            .onNodeWithText("Add Breakfast")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("Add Breakfast")
            .performClick()

        // takes us to search screen
        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.SEARCH)
        ).isTrue()

        // in the search field put banana and then click for it
        composeRule
            .onNodeWithTag("search_textfield")
            .performTextInput("banana")
        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        composeRule.onRoot().printToLog("COMPOSE TREE")

        //TrackableFoodItem
        // clicks on the card, adds the amount in the textField with semantics content description
        // and tracks the food
        composeRule
            .onNodeWithContentDescription("Card item")
            .performClick()
        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toStr())
        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        // make sure we go back to tracker overview

        assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()

        // this is because it may be more than one with that text so we use the 1st one
        //NutrientsHeader.kt I think
        // the expected ones are in this file from the banana
        composeRule
            .onAllNodesWithText(expectedCarbs.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedProtein.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedFat.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedCalories.toStr())
            .onFirst()
            .assertIsDisplayed()
    }


}