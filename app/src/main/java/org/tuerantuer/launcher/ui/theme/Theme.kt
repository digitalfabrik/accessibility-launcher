package org.tuerantuer.launcher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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
    tertiaryContainer = blue,
    onTertiaryContainer = almostWhite,
    onSecondaryContainer = almostWhite,
    onPrimaryContainer = almostWhite,
    onPrimary = almostWhite,
    surface = almostWhite,
    background = almostWhite,
    onSurface = almostBlack,
    onBackground = almostBlack,
)
private val DarkColorScheme = darkColorScheme(
    primary = purpleDM,
    primaryContainer = almostWhiteDM,
    secondary = purpleDM,
    secondaryContainer = gray,
    tertiaryContainer = blue,
    onTertiaryContainer = almostWhite,
    onSecondaryContainer = almostWhite,
    onPrimaryContainer = almostBlack,
    onPrimary = almostWhiteDM,
    surface = almostBlackDM,
    background = almostBlackDM,
    onSurface = almostWhiteDM,
    onBackground = almostWhiteDM,
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
    scalingFactor: Float = 1.0f,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val onWallpaperText = when (wallpaperType) {
        WallpaperType.SOLID_COLOR -> MaterialTheme.typography.labelLarge.copy(
            color = colorScheme.onBackground,
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
    }.scaleSizeWithHyphens(scalingFactor)

    val onWallpaperBackground = when (wallpaperType) {
        WallpaperType.SOLID_COLOR -> colorScheme.background
        WallpaperType.CUSTOM_WALLPAPER -> Color.Transparent
        WallpaperType.DARKENED_CUSTOM_WALLPAPER -> wallpaperScrim
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
            typography = createTypography(scalingFactor),
            content = content,
        )
    }
}