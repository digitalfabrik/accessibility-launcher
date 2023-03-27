package org.tuerantuer.launcher

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * TODO: add description
 *
 * @author Peter Huber
 * Created on 06/03/2023
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
