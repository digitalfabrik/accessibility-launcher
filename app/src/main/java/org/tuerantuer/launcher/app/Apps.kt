package org.tuerantuer.launcher.app

/**
 * All installed apps of the user along with their favorites.
 */
data class Apps(val allApps: List<AppItemInfo> = emptyList(), val favorites: List<AppItemInfo> = emptyList())
