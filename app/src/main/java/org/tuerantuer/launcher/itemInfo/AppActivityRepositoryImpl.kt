package org.tuerantuer.launcher.itemInfo

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tuerantuer.launcher.di.IoDispatcher
import org.tuerantuer.launcher.itemInfo.appIdentifier.ComponentKey
import org.tuerantuer.launcher.util.CustomInsetDrawable

/**
 * Default implementation of [AppActivityRepository].
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
class AppActivityRepositoryImpl(
    private val customLauncherApps: CustomLauncherApps,
    private val context: Context,
    private val userManager: UserManager,
    private val coroutineScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : AppActivityRepository {

    override val favorites: MutableStateFlow<List<AppItemInfo>> = MutableStateFlow(emptyList())
    override val allApps: MutableStateFlow<List<AppItemInfo>> = MutableStateFlow(emptyList())

    init {
        coroutineScope.launch {
            favorites.value = queryFavorites()
            allApps.value = queryAllApps()
        }
    }

    private suspend fun queryFavorites(): List<AppItemInfo> {
        return queryAllApps().take(5)
    }

    override suspend fun setFavorites(newFavorites: List<AppItemInfo>) {
        favorites.value = newFavorites
    }

    private suspend fun queryAllApps(): List<AppItemInfo> {
        return withContext(ioDispatcher) {
            customLauncherApps.queryAllAppActivities().map { activityInfo ->
                val label = activityInfo.label?.toString().orEmpty()
                val icon = getActivityInfoIcon(activityInfo, appIconDpi = 0)
                val componentKey = ComponentKey.fromLauncherActivityInfo(activityInfo)
                val key = Key(
                    componentKey.packageName,
                    componentKey.className,
                    userManager.serializeUser(componentKey.userHandle),
                )
                AppItemInfo(label, icon, componentKey, key)
            }
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
