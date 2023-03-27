package org.tuerantuer.launcher.util

import org.tuerantuer.launcher.util.extension.launchAppActivityInNewState

/**
 * Occurs when launching an activity through [launchAppActivityInNewState] fails.
 */
class ActivityLaunchFailedException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()
