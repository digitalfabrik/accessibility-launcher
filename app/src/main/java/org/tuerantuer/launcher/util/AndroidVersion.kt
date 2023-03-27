package org.tuerantuer.launcher.util

import android.os.Build

/**
 * Shortcuts to check if the user's device is running at a specific or newer Android version.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
object AndroidVersion {
    /**
     * Checks if the Android version is at least Android 8.1 Oreo (SDK version 27).
     */
    @JvmField
    val AT_LEAST_O_1 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

    /**
     * Checks if the Android version is at least Android 9.0 Pie (SDK version 28).
     */
    @JvmField
    val AT_LEAST_P = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    /**
     * Checks if the Android version is at least Android 10.0 Q (SDK version 29).
     */
    @JvmField
    val AT_LEAST_Q = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    /**
     * Checks if the Android version is at least Android 11.0 Red Velvet Cake (SDK version 30).
     */
    @JvmField
    val AT_LEAST_R = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

    /**
     * Checks if the Android version is at least Android 12.0 Snow Cone (SDK version 31).
     */
    @JvmField
    val AT_LEAST_S = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    /**
     * Checks if the Android version is at least Android 13.0 Tiramisu (SDK version 33).
     */
    @JvmField
    val AT_LEAST_T = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
}
