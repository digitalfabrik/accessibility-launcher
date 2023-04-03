package org.tuerantuer.launcher.itemInfo

import android.content.pm.LauncherActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tuerantuer.launcher.data.FavoriteDao
import org.tuerantuer.launcher.data.FavoriteEntity
import org.tuerantuer.launcher.di.IoDispatcher
import org.tuerantuer.launcher.itemInfo.appIdentifier.ComponentKey
import org.tuerantuer.launcher.itemInfo.appIdentifier.PackageUserSer
import org.tuerantuer.launcher.util.CustomInsetDrawable

/**
 * Default implementation of [AppActivityRepository].
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
class AppActivityRepositoryImpl(
    private val customLauncherApps: CustomLauncherApps,
    private val userManager: UserManager,
    private val coroutineScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val favoriteDao: FavoriteDao,
) : AppActivityRepository, CustomLauncherApps.AppChangeListener {

    override val allApps: MutableSharedFlow<List<AppItemInfo>> = MutableSharedFlow()
    override val favorites: Flow<List<AppItemInfo>> =
        combine(allApps, favoriteDao.observeAll()) { allApps, favoriteEntities ->
            buildFavorites(allApps, favoriteEntities)
        }

    private var updateAppsJob: Job? = null

    init {
        customLauncherApps.addAppChangeListener(appChangeListener = this)
        setAllAppsAsync()
    }

    override fun onAppsChanged(packageUsers: Set<PackageUserSer>, appChangeType: CustomLauncherApps.AppChangeType) {
        setAllAppsAsync()
    }

    private fun setAllAppsAsync() {
        updateAppsJob?.cancel()
        updateAppsJob = coroutineScope.launch {
            allApps.emit(queryAllApps())
        }
    }

    private suspend fun buildFavorites(
        allApps: List<AppItemInfo>,
        favoriteEntities: List<FavoriteEntity>,
    ): List<AppItemInfo> {
        return withContext(ioDispatcher) {
            favoriteEntities.mapNotNull { favoriteEntity ->
                val componentKey = ComponentKey(
                    favoriteEntity.packageName,
                    favoriteEntity.className,
                    userManager.deserializeUser(favoriteEntity.user),
                )
                allApps.find { it.componentKey == componentKey }
            }
        }
    }

    override suspend fun setFavorites(newFavorites: List<AppItemInfo>) {
        val newFavoriteEntities = newFavorites.mapIndexed { index, appItemInfo ->
            val componentKey = appItemInfo.componentKey
            FavoriteEntity(
                packageName = componentKey.packageName,
                className = componentKey.className,
                user = userManager.serializeUser(componentKey.userHandle),
                favoritesIndex = index,
            )
        }
        favoriteDao.replaceAllFavorites(newFavoriteEntities)
    }

    private suspend fun queryAllApps(): List<AppItemInfo> {
        return withContext(ioDispatcher) {
            val result = mutableListOf<AppItemInfo>()
            customLauncherApps.queryAllAppActivities().mapTo(result) { activityInfo ->
                val name = activityInfo.label?.toString().orEmpty()
                val icon = getActivityInfoIcon(activityInfo, appIconDpi = 0)
                val componentKey = ComponentKey.fromLauncherActivityInfo(activityInfo)
                val key = Key(
                    componentKey.packageName,
                    componentKey.className,
                    userManager.serializeUser(componentKey.userHandle),
                )
                AppItemInfo(name, icon, componentKey, key)
            }
            result.sortBy { it.name }
            result
        }
    }

    private fun getActivityInfoIcon(activityInfo: LauncherActivityInfo, appIconDpi: Int): Drawable? {
        @Suppress("SwallowedException", "TooGenericExceptionCaught")
        val originalIcon = try {
            activityInfo.getIcon(appIconDpi)
        } catch (e: SecurityException) {
            // On some devices (notably Samsung but others too) above call can throw a SecurityException
            // when internally querying the ApplicationInfo.
            // We just ignore this crash since it seems to be a vendor bug.
            null
        } catch (e: Resources.NotFoundException) {
            null
        } catch (e: IndexOutOfBoundsException) {
            // This occurs very rarely on some peculiar devices; ignore.
            null
        } ?: return null

        return if (originalIcon is AdaptiveIconDrawable) {
            originalIcon
        } else {
            run {
                AdaptiveIconDrawable(
                    ColorDrawable(Color.WHITE),
                    CustomInsetDrawable(originalIcon, wrappedDrawableRatio = 0.5f),
                )
            }
        }
    }
}
