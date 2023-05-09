package org.tuerantuer.launcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import org.tuerantuer.launcher.data.datastore.WallpaperType

private val LightColorScheme = lightColorScheme(
    primary = purple,
    primaryContainer = almostBlack,
    secondary = purple,
    secondaryContainer = gray,
    onSecondaryContainer = almostWhite,
    onPrimaryContainer = almostWhite,
    onPrimary = almostWhite,
    surface = almostWhite,
    background = almostWhite,
    onSurface = almostBlack,
    onBackground = almostBlack,
)

@Immutable
data class ThemeExtension(
    val onWallpaperText: TextStyle,
    val onWallpaperBackground: Color,
)

private val LocalThemeExtension = staticCompositionLocalOf {
    ThemeExtension(
        onWallpaperText = TextStyle.Default,
        onWallpaperBackground = Color.Unspecified,
    )
}

object LauncherTheme {
    val all: ThemeExtension
        @Composable
        get() = LocalThemeExtension.current
}

@Composable
fun LauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    wallpaperType: WallpaperType = WallpaperType.SOLID_COLOR,
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme

    val onWallpaperText = when (wallpaperType) {
        WallpaperType.SOLID_COLOR -> MaterialTheme.typography.labelLarge.copy(
            color = almostBlack,
        )
        WallpaperType.CUSTOM_WALLPAPER,
        WallpaperType.DARKENED_CUSTOM_WALLPAPER,
        -> MaterialTheme.typography.labelLarge.copy(
            color = almostWhite,
            shadow = Shadow(
                color = Color.Black,
                offset = Offset(0f, 2f),
                blurRadius = 6f,
            ),
        )
    }

    val onWallpaperBackground = when (wallpaperType) {
        WallpaperType.SOLID_COLOR -> MaterialTheme.colorScheme.background
        WallpaperType.CUSTOM_WALLPAPER -> Color.Transparent
        WallpaperType.DARKENED_CUSTOM_WALLPAPER -> Color.Black.copy(alpha = 0.5f)
    }

    val themeExtension = ThemeExtension(
        onWallpaperText = onWallpaperText,
        onWallpaperBackground = onWallpaperBackground,
    )
    CompositionLocalProvider(
        LocalThemeExtension provides themeExtension,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            shapes = Shapes,
            typography = Typography,
            content = content,
        )
    }
}
