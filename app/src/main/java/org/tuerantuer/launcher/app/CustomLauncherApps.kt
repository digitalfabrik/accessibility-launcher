package org.tuerantuer.launcher.app

import android.content.pm.LauncherActivityInfo
import android.graphics.Rect
import android.os.Bundle
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.app.appIdentifier.PackageUser
import org.tuerantuer.launcher.app.appIdentifier.PackageUserSer
import org.tuerantuer.launcher.util.ActivityLaunchFailedException

/**
 * Wrapper class for [android.content.pm.LauncherApps], which is Android's API for third-party launchers. It mainly lets
 * us query for app activities and shortcuts.
 *
 * @author Peter Huber
 * Created on 25/04/2022
 */
interface CustomLauncherApps {
    enum class AppChangeType {
        /**
         * The app was added or updated.
         */
        UPSERT,

        /**
         * The app was uninstalled.
         */
        REMOVE,
    }

    /**
     * This listener is called when the an app gets added, updated or removed.
     */
    interface AppChangeListener {
        fun onAppsChanged(packageUsers: Set<PackageUserSer>, appChangeType: AppChangeType)
    }

    fun addAppChangeListener(appChangeListener: AppChangeListener)

    fun removeAppChangeListener(appChangeListener: AppChangeListener)

    /**
     * This lets us start an activity of an app for any user profile.
     */
    fun launchActivityForProfile(componentKey: ComponentKey, sourceBounds: Rect?, opts: Bundle?)

    /**
     * Shows Android's app info screen for a particular app. From here, the user can uninstall the app, clear its data
     * or change its permission.
     *
     * @throws ActivityLaunchFailedException if the app info screen couldn't be opened.
     */
    @Throws(ActivityLaunchFailedException::class)
    fun openAppInfoScreen(packageUser: PackageUser, className: String?, sourceBounds: Rect?, opts: Bundle?)

    /**
     * Queries all the activities of installed apps of all users.
     */
    suspend fun queryAllAppActivities(): Collection<LauncherActivityInfo>

    /**
     * Queries the corresponding activity for the given [componentKey].
     *
     * @return null if no app provides an activity with the given [componentKey].
     */
    suspend fun queryActivity(componentKey: ComponentKey): LauncherActivityInfo?

    /**
     * Get activities for the specified package and user.
     */
    suspend fun queryActivityList(packageUser: PackageUser): Collection<LauncherActivityInfo>
}
