package org.tuerantuer.launcher.itemInfo

import android.graphics.drawable.Drawable
import org.tuerantuer.launcher.itemInfo.appIdentifier.ComponentKey

/**
 * The metadata of an app activity.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
data class AppItemInfo(val name: String, val icon: Drawable?, val componentKey: ComponentKey, val key: Any) : ItemInfo
