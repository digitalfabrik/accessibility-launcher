package org.tuerantuer.launcher.util

import org.tuerantuer.launcher.util.extension.launchAppActivityInNewState

/**
 * Occurs when launching an activity through [launchAppActivityInNewState] fails.
 */
class ActivityLaunchFailedException(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause)
