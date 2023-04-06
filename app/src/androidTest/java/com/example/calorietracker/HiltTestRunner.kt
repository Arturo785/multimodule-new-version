package com.example.calorietracker

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication


// this is done in order to create an application context for our UI tests
// this is like our

/*
@HiltAndroidApp
class CaloryTrackerApp: Application()*/

// but for instrumented test cases
// we also define in our gradle that this app file should be used for Integration tests
//  testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" this
// to this
//  testInstrumentationRunner = "com.example.calorietracker.HiltTestRunner"

class HiltTestRunner: AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}