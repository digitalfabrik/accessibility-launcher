package org.tuerantuer.launcher.app.appIdentifier

import android.content.ComponentName
import android.content.pm.LauncherActivityInfo
import android.os.UserHandle
import org.tuerantuer.launcher.app.UserManager

/**
 * The identifier of an app activity that can be launched.
 *
 * @param componentName identifies an activity within the same user profile.
 * @param userHandle identifies a user profile on the device. Usually only one user is registered on one device, see
 * [UserManager].
 */
data class ComponentKey(val componentName: ComponentName, val userHandle: UserHandle?) {

    companion object {
        fun fromLauncherActivityInfo(launcherActivityInfo: LauncherActivityInfo): ComponentKey {
            return ComponentKey(launcherActivityInfo.componentName, launcherActivityInfo.user)
        }
    }

    constructor(packageName: String, className: String, userHandle: UserHandle?) :
            this(ComponentName(packageName, className), userHandle)

    constructor(packageUser: PackageUser, className: String) :
            this(ComponentName(packageUser.packageName, className), packageUser.userHandle)

    /**
     * Identifies an app within a user profile.
     */
    val packageName: String
        get() = componentName.packageName

    /**
     * Identifies an activity within the same app, within a user profile.
     */
    val className: String
        get() = componentName.className

    val packageUser: PackageUser
        get() = PackageUser(packageName, userHandle)
}
