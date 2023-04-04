package org.tuerantuer.launcher.ui

import javax.annotation.concurrent.Immutable

/**
 * Each screen in the app has its own [ScreenState] class. Instances of this class hold the data
 * relevant for the corresponding screen.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
@Immutable
sealed class ScreenState {
    class Onboarding(val onboardingPage: OnboardingPage) : ScreenState()

    object LoadHomeScreen : ScreenState()

    object HomeScreen : ScreenState()

    object EditFavoritesScreen : ScreenState()

    object AllAppsScreen : ScreenState()

    object Settings : ScreenState()
}
