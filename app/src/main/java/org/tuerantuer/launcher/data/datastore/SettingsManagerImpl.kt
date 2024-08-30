package org.tuerantuer.launcher.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * Default implementation of [SettingsManager].
 */
class SettingsManagerImpl(
    private val dataStore: DataStore<Preferences>,
) : SettingsManager {
    companion object {
        private val APP_ICON_SIZE_DP_KEY = intPreferencesKey("app_icon_size_dp")
        private val APP_TEXT_SIZE_DP_KEY = floatPreferencesKey("app_text_size_dp")
        private val IS_USER_ONBOARDED_KEY = booleanPreferencesKey("is_user_onboarded")
        private val WALLPAPER_TYPE_KEY = stringPreferencesKey("wallpaper_type")
        private val USE_SCROLL_BUTTONS = booleanPreferencesKey("use_buttons_for_scrolling")
        private val DEFAULT_APP_ICON_SIZE = AppIconSize.M
        private val DEFAULT_APP_TEXT_SIZE = AppTextSize.M
        private const val DEFAULT_IS_USER_ONBOARDED = false
        private const val DEFAULT_USE_SCROLL_BUTTONS = false
        private val DEFAULT_WALLPAPER_TYPE = WallpaperType.SOLID_COLOR

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
            val appTextSizeFloat = preferences[APP_TEXT_SIZE_DP_KEY] ?: DEFAULT_APP_TEXT_SIZE.scalingFactor
            val appTextSize = AppTextSize.values().firstOrNull { it.scalingFactor == appTextSizeFloat } ?: DEFAULT_APP_TEXT_SIZE
            val isUserOnboarded = preferences[IS_USER_ONBOARDED_KEY] ?: DEFAULT_IS_USER_ONBOARDED
            val wallpaperTypeKey = preferences[WALLPAPER_TYPE_KEY]
            val useScrollButtons = preferences[USE_SCROLL_BUTTONS] ?: DEFAULT_USE_SCROLL_BUTTONS
            val wallpaperType = WallpaperType.values().firstOrNull { it.key == wallpaperTypeKey }
                ?: DEFAULT_WALLPAPER_TYPE
            Settings(appIconSize, appTextSize, isUserOnboarded, wallpaperType, useScrollButtons)
        }

    override suspend fun setAppIconSize(size: AppIconSize) {
        dataStore.edit { preferences -> preferences[APP_ICON_SIZE_DP_KEY] = size.sizeDp }
    }

    override suspend fun setAppTextSize(size: AppTextSize) {
        dataStore.edit { preferences -> preferences[APP_TEXT_SIZE_DP_KEY] = size.scalingFactor }
    }

    override suspend fun setIsUserOnboarded(isOnboarded: Boolean) {
        dataStore.edit { settings -> settings[IS_USER_ONBOARDED_KEY] = isOnboarded }
    }

    override suspend fun setWallpaperType(type: WallpaperType) {
        dataStore.edit { settings -> settings[WALLPAPER_TYPE_KEY] = type.key }
    }

    override suspend fun setUseScrollButtons(useButtons: Boolean) {
        dataStore.edit { settings -> settings[USE_SCROLL_BUTTONS] = useButtons }
    }
}
