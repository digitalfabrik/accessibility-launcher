package org.tuerantuer.launcher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.SettingsPage
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.motion.CustomMaterialMotion
import org.tuerantuer.launcher.ui.motion.ScreenTransitionManager
import org.tuerantuer.launcher.ui.screen.AllAppsScreen
import org.tuerantuer.launcher.ui.screen.EditFavoritesScreen
import org.tuerantuer.launcher.ui.screen.HomeScreen
import org.tuerantuer.launcher.ui.screen.LoadingScreen
import org.tuerantuer.launcher.ui.screen.OnboardingScreen
import org.tuerantuer.launcher.ui.screen.SettingsScreen
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import javax.inject.Inject

/**
 * Main UI entry point of the app.
 * @author Peter Huber
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var screenTransitionManager: ScreenTransitionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
            LauncherTheme(wallpaperType = uiState.settings.wallpaperType) {
                LauncherApp(mainViewModel, screenTransitionManager, uiState)
            }
        }
    }

    override fun onBackPressed() {
        if (!mainViewModel.goBack()) {
            super.onBackPressed()
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LauncherApp(
    mainViewModel: MainViewModel,
    screenTransitionManager: ScreenTransitionManager,
    uiState: UiState,
) {
    val coroutinesScope = rememberCoroutineScope()
    CustomMaterialMotion(
        targetState = uiState,
        animationForStateTransition = { old, new ->
            screenTransitionManager.loadAnimationForUiStateTransition(old, new)
        },
    ) { animatedUiState ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 550.dp),
            ) {
                Screens(animatedUiState, mainViewModel, coroutinesScope)
            }
        }
    }
}

@Composable
fun Screens(
    uiState: UiState,
    mainViewModel: MainViewModel,
    coroutinesScope: CoroutineScope,
) {
    when (uiState.screenState) {
        is ScreenState.LoadingState -> LoadingScreen()
        is ScreenState.HomeScreenState -> HomeScreen(
            uiState = uiState,
            onShowAllApps = mainViewModel::onShowAllApps,
            onOpenSettings = { mainViewModel.openSettingsPage(SettingsPage.Overview) },
            onEditFavorites = mainViewModel::onEditFavorites,
            onShowOnboarding = mainViewModel::onOpenOnboarding,
            onOpenApp = mainViewModel::openApp,
        )
        is ScreenState.AllAppsScreenState -> AllAppsScreen(
            uiState = uiState,
            onOpenApp = mainViewModel::openApp,
            onGoBack = mainViewModel::goBack,
        )
        is ScreenState.OnboardingState -> OnboardingScreen(
            uiState = uiState,
            onGoToPreviousStep = mainViewModel::goBack,
            onGoToNextStep = mainViewModel::onGoToNextOnboardingStep,
            onSetDefaultLauncher = mainViewModel::onSetDefaultLauncher,
            onCancelOnboarding = mainViewModel::cancelOnboarding,
            onSetIconSize = { appIconSize -> coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize) } },
            onSetFavorites = { favorites -> coroutinesScope.launch { mainViewModel.onSetFavorites(favorites) } },
        )
        is ScreenState.SettingsState -> SettingsScreen(
            uiState = uiState,
            onSetDefaultLauncher = mainViewModel::onSetDefaultLauncher,
            onShareLauncher = mainViewModel::onShareLauncher,
            onOpenSystemSettings = mainViewModel::onOpenSystemSettings,
            onOpenAccessibilitySettings = mainViewModel::onOpenAccessibilitySettings,
            onOpenSoundSettings = mainViewModel::onOpenSoundSettings,
            onOpenDisplaySettings = mainViewModel::onOpenDisplaySettings,
            onOpenNotificationSettings = mainViewModel::onOpenNotificationSettings,
            onUninstallLauncher = mainViewModel::onUninstallLauncher,
            onWriteFeedbackMail = mainViewModel::onWriteFeedbackMail,
            onOpenSettingsPage = mainViewModel::openSettingsPage,
            onSetIconSize = { appIconSize -> coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize) } },
            onSetWallpaperType = { wallpaperType ->
                coroutinesScope.launch { mainViewModel.onSetWallpaperType(wallpaperType) }
            },
            onGoBack = mainViewModel::goBack,
        )
        is ScreenState.EditFavoritesScreenState -> EditFavoritesScreen(
            uiState = uiState,
            onApplyEdits = { newFavorites ->
                coroutinesScope.launch {
                    mainViewModel.onSetFavorites(newFavorites)
                    mainViewModel.goBack()
                }
            },
            onGoBack = mainViewModel::goBack,
        )
    }
}
