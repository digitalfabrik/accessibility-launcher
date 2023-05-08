package org.tuerantuer.launcher.util

import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.appcompat.graphics.drawable.DrawableWrapperCompat
import kotlin.math.roundToInt

/**
 * This is a custom and very lightweight (but also unsafe) implementation of
 * [android.graphics.drawable.InsetDrawable]
 */
open class CustomInsetDrawable(
    drawable: Drawable,
    private val wrappedDrawableRatio: Float,
) : DrawableWrapperCompat(drawable) {

    private val mTmpRect = Rect()
    private var insets = 0

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        insets = (bounds.width() * ((1 - wrappedDrawableRatio) / 2f)).roundToInt()
        val r = mTmpRect
        r.set(bounds)

        r.left += insets
        r.top += insets
        r.right -= insets
        r.bottom -= insets

        // Apply inset bounds to the wrapped drawable.
        super.onBoundsChange(r)
    }

    override fun getPadding(padding: Rect): Boolean {
        super.getPadding(padding)
        padding.left += insets
        padding.top += insets
        padding.right += insets
        padding.bottom += insets
        return true
    }

    override fun getOpacity(): Int {
        @Suppress("DEPRECATION")
        val superOpacity = super.getOpacity()
        return if (superOpacity == PixelFormat.OPAQUE) PixelFormat.TRANSLUCENT else superOpacity
    }
}
