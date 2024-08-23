package org.tuerantuer.launcher.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.ui.unit.dp

/**
 * Draws a circle.
 *
 * @param circleColor The color of the circle.
 * @param circleRatio The ratio of the circle's diameter to the width of the drawable. So, if the ratio is 1, the circle
 *  will have the same diameter as the width of the drawable. If smaller, the circle has padding.
 *
 * @since 2019-04-09
 */
open class CircleDrawable(
    private val context: Context,
    @ColorInt val circleColor: Int,
    @ColorInt val strokeColor: Int? = null,
    @FloatRange(0.0, 1.0) private val circleRatio: Float = CIRCLE_DIAMETER_PERCENTAGE_DEFAULT,
) : Drawable() {
    companion object {
        const val CIRCLE_DIAMETER_PERCENTAGE_DEFAULT = 44f / 48f
    }

    private val fillPaint = Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
        color = circleColor
    }

    private val strokePaint = if (strokeColor != null) {
        Paint(Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            strokeWidth = dpToPx(context, 1.5.dp.value)
            style = Paint.Style.STROKE
        }
    } else {
        null
    }

    private var circleRadius = 0f
    private var centerX = 0f
    private var centerY = 0f

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        val width = bounds.width()
        centerX = bounds.exactCenterX()
        centerY = bounds.exactCenterY()
        circleRadius = width * (circleRatio / 2f)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, circleRadius, fillPaint)
        strokePaint?.let { canvas.drawCircle(centerX, centerY, circleRadius, it) }
    }

    override fun setAlpha(alpha: Int) {
        fillPaint.alpha = alpha
        strokePaint?.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        fillPaint.colorFilter = colorFilter
        strokePaint?.colorFilter = colorFilter
        invalidateSelf()
    }

    override fun getAlpha() = fillPaint.alpha

    override fun getOpacity() = PixelFormat.TRANSLUCENT

    private fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}