package org.tuerantuer.launcher.util.extension

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import org.tuerantuer.launcher.data.SettingsManager

val Context.dataStore by preferencesDataStore(name = SettingsManager.USER_PREFERENCES_NAME)
