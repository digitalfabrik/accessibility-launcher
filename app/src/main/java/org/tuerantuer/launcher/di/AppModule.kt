package org.tuerantuer.launcher.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.tuerantuer.launcher.app.AppActivityRepository
import org.tuerantuer.launcher.app.AppActivityRepositoryImpl
import org.tuerantuer.launcher.app.AppLauncher
import org.tuerantuer.launcher.app.CustomLauncherApps
import org.tuerantuer.launcher.app.CustomLauncherAppsImpl
import org.tuerantuer.launcher.app.UserManager
import org.tuerantuer.launcher.app.UserManagerImpl
import org.tuerantuer.launcher.data.database.favorites.FavoriteDao
import org.tuerantuer.launcher.data.datastore.SettingsManager
import org.tuerantuer.launcher.data.datastore.SettingsManagerImpl
import org.tuerantuer.launcher.ui.MainViewModel
import org.tuerantuer.launcher.ui.MainViewModelImpl
import org.tuerantuer.launcher.ui.motion.ScreenTransitionManager
import org.tuerantuer.launcher.util.FrameworkActionsManager
import org.tuerantuer.launcher.util.extension.dataStore
import javax.inject.Singleton

/**
 * This module contains singletons that are allowed to live for the whole lifecycle of the app.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope =
        CoroutineScope(context = SupervisorJob() + Dispatchers.Main)

    @Singleton
    @Provides
    fun provideUserManager(
        @ApplicationContext context: Context,
    ): UserManager = UserManagerImpl(context)

    @Singleton
    @Provides
    fun provideCustomLauncherApps(
        userManager: UserManager,
        @ApplicationContext context: Context,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): CustomLauncherApps = CustomLauncherAppsImpl(userManager, context, ioDispatcher)

    @Singleton
    @Provides
    fun provideScreenTransitionManager(): ScreenTransitionManager = ScreenTransitionManager()

    @Singleton
    @Provides
    fun provideAppActivityRepository(
        customLauncherApps: CustomLauncherApps,
        userManager: UserManager,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        coroutineScope: CoroutineScope,
        favoriteDao: FavoriteDao,
        @ApplicationContext context: Context,
    ): AppActivityRepository =
        AppActivityRepositoryImpl(customLauncherApps, userManager, coroutineScope, ioDispatcher, favoriteDao, context)

    @Singleton
    @Provides
    fun provideAppLauncher(
        @ApplicationContext context: Context,
    ): AppLauncher = AppLauncher(context)

    @Singleton
    @Provides
    fun provideSettingsManager(
        @ApplicationContext context: Context,
    ): SettingsManager = SettingsManagerImpl(context.dataStore)

    @Singleton
    @Provides
    fun provideDefaultLauncherChooser(
        @ApplicationContext context: Context,
    ): FrameworkActionsManager = FrameworkActionsManager(context)

    @Singleton
    @Provides
    fun provideMainViewModel(
        appActivityRepository: AppActivityRepository,
        appLauncher: AppLauncher,
        frameworkActionsManager: FrameworkActionsManager,
        settingsManager: SettingsManager,
        coroutineScope: CoroutineScope,
    ): MainViewModel = MainViewModelImpl(
        appActivityRepository = appActivityRepository,
        appLauncher = appLauncher,
        frameworkActionsManager = frameworkActionsManager,
        settingsManager = settingsManager,
        coroutineScope = coroutineScope,
    )
}
