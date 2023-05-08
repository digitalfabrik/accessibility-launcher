package org.tuerantuer.launcher.app

import android.graphics.drawable.Drawable
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.app.appIdentifier.ComponentKeySer

/**
 * The metadata of an app activity. Two [AppItemInfo]s are considered to be the same if they have the same
 * [componentKey].
 */
data class AppItemInfo(
    val name: String,
    val icon: Drawable?,
    val componentKey: ComponentKey,
    val componentKeySer: ComponentKeySer,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AppItemInfo) return false

        if (componentKey != other.componentKey) return false

        return true
    }

    override fun hashCode(): Int {
        return componentKey.hashCode()
    }
}
