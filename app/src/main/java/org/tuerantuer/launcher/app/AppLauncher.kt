package org.tuerantuer.launcher.app

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.tuerantuer.launcher.util.AndroidVersion

/**
 * This class is responsible for launching apps.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
class AppLauncher(
    private val context: Context,
) {
    companion object {
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
    }

    fun launchApp(appItemInfo: AppItemInfo) {
        // Note: This method only supports apps from the main user profile (and not work profiles).
        val launchIntent = makeLaunchIntent(appItemInfo)
        val options = createAnimateFromViewWithSplashscreenBundle()
        ContextCompat.startActivity(context, launchIntent, options)
    }

    private fun createAnimateFromViewWithSplashscreenBundle(): Bundle {
        val bundle = Bundle()
        if (!AndroidVersion.AT_LEAST_S) return bundle
        bundle.putInt(KEY_SPLASH_SCREEN_STYLE, SPLASH_SCREEN_STYLE_ENABLED)
        return bundle
    }

    /**
     * Creates an intent for launching an app.
     */
    private fun makeLaunchIntent(appItemInfo: AppItemInfo) = Intent(Intent.ACTION_MAIN)
        .addCategory(Intent.CATEGORY_LAUNCHER)
        .setComponent(appItemInfo.componentKey.componentName)
        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
}
