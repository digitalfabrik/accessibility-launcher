package org.tuerantuer.launcher.app

import android.content.ComponentName
import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.graphics.Rect
import android.os.Bundle
import android.os.Process
import android.os.UserHandle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.tuerantuer.launcher.BuildConfig
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.app.appIdentifier.PackageUser
import org.tuerantuer.launcher.app.appIdentifier.PackageUserSer
import org.tuerantuer.launcher.di.IoDispatcher
import org.tuerantuer.launcher.util.ActivityLaunchFailedException
import org.tuerantuer.launcher.util.extension.isSingle
import timber.log.Timber
import java.util.Collections

/**
 * Default implementation of [CustomLauncherApps].
 *
 * @author Peter Huber
 * Created on 24/05/2017
 */
class CustomLauncherAppsImpl(
    private val userManager: UserManager,
    context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : LauncherApps.Callback(), CustomLauncherApps {

    private val launcherApps: LauncherApps =
        context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps

    private val appChangeListeners: MutableList<CustomLauncherApps.AppChangeListener> = mutableListOf()

    init {
        launcherApps.registerCallback(this)
    }

    override fun addAppChangeListener(appChangeListener: CustomLauncherApps.AppChangeListener) {
        this.appChangeListeners.add(appChangeListener)
    }

    override fun removeAppChangeListener(appChangeListener: CustomLauncherApps.AppChangeListener) {
        this.appChangeListeners.remove(appChangeListener)
    }

    override fun launchActivityForProfile(componentKey: ComponentKey, sourceBounds: Rect?, opts: Bundle?) {
        launcherApps.startMainActivity(componentKey.componentName, componentKey.userHandle, sourceBounds, opts)
    }

    override fun openAppInfoScreen(packageUser: PackageUser, className: String?, sourceBounds: Rect?, opts: Bundle?) {
        // The class name seems to be optional
        val componentName = ComponentName(packageUser.packageName, className.orEmpty())
        try {
            launcherApps.startAppDetailsActivity(
                componentName,
                packageUser.userHandle,
                sourceBounds,
                opts,
            )
        } catch (e: Exception) {
            // most of the times this will be an ActivityNotFoundException but unfortunately, various other exceptions
            // can also be thrown.
            throw ActivityLaunchFailedException(cause = e)
        }
    }

    override suspend fun queryAllAppActivities(): List<LauncherActivityInfo> {
        val userHandleList = userManager.userProfiles
        return if (userHandleList.isSingle()) {
            // optimization for common case
            getActivityList(packageName = null, userHandleList.single())
        } else {
            userHandleList.flatMap { userHandle -> getActivityList(packageName = null, userHandle) }
        }
    }

    override suspend fun queryActivityList(packageUser: PackageUser): Collection<LauncherActivityInfo> {
        return getActivityList(packageUser.packageName, packageUser.userHandle)
    }

    override suspend fun queryActivity(componentKey: ComponentKey): LauncherActivityInfo? {
        val componentName = componentKey.componentName
        return getActivityList(componentName.packageName, componentKey.userHandle)
            .find { it.componentName == componentName }
    }

    private suspend fun getActivityList(packageName: String?, userHandle: UserHandle?): List<LauncherActivityInfo> {
        return withContext(ioDispatcher) {
            when {
                userHandle != null -> launcherApps.getActivityList(packageName, userHandle)
                else -> emptyList()
            }
        }
    }

    private fun onAppsChanged(packageUser: PackageUserSer, appChangeType: CustomLauncherApps.AppChangeType) {
        onAppsChanged(Collections.singleton(packageUser), appChangeType)
    }

    private fun onAppsChanged(
        packageNameArray: Array<String>,
        userHandle: UserHandle,
        appChangeType: CustomLauncherApps.AppChangeType,
    ) {
        val userSerialized = userManager.serializeUser(userHandle)
        val packageUserList = if (packageNameArray.size == 1) {
            Collections.singleton(PackageUserSer(packageNameArray.single(), userSerialized))
        } else {
            mutableSetOf<PackageUserSer>().apply {
                packageNameArray.forEach { packageName -> add(PackageUserSer(packageName, userSerialized)) }
            }
        }
        onAppsChanged(packageUserList, appChangeType)
    }

    private fun onAppsChanged(
        packageUsers: Set<PackageUserSer>,
        appChangeType: CustomLauncherApps.AppChangeType,
    ) {
        appChangeListeners.forEach { listener ->
            listener.onAppsChanged(packageUsers, appChangeType)
        }
    }

    override fun onPackageAdded(packageName: String, user: UserHandle) {
        Timber.d("onPackageAdded: $packageName user: $user isDefaultUser: ${user == Process.myUserHandle()}")
        onAppsChanged(
            PackageUserSer(packageName, userManager.serializeUser(user)),
            CustomLauncherApps.AppChangeType.UPSERT,
        )
    }

    override fun onPackageChanged(packageName: String, user: UserHandle) {
        if (BuildConfig.DEBUG && packageName == BuildConfig.APPLICATION_ID) return
        Timber.d("onPackageChanged: $packageName")
        onAppsChanged(
            PackageUserSer(packageName, userManager.serializeUser(user)),
            CustomLauncherApps.AppChangeType.UPSERT,
        )
    }

    override fun onPackageRemoved(packageName: String, user: UserHandle) {
        Timber.d("onPackageRemoved: $packageName")
        onAppsChanged(
            PackageUserSer(packageName, userManager.serializeUser(user)),
            CustomLauncherApps.AppChangeType.REMOVE,
        )
    }

    override fun onPackagesAvailable(packageNames: Array<String>, user: UserHandle, replacing: Boolean) {
        Timber.d("onPackagesAvailable: ${packageNames.contentToString()}")
        onAppsChanged(packageNames, user, CustomLauncherApps.AppChangeType.UPSERT)
    }

    override fun onPackagesUnavailable(packageNames: Array<String>, user: UserHandle, replacing: Boolean) {
        Timber.d("onPackagesUnavailable: ${packageNames.contentToString()}")
    }

    override fun onPackagesSuspended(packageNames: Array<String>, user: UserHandle) {
        super.onPackagesSuspended(packageNames, user)
        Timber.d("onPackagesSuspended: ${packageNames.contentToString()}")
        onAppsChanged(packageNames, user, CustomLauncherApps.AppChangeType.UPSERT)
    }

    override fun onPackagesUnsuspended(packageNames: Array<String>, user: UserHandle) {
        super.onPackagesUnsuspended(packageNames, user)
        Timber.d("onPackagesUnsuspended ${packageNames.contentToString()}")
        onAppsChanged(packageNames, user, CustomLauncherApps.AppChangeType.UPSERT)
    }
}
