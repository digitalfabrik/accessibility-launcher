package org.tuerantuer.launcher.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.tuerantuer.launcher.data.database.LauncherDatabase
import org.tuerantuer.launcher.data.database.favorites.FavoriteDao
import javax.inject.Singleton

/**
 * Provides the database and its data access objects.
 *
 * @author Peter Huber
 * Created on 03/04/2023
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): LauncherDatabase {
        return LauncherDatabase.build(context)
    }

    @Provides
    fun provideTaskDao(database: LauncherDatabase): FavoriteDao = database.favoriteDao
}
