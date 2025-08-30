package com.example.dependencyhilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class that initializes Hilt dependency injection.
 * 
 * The @HiltAndroidApp annotation triggers Hilt's code generation and
 * creates the base component for the application.
 */
@HiltAndroidApp
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Any additional application initialization can go here
    }
}
