package com.example.tracker_domain.model

// this are the models that will be used for the app, the ones in data package are for retrieve data
// this ones are for the usage of the tracker feature

data class TrackableFood(
    val name: String,
    val imageUrl: String?,
    val caloriesPer100g: Int,
    val carbsPer100g: Int,
    val proteinPer100g: Int,
    val fatPer100g: Int
)