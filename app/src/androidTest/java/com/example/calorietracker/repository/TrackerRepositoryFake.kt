package com.example.calorietracker.repository

import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_domain.model.TrackedFood
import com.example.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate
import kotlin.random.Random


//to avoid using the real one with api calls and dao
class TrackerRepositoryFake : TrackerRepository {

    var shouldReturnError = false

    // works as our data holder
    private val trackedFood = mutableListOf<TrackedFood>()
    var searchResults = listOf<TrackableFood>()

    private val getFoodsForDateFlow =
        MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(searchResults)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFood.add(food.copy(id = Random.nextInt())) // this is because it expects a random id per food saved
        getFoodsForDateFlow.emit(trackedFood)
        // simulates the flow that reacts to every change in the db
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFood.remove(food)
        getFoodsForDateFlow.emit(trackedFood)
        // simulates the flow that reacts to every change in the db
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}