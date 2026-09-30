package com.example.aproject.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Entry point of the application. Initializes Hilt and global app-wide dependencies.
 */
@HiltAndroidApp
class MainApplication : Application() {

}