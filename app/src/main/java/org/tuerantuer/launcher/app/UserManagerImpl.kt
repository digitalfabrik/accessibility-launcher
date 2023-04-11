package org.tuerantuer.launcher.app

import android.content.Context
import android.os.Process
import android.os.UserHandle
import android.util.ArrayMap
import androidx.collection.LongSparseArray
import androidx.core.content.getSystemService
import android.os.UserManager as AndroidUserManager

/**
 * Default implementation of [UserManager].
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
class UserManagerImpl(
    context: Context,
) : UserManager {

    @Suppress("MemberNameEqualsClassName")
    private val androidUserManager: AndroidUserManager = context.getSystemService()!!
    override val primaryUser: UserHandle = Process.myUserHandle()
    override val primaryUserSerial: Long = androidUserManager.getSerialNumberForUser(primaryUser)

    private lateinit var users: LongSparseArray<UserHandle>

    // Create a separate reverse map as LongArrayMap.indexOfValue checks if objects are same
    // and not {@link Object#equals}
    private lateinit var userToSerialMap: ArrayMap<UserHandle, Long>

    override val hasMoreThanOneUser: Boolean
        get() = synchronized(this) { userToSerialMap.size > 1 }

    override val userProfiles: List<UserHandle>
        get() {
            return synchronized(this) {
                ArrayList(userToSerialMap.keys)
            }
        }

    init {
        enableAndResetCache()
    }

    override fun isPrimaryUser(user: UserHandle?): Boolean = user == primaryUser

    override fun serializeUser(user: UserHandle?): Long {
        if (isPrimaryUser(user)) return primaryUserSerial
        if (user == null) return -1
        return synchronized(this) { userToSerialMap[user] ?: -1 }
    }

    override fun deserializeUser(serialNumber: Long): UserHandle? {
        if (serialNumber == primaryUserSerial) return primaryUser
        return synchronized(this) { users.get(serialNumber) }
    }

    private fun enableAndResetCache() {
        synchronized(this) {
            users = LongSparseArray()
            userToSerialMap = ArrayMap()
            androidUserManager.userProfiles?.forEach { user ->
                val serialNumber = androidUserManager.getSerialNumberForUser(user)
                users.put(serialNumber, user)
                userToSerialMap[user] = serialNumber
            }
        }
    }
}
