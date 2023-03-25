package com.example.tracker_domain.model

import java.time.LocalDate

// this are the models that will be used for the app, the ones in data package are for retrieve data
// this ones are for the usage of the tracker feature

data class TrackedFood(
    val name: String,
    val carbs: Int,
    val protein: Int,
    val fat: Int,
    val imageUrl: String?,
    val mealType: MealType,
    val amount: Int,
    val date: LocalDate,
    val calories: Int,
    val id: Int? = null
)