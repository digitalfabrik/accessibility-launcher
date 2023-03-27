package org.tuerantuer.launcher.itemInfo

import android.os.UserHandle
import androidx.annotation.AnyThread

/**
 * Manages users and their profiles.
 *
 * Different users and their profiles allow for having multiple instances of the same app, which are then handled as 2
 * *separate* apps. Most users only have one profile (the default or "primary" one). A work profile is an example for a
 * secondary profile. Work profiles provide platform-level separation of work apps and data, giving organizations full
 * control of the data, apps, and security policies within a work profile.
 *
 * Note: Currently [UserManager] does not update its state when users are added or removed.
 */
interface UserManager {
    /**
     *  Default and usually the only user.
     */
    val primaryUser: UserHandle

    /**
     *  Id of [primaryUser].
     */
    val primaryUserSerial: Long

    /**
     * List of all profiles.
     */
    @get:AnyThread
    val userProfiles: List<UserHandle>

    /**
     * Returns true if more user profiles than just the profile of the [primaryUser] are installed.
     */
    @get:AnyThread
    val hasMoreThanOneUser: Boolean

    /**
     * @return whether the given user is the primary user
     */
    @AnyThread
    fun isPrimaryUser(user: UserHandle?): Boolean

    @AnyThread
    fun serializeUser(user: UserHandle?): Long

    @AnyThread
    fun deserializeUser(serialNumber: Long): UserHandle?
}
