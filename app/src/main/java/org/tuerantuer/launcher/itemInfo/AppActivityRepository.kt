package org.tuerantuer.launcher.itemInfo

import kotlinx.coroutines.flow.StateFlow

/**
 * Here you can query the users favorites and all apps.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
interface AppActivityRepository {
    /**
     * The favorites of the user.
     */
    val favorites: StateFlow<List<AppItemInfo>>

    /**
     * All installed apps of the user.
     */
    val allApps: StateFlow<List<AppItemInfo>>

    /**
     * Sets the favorites of the user.
     */
    suspend fun setFavorites(newFavorites: List<AppItemInfo>)
}
