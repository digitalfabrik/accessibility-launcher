package org.tuerantuer.launcher.util.extension

import android.graphics.Rect
import android.view.View

/**
 * The bounds of the view on the device's screen.
 */
val View.screenBounds: Rect
    get() {
        val pos = IntArray(2)
        getLocationOnScreen(pos)
        return Rect(pos[0], pos[1], pos[0] + width, pos[1] + height)
    }
