package com.example.aproject.feature.advice.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.aproject.core.theme.Typography

private val AdviceLightColors = lightColorScheme(
    primary = Color(0xFF844EE8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDCCFC),
    onPrimaryContainer = Color(0xFF1A1A2E),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFFF8F7FF),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    onSurfaceVariant = Color(0xFF1A1A2E),
    surfaceContainerHighest = Color(0xFFF5F2FA),
    surfaceContainerLow = Color(0xFFF3F2FA),
    error = Color(0xFFB00020),
    onError = Color.White
)

private val AdviceDarkColors = darkColorScheme(
    primary = Color(0xFF123D60),
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF0F2D52),
    onPrimaryContainer = Color(0xFFCAC4D0),
    secondary = Color(0xFF03DAC6),
    onSecondary = Color.Black,
    background = Color(0xFF1B1A1E),
    onBackground = Color(0xFFCAC4D0),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFCAC4D0),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceContainerHighest = Color(0xFF242328),
    surfaceContainerLow = Color(0xFF242328),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

/**
 * Theme for the advice feature with its own color palette.
 */
@Composable
fun AdviceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AdviceDarkColors else AdviceLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // General typography from the main file
        content = content
    )
}