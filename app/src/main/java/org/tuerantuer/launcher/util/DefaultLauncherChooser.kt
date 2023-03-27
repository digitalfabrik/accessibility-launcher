package org.tuerantuer.launcher.util

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.Toast
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.util.extension.launchAppActivityInNewState

/**
 * To open the default launcher chooser.
 *
 * @author Peter Huber
 * Created on 07/03/2023
 */
class DefaultLauncherChooser(private val context: Context) {
    /**
     * Opens the default launcher chooser to let the user set this (or another) launcher as the default launcher.
     */
    fun openSetDefaultLauncherChooser(view: View?) {
        try {
            Intent(Settings.ACTION_HOME_SETTINGS).launchAppActivityInNewState(context, view)
        } catch (e: ActivityLaunchFailedException) {
            Toast.makeText(context, R.string.opening_set_default_launcher_screen_failed, Toast.LENGTH_SHORT).show()
        }
    }
}
