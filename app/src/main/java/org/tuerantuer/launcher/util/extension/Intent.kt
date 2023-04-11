package org.tuerantuer.launcher.util.extension

import android.content.Context
import android.content.Intent
import android.os.Bundle
import org.tuerantuer.launcher.util.ActivityLaunchFailedException
import org.tuerantuer.launcher.util.LaunchAnimationUtil

/**
 * Use this method to launch an activity that does not belong to our app. 'New state' means that we purposefully
 * recreate the activity we want to launch if its already open to reset its UI state. For example, if the want to
 * navigate the user to a certain screen inside Android's settings, this flag ensures that the user is being forwarded
 * to this screen instead of where they left off last time they opened the Settings app.
 * Moreover, when the user executes the back gesture on the launched activity, they immediately return to the
 * the last activity of our app instead of letting the third-party app of that activity decide what to open.
 *
 * @param view to request the receiver to start their activity launch animation from there. If null, we don't request
 * the receiver to start the animation from a specific location.
 * @throws ActivityLaunchFailedException if the activity couldn't be started, usually because the third party app is
 * not installed.
 */
@Throws(ActivityLaunchFailedException::class)
fun Intent.launchAppActivityInNewState(context: Context) {
    // Intent.FLAG_ACTIVITY_CLEAR_TASK purposefully recreates the activity we want to launch if its already open to
    // reset its UI state, see: https://stackoverflow.com/questions/21833402.
    val flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    addFlags(flags)
    val options = Bundle().apply { LaunchAnimationUtil.setShowSplashScreen(this) }
    @Suppress("TooGenericExceptionCaught")
    try {
        context.startActivity(this, options)
    } catch (e: Exception) {
        // Unfortunately, launching another app can induce various exceptions, so we have use the generic [Exception]
        // class for the catch block
        throw ActivityLaunchFailedException(cause = e)
    }
}
