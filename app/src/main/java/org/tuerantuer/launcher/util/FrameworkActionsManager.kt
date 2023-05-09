package org.tuerantuer.launcher.util

import androidx.annotation.RawRes
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey

/**
 * To interact with the Android framework, e.g. to open the settings, the default launcher chooser or to open a share
 * dialog.
 */
interface FrameworkActionsManager {
    /**
     * Opens the set launcher chooser to let the user set this (or another) launcher as the default launcher.
     */
    fun openSetDefaultLauncherChooser()

    /**
     * Opens a share dialog to let the user share this launcher to their friends.
     */
    fun shareLauncher()

    /**
     * Opens Android's system settings.
     * You can also use [openNotificationSettings], [openSoundSettings], [openDisplaySettings] or
     * [openAccessibilitySettings] to open a specific settings page.
     */
    fun openSystemSettings()

    /**
     * Opens Android's accessibility settings.
     */
    fun openAccessibilitySettings()

    /**
     * Opens Android's notification settings.
     */
    fun openNotificationSettings()

    /**
     * Opens Android's sound settings.
     */
    fun openSoundSettings()

    /**
     * Opens Android's display settings.
     */
    fun openDisplaySettings()

    /**
     * Opens the uninstall dialog for the app with the given [componentKey].
     */
    fun openAppUninstallDialog(componentKey: ComponentKey)

    /**
     * Allows the user to send us feedback via email.
     */
    fun sendFeedbackMail()

    /**
     * Sets the wallpaper of the users home and lock screen to the given [drawableRes].
     */
    suspend fun setWallpaper(@RawRes drawableRes: Int)
}