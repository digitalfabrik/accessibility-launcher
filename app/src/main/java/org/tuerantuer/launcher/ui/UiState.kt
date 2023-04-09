package org.tuerantuer.launcher.ui

import org.tuerantuer.launcher.data.Settings
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.itemInfo.Apps

/**
 * Dictates what should be displayed to the user.
 *
 * @author Peter Huber
 * Created on 03/04/2023
 */
data class UiState(
    val screenState: ScreenState,
    val apps: Apps = Apps(),
    val settings: Settings = Settings(),
) {
    val favorites: List<AppItemInfo>
        get() = apps.favorites
    val allApps: List<AppItemInfo>
        get() = apps.allApps
}
