package com.example.aproject.navigation

/**
 * Navigation routes of the application.
 */
sealed class Screen(val route: String) {

    object Advice : Screen("advice")
}