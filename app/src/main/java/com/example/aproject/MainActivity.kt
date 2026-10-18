package com.example.aproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.aproject.core.theme.AProjectTheme
import com.example.aproject.core.work.WorkScheduler
import com.example.aproject.feature.advice.presentation.screen.AdviceScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Entry point of the UI. Hosts the Compose content with edge-to-edge rendering
 * and the application theme.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var workScheduler: WorkScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        workScheduler.scheduleDailyAdvice()

        setContent {
            AProjectTheme {

                AdviceScreen()
            }
        }
    }
}