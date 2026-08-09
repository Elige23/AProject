package com.example.aproject.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp

@Composable
fun screenSizeInfo(): ScreenInfo {
    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current

    val screenHeightPx = windowInfo.containerSize.height.toFloat()
    val screenWidthPx = windowInfo.containerSize.width.toFloat()

    val screenHeightDp = with(density) { screenHeightPx.toDp() }
    val screenWidthDp = with(density) { screenWidthPx.toDp() }

    return ScreenInfo(
        heightPx = screenHeightPx,
        widthPx = screenWidthPx,
        heightDp = screenHeightDp,
        widthDp = screenWidthDp,
        isLandscape = windowInfo.containerSize.width > windowInfo.containerSize.height
    )
}

data class ScreenInfo(
    val heightPx: Float,
    val widthPx: Float,
    val heightDp: Dp,
    val widthDp: Dp,
    val isLandscape: Boolean
)

// 1. Получаем общую высоту экрана в dp.
// Есть предупреждение, но если использовать новый метод без
// val density = LocalDensity.current, то screenHeightDp = 2424.0.dp, а старый метод возвращает
// screenHeightDp = 923.0.dp.

// Старый метод:
//    val configuration = LocalConfiguration.current //LocalConfiguration.current возвращает логические пиксели (Dp)
//    val screenHeightDp = configuration.screenHeightDp.dp

//по новому методу:
//val density = LocalDensity.current
//val windowInfo = LocalWindowInfo.current //LocalWindowInfo.current возвращает физические пиксели (Px)
//// Получаем высоту в пикселях (Px)
//val screenHeightPx = windowInfo.containerSize.height.toFloat()
//// Конвертируем в Dp (будут те же 923.dp, что и в старом способе)
//val screenHeightDp = with(density) { screenHeightPx.toDp() }
//
//FloatingActionButton(
//onClick = onClick,
//modifier = Modifier.offset(x = (-10).dp, y = -screenHeightDp/8),