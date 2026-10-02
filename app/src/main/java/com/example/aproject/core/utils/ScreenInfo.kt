package com.example.aproject.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

/**
 * Returns and remembers the current screen size in pixels and dp, and screen orientation.
 */
@Composable
fun rememberScreenInfo(): ScreenInfo {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current

    return remember(density, windowInfo) {
        val screenHeightPx = windowInfo.containerSize.height.toFloat()
        val screenWidthPx = windowInfo.containerSize.width.toFloat()

        val screenHeightDp = with(density) { screenHeightPx.toDp() }
        val screenWidthDp = with(density) { screenWidthPx.toDp() }

        ScreenInfo(
            heightPx = screenHeightPx,
            widthPx = screenWidthPx,
            heightDp = screenHeightDp,
            widthDp = screenWidthDp,
            isLandscape = windowInfo.containerSize.width > windowInfo.containerSize.height
        )
    }
}

/**
 * Screen dimensions in pixels and dp, and screen orientation.
 */
data class ScreenInfo(
    val heightPx: Float,
    val widthPx: Float,
    val heightDp: Dp,
    val widthDp: Dp,
    val isLandscape: Boolean
)