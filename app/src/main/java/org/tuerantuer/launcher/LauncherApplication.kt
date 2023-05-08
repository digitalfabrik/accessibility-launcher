package org.tuerantuer.launcher

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * The application class of the launcher app that is used to initialize the app.
 */
@HiltAndroidApp
class LauncherApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val inDebugMode = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        if (inDebugMode) {
            setStrictMode()
            Timber.plant(Timber.DebugTree())
        }
    }

    /**
     * Sets the strict mode for the app so that we can detect any potential issues during development.
     */
    private fun setStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitDiskReads()
                .permitDiskWrites()
                .penaltyLog()
                .penaltyDialog()
                .build(),
        )
        StrictMode.setVmPolicy(
            StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build(),
        )
    }
}
