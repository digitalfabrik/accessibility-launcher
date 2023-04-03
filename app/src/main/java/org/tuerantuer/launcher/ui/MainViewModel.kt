package org.tuerantuer.launcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.tuerantuer.launcher.itemInfo.AppActivityRepository
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.itemInfo.AppLauncher
import org.tuerantuer.launcher.util.DefaultLauncherChooser
import org.tuerantuer.launcher.util.WhileUiSubscribed
import javax.inject.Inject

/**
 * View Model of [MainActivity].
 *
 * @author Peter Huber
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val appActivityRepository: AppActivityRepository,
    private val appLauncher: AppLauncher,
    private val defaultLauncherChooser: DefaultLauncherChooser,
) : ViewModel() {

    private val screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.HomeScreen)

    /**
     * Determines the current state ui. Each subclass of [ScreenState] represents a different screen in
     * the app. You can subscribe to this [Flow] to be notified when the underlying [ScreenState]
     * changes.
     */
    val uiState: StateFlow<UiState> = combine(
        appActivityRepository.allApps,
        appActivityRepository.favorites,
        screenState,
    ) { allApps, favorites, screenState ->
        UiState(screenState = screenState, favorites = favorites, allApps = allApps)
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = UiState(screenState = screenState.value, allApps = emptyList(), favorites = emptyList()),
    )

    private var isActivityInForeground: Boolean = true

    fun onActivityStart() {
        isActivityInForeground = true
    }

    fun onActivityStop() {
        isActivityInForeground = false
    }

    /**
     * @return true if pressing back event was handled and should not be propagated further.
     */
    fun goBack(): Boolean {
        when (screenState.value) {
            is ScreenState.LoadHomeScreen -> return true
            is ScreenState.HomeScreen -> return true
            is ScreenState.Onboarding -> loadHomeScreen()
            is ScreenState.AllAppsScreen -> loadHomeScreen()
            is ScreenState.Settings -> loadHomeScreen()
            is ScreenState.EditFavoritesScreen -> loadHomeScreen()
            // is ScreenState.LoadHomeScreen, is ScreenState.MainMenu, ScreenState.Onboarding -> return false
            // is ScreenState.MatchOverview, is ScreenState.Settings -> loadMainMenu()
            // is ScreenState.AnswerQuestions -> this.uiState = ScreenState.MatchOverview(uiState.match)
            // is ScreenState.SelectRoundSubject -> this.uiState = ScreenState.MatchOverview(uiState.match)
            // is ScreenState.SetPlayer -> onCancelSetPlayerChange(uiState)
        }
        return true
    }

    fun onSetDefaultLauncher() {
        defaultLauncherChooser.openSetDefaultLauncherChooser(view = null)
    }

    private fun loadHomeScreen() {
        screenState.value = ScreenState.HomeScreen
    }

    fun openApp(appItemInfo: AppItemInfo) {
        appLauncher.launchApp(appItemInfo)
    }

    fun onEditFavorites() {
        screenState.value = ScreenState.EditFavoritesScreen
    }

    fun onShowAllApps() {
        screenState.value = ScreenState.AllAppsScreen
    }

    fun onOpenSettings() {
        screenState.value = ScreenState.Settings
    }

    fun onOpenOnboarding() {
        screenState.value = ScreenState.Onboarding
    }

    suspend fun onSetFavorites(newFavorites: List<AppItemInfo>) {
        appActivityRepository.setFavorites(newFavorites)
        screenState.value = ScreenState.HomeScreen
    }
}
