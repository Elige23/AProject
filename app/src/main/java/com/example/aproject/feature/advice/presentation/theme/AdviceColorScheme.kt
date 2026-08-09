package com.example.aproject.feature.advice.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.aproject.core.theme.Typography


val AdviceLightColors = lightColorScheme(
    primary = Color(0xFF6C63FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8E6FF),
    onPrimaryContainer = Color(0xFF1A1A2E),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFFF8F7FF),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    error = Color(0xFFB00020),
    onError = Color.White
)

val AdviceDarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    onPrimaryContainer = Color(0xFFE8E6FF),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE8E6FF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE8E6FF),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun AdviceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AdviceDarkColors else AdviceLightColors //Выбираем цветовую схему в зависимости от темы

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // ← Общая типография из основного файла
        content = content
    )
}