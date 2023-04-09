package org.tuerantuer.launcher.data

import kotlinx.coroutines.flow.Flow

/**
 * To read and write all settings of the app that can be persisted in key value pairs. Only the favorites are settings
 * that are not persisted this way.
 *
 * @author Peter Huber
 * Created on 09/04/2023
 */
interface SettingsManager {
    companion object {
        const val USER_PREFERENCES_NAME = "user_preferences"
    }

    val settings: Flow<Settings>

    suspend fun setAppIconSize(size: AppIconSize)
}
