package org.tuerantuer.launcher.ui

import android.os.Process
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
 * Default implementation of [MainViewModel].
 *
 * @author Peter Huber
 */
class MainViewModelImpl(
    private val appActivityRepository: AppActivityRepository,
    private val appLauncher: AppLauncher,
    private val frameworkActionsManager: FrameworkActionsManager,
    private val settingsManager: SettingsManager,
    private val coroutineScope: CoroutineScope,
) : MainViewModel {

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
    override val uiState: StateFlow<UiState> = combine(
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

    /**
     * @return true if pressing back event was handled and should not be propagated further.
     */
    override fun goBack(): Boolean {
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

    override fun onSetDefaultLauncher() {
        frameworkActionsManager.openSetDefaultLauncherChooser()
    }

    private fun loadHomeScreen() {
        screenState = ScreenState.HomeScreenState
    }

    override fun openApp(appItemInfo: AppItemInfo) {
        appLauncher.launchApp(appItemInfo)
    }

    override fun onEditFavorites() {
        screenState = ScreenState.EditFavoritesScreenState
    }

    override fun onShowAllApps() {
        screenState = ScreenState.AllAppsScreenState
    }

    override fun onOpenSettings() {
        screenState = ScreenState.SettingsState
    }

    override fun onOpenOnboarding() {
        screenState = ScreenState.OnboardingState(OnboardingPage.SCREEN_1)
    }

    override suspend fun onSetIconSize(appIconSize: AppIconSize) {
        settingsManager.setAppIconSize(appIconSize)
    }

    override fun onGoToNextOnboardingStep() {
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

    override fun onShareLauncher() {
        frameworkActionsManager.shareLauncher()
    }

    override fun onOpenSystemSettings() {
        frameworkActionsManager.openSystemSettings()
    }

    override fun onUninstallLauncher() {
        val ownComponentKey = ComponentKey(
            packageName = BuildConfig.APPLICATION_ID,
            className = "${BuildConfig.APPLICATION_ID}.ui.MainActivity",
            userHandle = Process.myUserHandle(),
        )
        frameworkActionsManager.openAppUninstallDialog(ownComponentKey)
    }

    override fun cancelOnboarding() {
        loadHomeScreen()
    }

    override suspend fun onSetFavorites(newFavorites: List<AppItemInfo>) {
        appActivityRepository.setFavorites(newFavorites)
        screenState = ScreenState.HomeScreenState
    }
}
