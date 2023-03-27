package org.tuerantuer.launcher.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import org.tuerantuer.launcher.itemInfo.AppActivityRepository
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.itemInfo.AppLauncher
import org.tuerantuer.launcher.util.DefaultLauncherChooser
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
    private val coroutineScope: CoroutineScope,
    private val defaultLauncherChooser: DefaultLauncherChooser,
) : ViewModel() {

    /**
     * Determines the current state ui. Each subclass of [UiState] represents a different screen in
     * the app. You can subscribe to this [LiveData] to be notified when the underlying [UiState]
     * changes.
     */
    val uiStateLiveData: LiveData<UiState>
        get() = internalUiState

    private var uiState: UiState
        // we unfortunately need the !! operator, because LiveData is a Java class and doesn't
        // provide null-safety
        get() = internalUiState.value!!
        set(value) {
            internalUiState.value = value
        }

    private val internalUiState: MutableLiveData<UiState> =
        MutableLiveData(UiState.LoadHomeScreen)

    private var isActivityInForeground: Boolean = true

    init {
        loadHomeScreen()
    }

    fun onActivityStart() {
        isActivityInForeground = true
    }

    fun onActivityStop() {
        isActivityInForeground = false
    }

    //    /**
//     * @return true if pressing back event was handled and should not be propagated further.
//     */
    fun goBack(): Boolean {
        when (uiState) {
            is UiState.LoadHomeScreen -> return true
            is UiState.HomeScreen -> return true
            is UiState.Onboarding -> loadHomeScreen()
            is UiState.AllAppsScreen -> loadHomeScreen()
            is UiState.Settings -> loadHomeScreen()
            is UiState.EditFavoritesScreen -> loadHomeScreen()
//            is UiState.LoadHomeScreen, is UiState.MainMenu, UiState.Onboarding -> return false
//            is UiState.MatchOverview, is UiState.Settings -> loadMainMenu()
//            is UiState.AnswerQuestions -> this.uiState = UiState.MatchOverview(uiState.match)
//            is UiState.SelectRoundSubject -> this.uiState = UiState.MatchOverview(uiState.match)
//            is UiState.SetPlayer -> onCancelSetPlayerChange(uiState)
        }
        return true
    }

//    fun openSettings(uiState: UiState.MainMenu) {
//        this.uiState = UiState.Settings(uiState.player)
//    }

    fun onSetDefaultLauncher() {
        defaultLauncherChooser.openSetDefaultLauncherChooser(view = null)
    }

    private fun loadHomeScreen() {
        uiState = UiState.HomeScreen(favorites = appActivityRepository.favorites.value)
    }

    /**
     * Returns the MainMenu if the player is created, else returns the Onboarding UI State.
     */
    private suspend fun getMainUiState(onlyLoadFromCache: Boolean = false): UiState {
        return UiState.Onboarding
    }

    fun openApp(appItemInfo: AppItemInfo) {
        appLauncher.launchApp(appItemInfo)
    }

    fun onEditFavorites() {
        uiState = UiState.EditFavoritesScreen(favorites = appActivityRepository.favorites.value)
    }

    fun onShowAllApps() {
        uiState = UiState.AllAppsScreen(allApps = appActivityRepository.allApps.value)
    }

    fun onOpenSettings() {
        uiState = UiState.Settings
    }

    fun onOpenOnboarding() {
        uiState = UiState.Onboarding
    }
}
