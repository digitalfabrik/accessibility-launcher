package org.tuerantuer.launcher.ui

import android.os.Process
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.BuildConfig
import org.tuerantuer.launcher.data.AppIconSize
import org.tuerantuer.launcher.data.SettingsManager
import org.tuerantuer.launcher.itemInfo.AppActivityRepository
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.itemInfo.AppLauncher
import org.tuerantuer.launcher.itemInfo.Apps
import org.tuerantuer.launcher.itemInfo.appIdentifier.ComponentKey
import org.tuerantuer.launcher.util.FrameworkActionsManager
import org.tuerantuer.launcher.util.WhileUiSubscribed

/**
 * View Model of [MainActivity]. This class is not an extension of [ViewModel] because the [ViewModel]s lifecycle is
 * broken with launchers (doing the home gesture the first time recreates the view model). Since our [MainActivity]
 * lives as long as the launcher is running if the launcher is set as default, it's okay that our [MainViewModel] lives
 * as long as the launcher is running.
 *
 * @author Peter Huber
 */
class MainViewModel(
    private val appActivityRepository: AppActivityRepository,
    private val appLauncher: AppLauncher,
    private val frameworkActionsManager: FrameworkActionsManager,
    private val settingsManager: SettingsManager,
    private val coroutineScope: CoroutineScope,
) {

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
        if (!settings.isUserOnboarded && screenState !is ScreenState.OnboardingState) {
            onOpenOnboarding()
        }
        UiState(screenState = screenState, apps = apps, settings = settings)
    }.stateIn(
        scope = coroutineScope,
        started = WhileUiSubscribed,
        initialValue = UiState(screenState = ScreenState.LoadHomeScreenState, apps = Apps()),
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
        frameworkActionsManager.openSetDefaultLauncherChooser()
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
        when {
            nextStep < OnboardingPage.SCREEN_1.pageNumber -> cancelOnboarding()
            nextStep > OnboardingPage.LAST_PAGE.pageNumber -> {
                coroutineScope.launch {
                    settingsManager.setIsUserOnboarded(isOnboarded = true)
                    loadHomeScreen()
                }
            }
            else -> {
                this.screenState =
                    ScreenState.OnboardingState(OnboardingPage.values().first { it.pageNumber == nextStep })
            }
        }
    }

    fun onShareLauncher() {
        frameworkActionsManager.shareLauncher()
    }

    fun onOpenSystemSettings() {
        frameworkActionsManager.openSystemSettings()
    }

    fun onUninstallLauncher() {
        val ownComponentKey = ComponentKey(
            packageName = BuildConfig.APPLICATION_ID,
            className = "${BuildConfig.APPLICATION_ID}.ui.MainActivity",
            userHandle = Process.myUserHandle(),
        )
        frameworkActionsManager.openAppUninstallDialog(ownComponentKey)
    }

    fun cancelOnboarding() {
        loadHomeScreen()
    }

    suspend fun onSetFavorites(newFavorites: List<AppItemInfo>) {
        appActivityRepository.setFavorites(newFavorites)
        screenState = ScreenState.HomeScreenState
    }
}
