package org.tuerantuer.launcher.util

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import org.tuerantuer.launcher.util.extension.half

/**
 * Utility class for running animations.
 *
 * @author Peter Huber
 * Created on 20/03/2023
 */
object AnimationUtil {

    /**
     * Hidden Android API to show the app's splash screen if the app does a cold start.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private const val KEY_SPLASH_SCREEN_STYLE = "android.activity.splashScreenStyle"

    /**
     * Hidden Android API to show the app's splash screen if the app does a cold start.
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private const val SPLASH_SCREEN_STYLE_ENABLED = 1

    /**
     * @return a bundle that overwrites the activity launch animation. The animation scales the launched activity up,
     * starting from the [view]'s bounds. Moreover, we request that the activity launched with the returned bundle shows
     * its splash screen (that's usually its app icon) on Android S and above.
     */
    fun createOpenFromViewAnimationBundle(view: View): Bundle {
        val bundle = makeSquareScaleUpBundle(view) ?: Bundle()
        setShowSplashScreen(bundle)
        return bundle
    }

    /**
     * Requests that the activity launched with [bundle] shows its splash screen (that's usually its app icon)
     * on Android S and above.
     */
    fun setShowSplashScreen(bundle: Bundle) {
        if (!AndroidVersion.AT_LEAST_S) return
        bundle.putInt(KEY_SPLASH_SCREEN_STYLE, SPLASH_SCREEN_STYLE_ENABLED)
    }

    /**
     * Creates a scale up animation that starts within view bounds with an aspect ratio of 1 (=square).
     * For consistent animations throughout the launcher, don't call this method directly. Instead, call
     * [createOpenFromViewAnimationBundle].
     */
    private fun makeSquareScaleUpBundle(view: View?): Bundle? {
        if (view == null) return null
        val width = view.measuredWidth
        val height = view.measuredHeight
        return if (width >= height) {
            // View too wide
            ActivityOptionsCompat.makeScaleUpAnimation(view, (width - height).half(), 0, height, height)
        } else {
            // View too high
            ActivityOptionsCompat.makeScaleUpAnimation(view, 0, (height - width).half(), width, width)
        }.toBundle()
    }
}
