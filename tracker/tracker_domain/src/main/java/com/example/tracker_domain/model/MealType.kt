package com.example.tracker_domain.model


// this are the models that will be used for the app, the ones in data package are for retrieve data
// this ones are for the usage of the tracker feature

sealed class MealType(val name: String) {
    object Breakfast: MealType("breakfast")
    object Lunch: MealType("lunch")
    object Dinner: MealType("dinner")
    object Snack: MealType("snack")

    companion object {
        fun fromString(name: String): MealType {
            return when(name) {
                "breakfast" -> Breakfast
                "lunch" -> Lunch
                "dinner" -> Dinner
                "snack" -> Snack
                else -> Breakfast
            }
        }
    }
}