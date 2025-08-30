package com.example.architecturepatterns

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the Architecture Patterns app.
 * 
 * This class is required for Hilt dependency injection to work properly.
 * It serves as the entry point for the application and initializes Hilt.
 */
@HiltAndroidApp
class ArchitecturePatternsApp : Application()
