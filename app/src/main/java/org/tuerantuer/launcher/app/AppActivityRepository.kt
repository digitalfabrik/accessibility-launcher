package org.tuerantuer.launcher.app

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
     * Lets you query and observe all installed apps of the user along with their favorites.
     * Note: We filter out the launcher app itself.
     */
    val apps: Flow<Apps>

    /**
     * Sets the favorites of the user.
     */
    @Throws(SQLiteException::class)
    suspend fun setFavorites(newFavorites: List<AppItemInfo>)
}
