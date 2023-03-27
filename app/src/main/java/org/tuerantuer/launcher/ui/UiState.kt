package org.tuerantuer.launcher.ui

import org.tuerantuer.launcher.itemInfo.AppItemInfo
import javax.annotation.concurrent.Immutable

/**
 * Each screen in the app has its own [UiState] class. Instances of this class hold the data
 * relevant for the corresponding screen.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
@Immutable
sealed class UiState {
    object Onboarding : UiState()

    object LoadHomeScreen : UiState()

    data class HomeScreen(val favorites: List<AppItemInfo>) : UiState()

    data class EditFavoritesScreen(val favorites: List<AppItemInfo>) : UiState()

    data class AllAppsScreen(val allApps: List<AppItemInfo>) : UiState()

    object Settings : UiState()
}
