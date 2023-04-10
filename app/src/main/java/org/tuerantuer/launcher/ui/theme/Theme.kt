package org.tuerantuer.launcher.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6F37C3),

    primaryContainer = Color(0xFF121213),
    onPrimaryContainer = Color(0xFFF7F7F9),
    onPrimary = Color(0xFFF7F7F9),
    surface = Color(0xFFF7F7F9),
    background = Color(0xFFF7F7F9),
    onSurface = Color(0xFF121213),
    onBackground = Color(0xFF121213),
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
            val backgroundColor = colorScheme.background.toArgb()
            (view.context as Activity).window.statusBarColor = backgroundColor
            (view.context as Activity).window.navigationBarColor = backgroundColor
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
