package com.example.aproject.navigation

sealed class Screen(val route: String) {

    object Advice : Screen("advice")
}