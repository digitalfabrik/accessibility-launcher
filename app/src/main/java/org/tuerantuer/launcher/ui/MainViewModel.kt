package org.tuerantuer.launcher.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.tuerantuer.launcher.data.AppIconSize
import org.tuerantuer.launcher.data.SettingsManager
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
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _screenState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.HomeScreenState)
    private var screenState: ScreenState
        get() = _screenState.value
        set(value) {
            _screenState.value = value
        }

    /**
     * Determines the current state ui. Each subclass of [ScreenState] represents a different screen in
     * the app. You can subscribe to this [Flow] to be notified when the underlying [ScreenState]
     * changes.
     */
    val uiState: StateFlow<UiState> = combine(
        appActivityRepository.apps,
        _screenState,
        settingsManager.settings,
    ) { apps, screenState, settings ->
        UiState(screenState = screenState, favorites = apps.favorites, allApps = apps.allApps, settings = settings)
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = UiState(screenState = screenState, allApps = emptyList(), favorites = emptyList()),
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
        when (screenState) {
            is ScreenState.LoadHomeScreenState -> return true
            is ScreenState.HomeScreenState -> return true
            is ScreenState.OnboardingState -> onGoToPreviousOnboardingStep()
            is ScreenState.AllAppsScreenState -> loadHomeScreen()
            is ScreenState.SettingsState -> loadHomeScreen()
            is ScreenState.EditFavoritesScreenState -> loadHomeScreen()
            // is ScreenState.LoadHomeScreenState, is ScreenState.MainMenu, ScreenState.OnboardingState -> return false
            // is ScreenState.MatchOverview, is ScreenState.SettingsState -> loadMainMenu()
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
        screenState = ScreenState.HomeScreenState
    }

    fun openApp(appItemInfo: AppItemInfo) {
        appLauncher.launchApp(appItemInfo)
    }

    fun onEditFavorites() {
        screenState = ScreenState.EditFavoritesScreenState
    }

    fun onShowAllApps() {
        screenState = ScreenState.AllAppsScreenState
    }

    fun onOpenSettings() {
        screenState = ScreenState.SettingsState
    }

    fun onOpenOnboarding() {
        screenState = ScreenState.OnboardingState(OnboardingPage.SCREEN_1)
    }

    suspend fun onSetIconSize(appIconSize: AppIconSize) {
        settingsManager.setAppIconSize(appIconSize)
    }

    fun onGoToNextOnboardingStep() {
        takeOnboardingStep(1)
    }

    private fun onGoToPreviousOnboardingStep() {
        takeOnboardingStep(-1)
    }

    private fun takeOnboardingStep(stepSize: Int) {
        val screenState = screenState
        require(screenState is ScreenState.OnboardingState)
        val nextStep = screenState.onboardingPage.pageNumber + stepSize
        if (nextStep < OnboardingPage.SCREEN_1.pageNumber || nextStep > OnboardingPage.LAST_PAGE.pageNumber) {
            cancelOnboarding()
        } else {
            this.screenState = ScreenState.OnboardingState(OnboardingPage.values().first { it.pageNumber == nextStep })
        }
    }

    fun cancelOnboarding() {
        loadHomeScreen()
    }

    suspend fun onSetFavorites(newFavorites: List<AppItemInfo>) {
        appActivityRepository.setFavorites(newFavorites)
        screenState = ScreenState.HomeScreenState
    }
}
