package com.example.tracker_domain.use_case


// a wrapper that holds all the useCases to avoid messy viewModels with a lot of injected fields
data class TrackerUseCases(
    val trackFood: TrackFood,
    val searchFood: SearchFood,
    val getFoodsForDate: GetFoodsForDate,
    val deleteTrackedFood: DeleteTrackedFood,
    val calculateMealNutrients: CalculateMealNutrients
)