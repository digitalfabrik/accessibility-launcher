package org.tuerantuer.launcher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.tuerantuer.launcher.data.database.favorites.FavoriteDao
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity

/**
 *  The Room Database that contains the favorites table.
 *
 * @author Peter Huber
 * Created on 03/04/2023
 */
@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class LauncherDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "Database.db"

        fun build(applicationContext: Context): LauncherDatabase {
            return Room.databaseBuilder(
                applicationContext,
                LauncherDatabase::class.java,
                DATABASE_NAME,
            ).build()
        }
    }

    abstract val favoriteDao: FavoriteDao
}
