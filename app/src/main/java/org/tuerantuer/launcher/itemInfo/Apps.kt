package org.tuerantuer.launcher.itemInfo

/**
 * All installed apps of the user along with their favorites.
 *
 * @author Peter Huber
 * Created on 09/04/2023
 */
data class Apps(val allApps: List<AppItemInfo>, val favorites: List<AppItemInfo>)
