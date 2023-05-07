package org.tuerantuer.launcher.data.datastore

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import org.tuerantuer.launcher.ui.screen.AllAppsScreen
import org.tuerantuer.launcher.ui.screen.HomeScreen

/**
 * Determines the background of the [HomeScreen] and [AllAppsScreen].
 *
 * @author Peter Huber
 * Created on 07/05/2023
 */
enum class WallpaperType(val key: String) {
    /**
     * Use [ColorScheme.background] from [MaterialTheme.colorScheme] as the background color.
     */
    SOLID_COLOR("solid_color"),

    /**
     * Show the system's wallpaper, but overlay a dark tint on top of it.
     */
    DARKENED_CUSTOM_WALLPAPER("darkened_custom_wallpaper"),

    /**
     * Show the system's wallpaper without darkening it.
     */
    CUSTOM_WALLPAPER("custom_wallpaper"),
}