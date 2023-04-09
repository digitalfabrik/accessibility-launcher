package org.tuerantuer.launcher.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Default implementation of [SettingsManager].
 *
 * @author Peter Huber
 * Created on 09/04/2023
 */
class SettingsManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsManager {
    companion object {
        private val APP_ICON_SIZE_DP_KEY = intPreferencesKey("app_icon_size_dp")
        private val IS_USER_ONBOARDED_KEY = booleanPreferencesKey("is_user_onboarded")
        private val DEFAULT_APP_ICON_SIZE = AppIconSize.Medium
        private const val DEFAULT_IS_USER_ONBOARDED = false
    }

    override val settings: Flow<Settings> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val appIconSizeInt = preferences[APP_ICON_SIZE_DP_KEY] ?: DEFAULT_APP_ICON_SIZE.sizeDp
            val appIconSize = AppIconSize.values().firstOrNull { it.sizeDp == appIconSizeInt } ?: DEFAULT_APP_ICON_SIZE
            val isUserOnboarded = preferences[IS_USER_ONBOARDED_KEY] ?: DEFAULT_IS_USER_ONBOARDED
            Settings(appIconSize, isUserOnboarded)
        }

    override suspend fun setAppIconSize(size: AppIconSize) {
        dataStore.edit { preferences -> preferences[APP_ICON_SIZE_DP_KEY] = size.sizeDp }
    }

    override suspend fun setIsUserOnboarded(isOnboarded: Boolean) {
        dataStore.edit { settings -> settings[IS_USER_ONBOARDED_KEY] = isOnboarded }
    }
}
