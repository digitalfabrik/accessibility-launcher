package org.tuerantuer.launcher.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val LightColorScheme = lightColorScheme(
    primary = purple,
    primaryContainer = almostBlack,
    onPrimaryContainer = almostWhite,
    onPrimary = almostWhite,
    surface = almostWhite,
    background = almostWhite,
    onSurface = almostBlack,
    onBackground = almostBlack,
)

@Composable
fun LauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.apply {
                val backgroundColor = colorScheme.background.toArgb()
                statusBarColor = backgroundColor
                navigationBarColor = backgroundColor
            }
            ViewCompat.getWindowInsetsController(view)?.apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content,
    )
}
