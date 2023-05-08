package org.tuerantuer.launcher.data.database.favorites

import android.database.sqlite.SQLiteException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/**
 *  Data Access Object for the [FavoriteEntity] table.
 */
@Dao
interface FavoriteDao {
    @Query("SELECT * FROM ${FavoriteEntity.TABLE_NAME}")
    @Throws(SQLiteException::class)
    fun observeAll(): Flow<List<FavoriteEntity>>

    @Transaction
    @Throws(SQLiteException::class)
    suspend fun replaceAllFavorites(
        newFavorites: List<FavoriteEntity>,
    ) {
        deleteAll()
        insertAll(newFavorites)
    }

    @Upsert
    @Throws(SQLiteException::class)
    suspend fun upsert(favoriteEntity: FavoriteEntity)

    @Insert
    @Throws(SQLiteException::class)
    suspend fun insertAll(favoriteEntities: List<FavoriteEntity>)

    @Query("DELETE FROM ${FavoriteEntity.TABLE_NAME}")
    @Throws(SQLiteException::class)
    suspend fun deleteAll()
}
