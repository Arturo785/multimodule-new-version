package com.example.onboarding_domain.di

import com.example.onboarding_domain.use_case.ValidateNutrients
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


// in here because only the onboarding part makes usage of this dependency, not like the others that
// live in the app module because are used whole app wide
@Module
@InstallIn(ViewModelComponent::class) // attached to the viewModel scope and component
object OnboardingDomainModule {


    /*Scope annotation for bindings that should exist for the life of a a single androidx.lifecycle.ViewModel.
Use this scope annotation when you want to define a dependency in the dagger.hilt.android.components.ViewModelComponent
 for which a single instance will be provided across all other dependencies for a single dagger.hilt.android.lifecycle.HiltViewModel-annotated ViewModel.
  Other ViewModels that request the scoped dependency will receive a different instance.
   For sharing the same instance of a dependency across all ViewModels use a scope from one of the parent components of dagger.hilt.android.components.ViewModelComponent,
    such as javax.inject.Singleton or ActivityRetainedScoped.*/
    @Provides
    @ViewModelScoped
    fun provideValidateNutrientsUseCase(): ValidateNutrients {
        return ValidateNutrients()
    }

}