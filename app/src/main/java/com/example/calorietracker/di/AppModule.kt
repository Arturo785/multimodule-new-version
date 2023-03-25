package com.example.calorietracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.core.data.preferences.DefaultPreferences
import com.example.core.domain.preferences.Preferences
import com.example.core.domain.use_case.FilterOutDigitsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// supposed to live as the whole app is alive

// this dependencies provided are supposed to be used whole app wide


// our main module and kind of main component in old dagger which instances will live in
// the singleton component, and accessible in that scope
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //With @Singleton when the app creates this Component instance will retain all the
    // dependencies with the annotation, if another instance of the component is created
    // the injected dependencies will still be a singleton however will be distinct from the ones
    // from the other module
    // in an nutshell, the dependencies attached to the component will be the same but different
    // components will not share the same allocation of memory

    // App component 1
    // SharedPreferences = memory allocation 1234 (same instance when injected along the app)

    // App component 2
    // SharedPreferences = memory allocation 7890 (same instance when injected along the app)

    // needed for the default preferences file
    @Provides
    @Singleton // because of this only one instance will live as long the parent instance lives
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton // because of this only one instance will live as long the parent instance lives
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideFilterOutDigitsUseCase() = FilterOutDigitsUseCase()
}