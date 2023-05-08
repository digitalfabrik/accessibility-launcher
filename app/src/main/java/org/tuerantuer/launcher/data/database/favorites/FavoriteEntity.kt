package org.tuerantuer.launcher.data.database.favorites

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity.Key.CLASS_NAME
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity.Key.PACKAGE_NAME
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity.Key.PCU_INDEX_NAME
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity.Key.TABLE_NAME
import org.tuerantuer.launcher.data.database.favorites.FavoriteEntity.Key.USER

/**
 * A favorite is an app that the user has pinned to the home screen.
 */
@Entity(
    tableName = TABLE_NAME,
    indices = [
        Index(
            name = PCU_INDEX_NAME,
            value = [PACKAGE_NAME, CLASS_NAME, USER],
            unique = true,
        ),
    ],
)
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Int = 0,
    @ColumnInfo(name = PACKAGE_NAME) val packageName: String,
    @ColumnInfo(name = CLASS_NAME) val className: String,
    @ColumnInfo(name = USER) val user: Long,
    @ColumnInfo(name = FAVORITES_INDEX) val favoritesIndex: Int,
) {
    companion object Key {
        const val TABLE_NAME = "Favorite"
        const val ID = "id"
        const val PACKAGE_NAME = "package_name"
        const val CLASS_NAME = "class_name"
        const val USER = "user"
        const val FAVORITES_INDEX = "favorites_index"
        const val PCU_INDEX_NAME = "pcu_index"
    }
}
