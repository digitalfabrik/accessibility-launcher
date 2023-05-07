package org.tuerantuer.launcher.data.datastore

/**
 * All settings of the app that can be persisted in key value pairs. Only the favorites are settings that are not
 * persisted this way.
 *
 * @author Peter Huber
 * Created on 04/04/2023
 */
data class Settings(
    /**
     * @see AppIconSize
     */
    val appIconSize: AppIconSize = AppIconSize.M,

    /**
     * Whether the user has tapped through all the onboarding screens and therefore accepted the terms and conditions.
     */
    val isUserOnboarded: Boolean = true,

    /**
     * @see WallpaperType
     */
    val wallpaperType: WallpaperType = WallpaperType.SOLID_COLOR,
)
