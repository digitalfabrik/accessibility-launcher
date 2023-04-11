package org.tuerantuer.launcher.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import org.tuerantuer.launcher.BuildConfig
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.util.extension.launchAppActivityInNewState

/**
 * To interact with the Android framework, e.g. to open the default launcher chooser or to open a share dialog.
 *
 * @author Peter Huber
 * Created on 07/03/2023
 */
class FrameworkActionsManager(private val context: Context) {
    /**
     * Opens the default launcher chooser to let the user set this (or another) launcher as the default launcher.
     */
    fun openSetDefaultLauncherChooser() {
        try {
            Intent(Settings.ACTION_HOME_SETTINGS).launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.opening_set_default_launcher_screen_failed)
        }
    }

    fun shareLauncher() {
        val chooserTitle = context.getString(R.string.share_launcher)
        val link = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, chooserTitle)
                putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_launcher_text, link))
            }
            Intent.createChooser(intent, chooserTitle).launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.action_not_supported)
        }
    }

    fun openSystemSettings() {
        try {
            Intent(Settings.ACTION_SETTINGS).launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.action_not_supported)
        }
    }

    fun openAccessibilitySettings() {
        try {
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS).launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.action_not_supported)
        }
    }

    private fun showLongToast(messageRes: Int) {
        Toast.makeText(context, messageRes, Toast.LENGTH_LONG).show()
    }

    fun openAppUninstallDialog(componentKey: ComponentKey) {
        val packageUri = Uri.parse("package:" + componentKey.packageName)
        try {
            context.startActivity(
                Intent(Intent.ACTION_DELETE, packageUri)
                    .setData(
                        Uri.fromParts(
                            "package",
                            componentKey.packageName,
                            componentKey.className,
                        ),
                    )
                    .putExtra(Intent.EXTRA_USER, componentKey.userHandle)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        } catch (e: ActivityNotFoundException) {
            try {
                context.startActivity(
                    Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                )
            } catch (e2: ActivityNotFoundException) {
                showLongToast(R.string.action_not_supported)
            }
        }
    }

    fun sendFeedbackMail() {
        sendMail(
            subject = context.getString(R.string.feedback_mail_subject),
            body = context.getString(R.string.feedback_mail_body),
        )
    }

    /**
     * Lets the user send a prewritten email to us.
     *
     * @param subject   the subject of the email.
     * @param body      the body of the email.
     */
    private fun sendMail(subject: String, body: CharSequence) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            val supportEmailAddress = context.getString(R.string.support_mail_address)
            // Data makes sure only email apps should handle this. For some reason the intent breaks for Gmail if we
            // include our email address in the mailto URI.
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmailAddress))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        try {
            intent.launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.action_not_supported)
        }
    }
}
