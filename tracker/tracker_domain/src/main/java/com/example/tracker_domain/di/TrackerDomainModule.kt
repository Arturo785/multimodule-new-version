package com.example.tracker_domain.di

import com.example.core.domain.preferences.Preferences
import com.example.tracker_domain.repository.TrackerRepository
import com.example.tracker_domain.use_case.CalculateMealNutrients
import com.example.tracker_domain.use_case.DeleteTrackedFood
import com.example.tracker_domain.use_case.GetFoodsForDate
import com.example.tracker_domain.use_case.SearchFood
import com.example.tracker_domain.use_case.TrackFood
import com.example.tracker_domain.use_case.TrackerUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // available in the viewModel scope component
object TrackerDomainModule {

    /*
    *
    * Scope annotation for bindings that should exist for the life of a a single androidx.lifecycle.ViewModel.
    Use this scope annotation when you want to define a dependency in the dagger.hilt.android.components.
* ViewModelComponent for which a single instance will be provided across all other dependencies for
*  a single dagger.hilt.android.lifecycle.HiltViewModel-annotated ViewModel.
*  Other ViewModels that request the scoped dependency will receive a different instance.
*  For sharing the same instance of a dependency across all ViewModels use a scope from one of the parent components
*  of dagger.hilt.android.components.ViewModelComponent, such as javax.inject.Singleton or ActivityRetainedScoped.
    *
    * */
    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository, // provided in the tracker data di module
        preferences: Preferences // provided in the app di module (used app wide)
    ): TrackerUseCases {
        // our wrapper and each useCase that contains it's dependencies
        return TrackerUseCases(
            trackFood = TrackFood(repository),
            searchFood = SearchFood(repository),
            getFoodsForDate = GetFoodsForDate(repository),
            deleteTrackedFood = DeleteTrackedFood(repository),
            calculateMealNutrients = CalculateMealNutrients(preferences)
        )
    }
}