package org.tuerantuer.launcher.app.appIdentifier

import android.os.UserHandle
import org.tuerantuer.launcher.app.UserManager

/**
 * The identifier of an app. If you have a serialized version of [userHandle], use [PackageUserSer].
 *
 * @param packageName identifies an app within a user profile.
 * @param userHandle identifies a user profile on the device. Usually only one user is registered on one device, see
 * [UserManager].
 *
 * @see PackageUserSer
 *
 * @author Peter Huber
 * Created on 04/10/2018
 */
data class PackageUser(val packageName: String, val userHandle: UserHandle?)
