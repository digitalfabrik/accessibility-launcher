package org.tuerantuer.launcher.ui.data

import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.app.Apps
import org.tuerantuer.launcher.data.datastore.Settings

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
