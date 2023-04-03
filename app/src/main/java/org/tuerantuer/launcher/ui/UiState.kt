package org.tuerantuer.launcher.ui

import org.tuerantuer.launcher.itemInfo.AppItemInfo

/**
 * Dictates what should be displayed to the user.
 *
 * @author Peter Huber
 * Created on 03/04/2023
 */
data class UiState(
    val screenState: ScreenState,
    val favorites: List<AppItemInfo> = emptyList(),
    val allApps: List<AppItemInfo> = emptyList(),
)
