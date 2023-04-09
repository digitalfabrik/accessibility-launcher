package org.tuerantuer.launcher.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        private val DEFAULT_APP_ICON_SIZE = AppIconSize.Medium
    }

    private val appIconSize: Flow<AppIconSize> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val appIconSizeInt = preferences[APP_ICON_SIZE_DP_KEY] ?: DEFAULT_APP_ICON_SIZE.sizeDp
            AppIconSize.values().firstOrNull { it.sizeDp == appIconSizeInt } ?: DEFAULT_APP_ICON_SIZE
        }

    override val settings: Flow<Settings> = appIconSize.map { Settings(it) }

    override suspend fun setAppIconSize(size: AppIconSize) {
        dataStore.edit { preferences -> preferences[APP_ICON_SIZE_DP_KEY] = size.sizeDp }
    }
}
