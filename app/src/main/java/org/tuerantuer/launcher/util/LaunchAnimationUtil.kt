package org.tuerantuer.launcher.util

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi

/**
 * Utility class for controlling app launch animations.
 *
 * @author Peter Huber
 * Created on 20/03/2023
 */
object LaunchAnimationUtil {

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
     * Requests that the activity launched with [bundle] shows its splash screen (that's usually its app icon)
     * on Android S and above.
     */
    fun setShowSplashScreen(bundle: Bundle) {
        if (!AndroidVersion.AT_LEAST_S) return
        bundle.putInt(KEY_SPLASH_SCREEN_STYLE, SPLASH_SCREEN_STYLE_ENABLED)
    }
}
