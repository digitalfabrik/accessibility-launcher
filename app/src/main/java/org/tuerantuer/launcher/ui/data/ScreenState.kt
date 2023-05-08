package org.tuerantuer.launcher.ui.data

import javax.annotation.concurrent.Immutable

/**
 * Each screen in the app has its own [ScreenState] class. Instances of this class hold the data
 * relevant for the corresponding screen.
 */
@Immutable
sealed class ScreenState {
    class OnboardingState(val onboardingPage: OnboardingPage) : ScreenState()

    object LoadingState : ScreenState()

    object HomeScreenState : ScreenState()

    object EditFavoritesScreenState : ScreenState()

    object AllAppsScreenState : ScreenState()

    class SettingsState(val settingsPage: SettingsPage) : ScreenState()
}
