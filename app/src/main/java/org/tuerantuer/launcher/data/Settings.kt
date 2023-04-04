package org.tuerantuer.launcher.data

/**
 * All settings of the app that can be persisted in key value pairs. Only the favorites are settings that are not
 * persisted this way.
 *
 * @author Peter Huber
 * Created on 04/04/2023
 */
data class Settings(
    val appIconSize: AppIconSize = AppIconSize.Medium,
)
