package org.tuerantuer.launcher.ui.theme

import androidx.compose.ui.graphics.Color
import org.tuerantuer.launcher.data.datastore.Settings
import org.tuerantuer.launcher.data.datastore.WallpaperType

val blue = Color(0xFF4B73D9)
val purple = Color(0xFF6F37C3)
val almostWhite = Color(0xFFF7F7F9)
val almostBlack = Color(0xFF121213)
val gray = Color(0xFF575759)

// Darkmode Color variations
val almostBlackDM = Color(0xFF1C1C1E)
val almostWhiteDM = Color(0xFFE6E0E9)
val purpleDM = Color(0xFF9770D2)

/**
 * The scrim color used to darken the wallpaper if the user sets the [wallpaper type][Settings.wallpaperType] to
 * [WallpaperType.DARKENED_CUSTOM_WALLPAPER].
 */
val wallpaperScrim = Color(0x50000000)
