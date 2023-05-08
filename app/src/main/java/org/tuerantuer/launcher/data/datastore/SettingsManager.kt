package org.tuerantuer.launcher.data.datastore

import kotlinx.coroutines.flow.Flow

/**
 * To read and write all settings of the app that can be persisted in key value pairs. Only the favorites are settings
 * that are not persisted this way.
 */
interface SettingsManager {
    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
    }

    val settings: Flow<Settings>

    /**
     * @see Settings.appIconSize
     */
    suspend fun setAppIconSize(size: AppIconSize)

    /**
     * @see Settings.isUserOnboarded
     */
    suspend fun setIsUserOnboarded(isOnboarded: Boolean)

    /**
     * @see Settings.wallpaperType
     */
    suspend fun setWallpaperType(type: WallpaperType)
}
