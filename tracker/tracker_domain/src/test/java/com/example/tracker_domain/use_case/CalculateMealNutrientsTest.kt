package com.example.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import com.example.core.domain.model.ActivityLevel
import com.example.core.domain.model.Gender
import com.example.core.domain.model.GoalType
import com.example.core.domain.model.UserInfo
import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.model.MealType
import com.example.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random


class CalculateMealNutrientsTest {

    // to be instanced later
    private lateinit var calculateMealNutrients: CalculateMealNutrients


    // this function gets executed everytime before a test happens
    @Before
    fun setUp() {
        // our mocked preferences to avoid creating an instance
        val preferences = mockk<Preferences>(relaxed = true)
        // what happens every time this function gets called
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
        // we instantiate our use case
        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated`() {
        // we create a large list of foods
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)

        // amount calculated by our usecase
        val breakfastCalories = result.mealNutrients.values
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        // the raw amount of calories given by the list
        val expectedCalories = trackedFoods
            .filter { it.mealType is MealType.Breakfast }
            .sumOf { it.calories }

        // they should match
        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Carbs for dinner properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                protein = Random.nextInt(100),
                fat = Random.nextInt(100),
                mealType = MealType.fromString(
                    listOf("breakfast", "lunch", "dinner", "snack").random()
                ),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }

        val result = calculateMealNutrients(trackedFoods)


        // amount calculated by our usecase
        val dinnerCarbs = result.mealNutrients.values
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carbs }

        // the raw amount of calories given by the list
        val expectedCarbs = trackedFoods
            .filter { it.mealType is MealType.Dinner }
            .sumOf { it.carbs }

        // they should match
        assertThat(dinnerCarbs).isEqualTo(expectedCarbs)
    }

}