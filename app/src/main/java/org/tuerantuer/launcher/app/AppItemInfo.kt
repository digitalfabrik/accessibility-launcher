package org.tuerantuer.launcher.app

import android.graphics.drawable.Drawable
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.app.appIdentifier.ComponentKeySer

/**
 * The metadata of an app activity.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
data class AppItemInfo(
    val name: String,
    val icon: Drawable?,
    val componentKey: ComponentKey,
    val componentKeySer: ComponentKeySer,
)
