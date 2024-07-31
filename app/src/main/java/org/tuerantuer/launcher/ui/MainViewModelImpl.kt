package org.tuerantuer.launcher.ui

import android.os.Process
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.BuildConfig
import org.tuerantuer.launcher.app.AppActivityRepository
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.app.AppLauncher
import org.tuerantuer.launcher.app.Apps
import org.tuerantuer.launcher.app.appIdentifier.ComponentKey
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.data.datastore.SettingsManager
import org.tuerantuer.launcher.data.datastore.WallpaperType
import org.tuerantuer.launcher.ui.data.OnboardingPage
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.SettingsPage
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.util.FrameworkActionsManager

/**
 * Default implementation of [MainViewModel].
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
            if (screenState == value) return
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
        started = SharingStarted.Lazily,
        initialValue = UiState(screenState = ScreenState.LoadingState, apps = Apps()),
    )

    /**
     * @return true if pressing back event was handled and should not be propagated further.
     */
    override fun goBack(): Boolean {
        when (val screenState = screenState) {
            is ScreenState.LoadingState -> return true
            is ScreenState.HomeScreenState -> return true
            is ScreenState.OnboardingState -> onGoToPreviousOnboardingStep()
            is ScreenState.AllAppsScreenState -> loadHomeScreen()
            is ScreenState.SettingsState -> {
                val parentPage = screenState.settingsPage.parentPage
                if (parentPage != null) {
                    openSettingsPage(parentPage)
                } else {
                    loadHomeScreen()
                }
            }

            is ScreenState.EditFavoritesScreenState -> loadHomeScreen()
            // is ScreenState.LoadingState, is ScreenState.MainMenu, ScreenState.OnboardingState -> return false
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
        loadHomeScreen() // always go to home screen after launching app, does nothing if already on home screen
    }

    override fun onEditFavorites() {
        screenState = ScreenState.EditFavoritesScreenState
    }

    override fun onShowAllApps() {
        screenState = ScreenState.AllAppsScreenState
    }

    override fun openSettingsPage(settingsPage: SettingsPage) {
        screenState = ScreenState.SettingsState(settingsPage)
    }

    override fun onOpenOnboarding() {
        screenState = ScreenState.OnboardingState(OnboardingPage.INTRODUCTION_1)
    }

    override suspend fun onSetIconSize(appIconSize: AppIconSize) {
        settingsManager.setAppIconSize(appIconSize)
    }

    override suspend fun onSetWallpaperType(wallpaperType: WallpaperType) {
        settingsManager.setWallpaperType(wallpaperType)
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
            nextStep < OnboardingPage.INTRODUCTION_1.pageNumber -> cancelOnboarding()
            nextStep > OnboardingPage.SETUP_FINISHED_3.pageNumber -> {
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

    override fun onOpenAccessibilitySettings() {
        frameworkActionsManager.openAccessibilitySettings()
    }

    override fun onOpenDisplaySettings() {
        frameworkActionsManager.openDisplaySettings()
    }

    override fun onOpenSoundSettings() {
        frameworkActionsManager.openSoundSettings()
    }

    override fun onOpenApplicationSettings() {
        frameworkActionsManager.openApplicationSettings()
    }
    override fun onUninstallLauncher() {
        val ownComponentKey = ComponentKey(
            packageName = BuildConfig.APPLICATION_ID,
            className = "${BuildConfig.APPLICATION_ID}.ui.MainActivity",
            userHandle = Process.myUserHandle(),
        )
        frameworkActionsManager.openAppUninstallDialog(ownComponentKey)
    }

    override fun onWriteFeedbackMail() {
        frameworkActionsManager.sendFeedbackMail()
    }

    override fun cancelOnboarding() {
        loadHomeScreen()
    }

    override suspend fun onSetFavorites(newFavorites: List<AppItemInfo>) {
        appActivityRepository.setFavorites(newFavorites)
    }

    override suspend fun onSetWallpaper(wallpaperRes: Int) {
        frameworkActionsManager.setWallpaper(wallpaperRes)
        loadHomeScreen()
    }
}
