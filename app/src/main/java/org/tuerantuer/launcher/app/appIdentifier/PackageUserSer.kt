package org.tuerantuer.launcher.app.appIdentifier

import android.os.UserHandle
import org.tuerantuer.launcher.app.UserManager

/**
 * The identifier of an app, as [PackageUser]. The only difference between this class and [PackageUser] is that in this
 * class the user is serialized ([Long]) and not a [UserHandle]. You can serialize and deserialize users via
 * [UserManager.serializeUser] and [UserManager.deserializeUser].
 *
 * @param packageName           identifies an app within a user profile.
 * @param userHandleSerialized  identifies a user profile on the device. Usually only one user is registered on one
 * device, see [UserManager].
 *
 * @see PackageUser
 */
data class PackageUserSer(val packageName: String, val userHandleSerialized: Long)
