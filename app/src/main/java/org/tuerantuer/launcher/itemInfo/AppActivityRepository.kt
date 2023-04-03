package org.tuerantuer.launcher.itemInfo

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.flow.Flow

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
    val favorites: Flow<List<AppItemInfo>>

    /**
     * All installed apps of the user.
     */
    val allApps: Flow<List<AppItemInfo>>

    /**
     * Sets the favorites of the user.
     */
    @Throws(SQLiteException::class)
    suspend fun setFavorites(newFavorites: List<AppItemInfo>)
}
