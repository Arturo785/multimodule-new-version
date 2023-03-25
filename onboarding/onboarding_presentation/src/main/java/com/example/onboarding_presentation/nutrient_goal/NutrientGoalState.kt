package com.example.onboarding_presentation.nutrient_goal


// we have a default state in order to have something to show
// basically defines what is shown in the UI
data class NutrientGoalState(
    val carbsRatio: String = "40",
    val proteinRatio: String = "30",
    val fatRatio: String = "30"
)