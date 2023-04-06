package com.example.calorietracker

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.calorietracker.repository.TrackerRepositoryFake
import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.Gender
import com.example.core.domain.model.GoalType
import com.example.core.domain.model.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.use_case.FilterOutDigitsUseCase
import com.example.core.navigation.Route
import com.example.onboarding_domain.use_case.ValidateNutrients
import com.example.onboarding_presentation.activity.ActivityScreen
import com.example.onboarding_presentation.activity.ActivityViewModel
import com.example.onboarding_presentation.age.AgeScreen
import com.example.onboarding_presentation.age.AgeViewModel
import com.example.onboarding_presentation.gender.GenderScreen
import com.example.onboarding_presentation.gender.GenderViewModel
import com.example.onboarding_presentation.goal.GoalScreen
import com.example.onboarding_presentation.goal.GoalViewModel
import com.example.onboarding_presentation.height.HeightScreen
import com.example.onboarding_presentation.height.HeightViewModel
import com.example.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.example.onboarding_presentation.nutrient_goal.NutrientGoalViewModel
import com.example.onboarding_presentation.weight.WeightScreen
import com.example.onboarding_presentation.weight.WeightViewModel
import com.example.onboarding_presentation.welcome.WelcomeScreen
import com.example.tracker_domain.use_case.CalculateMealNutrients
import com.example.tracker_domain.use_case.DeleteTrackedFood
import com.example.tracker_domain.use_case.GetFoodsForDate
import com.example.tracker_domain.use_case.SearchFood
import com.example.tracker_domain.use_case.TrackFood
import com.example.tracker_domain.use_case.TrackerUseCases
import com.example.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.example.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalComposeUiApi
@ExperimentalCoilApi
@HiltAndroidTest
class OnboardingOverviewE2E {

    private lateinit var scaffoldState: ScaffoldState

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases


    private lateinit var viewModelAge: AgeViewModel

    private lateinit var viewModelGender: GenderViewModel

    private lateinit var viewModelHeight: HeightViewModel

    private lateinit var viewModelWeight: WeightViewModel

    private lateinit var viewModelNutrientGoal: NutrientGoalViewModel

    private lateinit var viewModelActivity: ActivityViewModel

    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel

    private lateinit var viewModelGoal: GoalViewModel

    private lateinit var filterOutDigitsUseCase: FilterOutDigitsUseCase

    private lateinit var validateNutrients: ValidateNutrients

    private lateinit var preferences: Preferences


    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var navController: NavHostController


    @Before
    fun setup() {

        preferences = mockk(relaxed = true)
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

        filterOutDigitsUseCase = FilterOutDigitsUseCase()
        validateNutrients = ValidateNutrients()

        repositoryFake = TrackerRepositoryFake()

        // our holder for use cases that will contain all of them with our mocked repository and preferences
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            searchFood = SearchFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )


        viewModelAge = AgeViewModel(preferences, filterOutDigitsUseCase)

        viewModelGender = GenderViewModel(preferences)

        viewModelHeight = HeightViewModel(preferences, filterOutDigitsUseCase)

        viewModelWeight = WeightViewModel(preferences)

        viewModelNutrientGoal =
            NutrientGoalViewModel(preferences, filterOutDigitsUseCase, validateNutrients)

        viewModelActivity = ActivityViewModel(preferences)

        viewModelGoal = GoalViewModel(preferences)

        trackerOverviewViewModel = TrackerOverviewViewModel(
            preferences = preferences,
            trackerUseCases = trackerUseCases
        )


        composeRule.activity.setContent {
            // to show snackbar
            scaffoldState = rememberScaffoldState()
            // our navController
            navController = rememberNavController()

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState
            ) { padding ->
                // we create our navHost
                NavHost(
                    navController = navController,
                    startDestination = Route.WELCOME,
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
                            onNextClick = { navController.navigate(Route.HEIGHT) },
                            viewModel = viewModelAge
                        )
                    }
                    composable(Route.GENDER) {
                        // the viewModel is a default parameter created on the Screen
                        GenderScreen(
                            onNextClick = {
                                navController.navigate(Route.AGE)
                            },
                            viewModel = viewModelGender
                        )
                    }
                    composable(Route.HEIGHT) {
                        HeightScreen(
                            scaffoldState = scaffoldState, // to show the snackbar
                            onNextClick = {
                                navController.navigate(Route.ACTIVITY)
                            },
                            viewModel = viewModelHeight
                        )
                    }
                    composable(Route.WEIGHT) {
                        WeightScreen(
                            scaffoldState = scaffoldState, // to show the snackbar
                            onNextClick = {
                                navController.navigate(Route.ACTIVITY)
                            },
                            viewModel = viewModelWeight
                        )
                    }
                    composable(Route.NUTRIENT_GOAL) {
                        NutrientGoalScreen(
                            scaffoldState = scaffoldState, // to show the snackbar
                            onNextClick = {
                                navController.navigate(Route.TRACKER_OVERVIEW)
                            },
                            viewModel = viewModelNutrientGoal
                        )
                    }
                    composable(Route.ACTIVITY) {
                        ActivityScreen(
                            onNextClick = {
                                navController.navigate(Route.GOAL)
                            },
                            viewModel = viewModelActivity
                        )
                    }
                    composable(Route.GOAL) {
                        GoalScreen(
                            onNextClick = {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            },
                            viewModel = viewModelGoal
                        )
                    }
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
                }
            }
        }
    }

    @Test
    fun completeOnboardingFlow() {
        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.GENDER)
        ).isTrue()

        composeRule.onNodeWithText("Male").performClick()
        Truth.assertThat(viewModelGender.selectedGender.value).isEqualTo(Gender.Male)

        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.AGE)
        ).isTrue()

        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.HEIGHT)
        ).isTrue()

        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.ACTIVITY)
        ).isTrue()

        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.GOAL)
        ).isTrue()

        composeRule.onNodeWithText("Next").performClick()

        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.NUTRIENT_GOAL)
        ).isTrue()

        composeRule.onAllNodesWithContentDescription("unit text field")[0].performTextReplacement("40")
        composeRule.onAllNodesWithContentDescription("unit text field")[1].performTextReplacement("40")
        composeRule.onAllNodesWithContentDescription("unit text field")[2].performTextReplacement("30")

        composeRule.onNodeWithText("Next").performClick()
        Truth.assertThat(scaffoldState.snackbarHostState.currentSnackbarData?.message)
            .contains("The values must add up to 100%")


        // the snackbar was interfering with the compose rules and nodes, so we dismiss it
        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()

        composeRule.onAllNodesWithContentDescription("unit text field")[0].performTextReplacement("40")
        composeRule.onAllNodesWithContentDescription("unit text field")[1].performTextReplacement("40")
        composeRule.onAllNodesWithContentDescription("unit text field")[2].performTextReplacement("20")


        composeRule.onNodeWithText("Next").performClick()



        Truth.assertThat(
            navController
                .currentDestination
                ?.route
                ?.startsWith(Route.TRACKER_OVERVIEW)
        ).isTrue()
    }

}