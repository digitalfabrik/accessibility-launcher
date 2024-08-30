package org.tuerantuer.launcher.util

import android.app.WallpaperManager
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.tuerantuer.launcher.BuildConfig
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.di.IoDispatcher
import org.tuerantuer.launcher.util.extension.launchAppActivityInNewState
import java.io.IOException


/**
 * Default implementation of [FrameworkActionsManager].
 */
class FrameworkActionsManagerImpl(
    private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FrameworkActionsManager {

    override fun openSetDefaultLauncherChooser() {
        try {
            Intent(Settings.ACTION_HOME_SETTINGS).launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.opening_set_default_launcher_screen_failed)
        }
    }

    override fun shareLauncher() {
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

    override fun openSystemSettings() {
        launchIntentAndHandleFailure(Intent(Settings.ACTION_SETTINGS))
    }

    override fun openAccessibilitySettings() {
        launchIntentAndHandleFailure(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    override fun openSoundSettings() {
        launchIntentAndHandleFailure(Intent(Settings.ACTION_SOUND_SETTINGS))
    }

    override fun openApplicationSettings() {
        launchIntentAndHandleFailure(Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS))
    }

    override fun openDisplaySettings() {
        launchIntentAndHandleFailure(Intent(Settings.ACTION_DISPLAY_SETTINGS))
    }

    private fun launchIntentAndHandleFailure(intent: Intent) {
        try {
            intent.launchAppActivityInNewState(context)
        } catch (e: ActivityLaunchFailedException) {
            showLongToast(R.string.action_not_supported)
        }
    }

    private fun showLongToast(messageRes: Int) {
        Toast.makeText(context, messageRes, Toast.LENGTH_LONG).show()
    }

    override fun openAppUninstallDialog(componentKey: ComponentKey) {
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

    override fun sendFeedbackMail() {
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

    override suspend fun setWallpaper(@RawRes drawableRes: Int) {
        try {
            withContext(ioDispatcher) {
                val wallpaperManager = WallpaperManager.getInstance(context)
                wallpaperManager.setResource(drawableRes)
            }
        } catch (e: IOException) {
            showLongToast(R.string.action_not_supported)
        }
    }

    /**
     * Generates a URI from a drawable resource ID.
     * @param context - context
     * @param drawableId - drawable res id
     * @return - uri
     */
    fun getUriFromDrawable(
        context: Context,
        @DrawableRes drawableId: Int,
    ): Uri {
        return Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + context.resources.getResourcePackageName(drawableId)
                    + '/' + context.resources.getResourceTypeName(drawableId)
                    + '/' + context.resources.getResourceEntryName(drawableId),
        )
    }
}
