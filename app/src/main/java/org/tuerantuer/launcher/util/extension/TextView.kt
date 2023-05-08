package org.tuerantuer.launcher.util.extension

import android.widget.TextView
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.toArgb

/**
 * To set the shadow of a legacy [TextView] with a [Shadow] from Compose.
 */
fun TextView.setShadow(shadow: Shadow) {
    val shadowOffset = shadow.offset
    setShadowLayer(shadow.blurRadius, shadowOffset.x, shadowOffset.y, shadow.color.toArgb())
}