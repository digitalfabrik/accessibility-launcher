package org.tuerantuer.launcher.app.appIdentifier

import android.content.ComponentName
import android.os.UserHandle
import org.tuerantuer.launcher.app.UserManager

/**
 * Serialized version of [ComponentKey]. The only difference between this class and [ComponentKey] is that the user
 * handle [userHandleSer] is serialized (a [Long] instead of a [UserHandle]). You can serialize and deserialize users
 * via [UserManager.serializeUser] and [UserManager.deserializeUser].
 *
 * @author Peter Huber
 * Created on 05/10/2018
 */
data class ComponentKeySer(val componentName: ComponentName, val userHandleSer: Long) {
    val packageUserSer: PackageUserSer
        get() = PackageUserSer(componentName.packageName, userHandleSer)
}
