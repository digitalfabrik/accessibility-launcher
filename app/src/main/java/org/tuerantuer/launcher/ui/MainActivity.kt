package org.tuerantuer.launcher.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.data.datastore.WallpaperType
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

@Composable
fun StatusAndNavigationBars(
    uiState: UiState,
) {
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        val useTransparentBars = uiState.settings.wallpaperType != WallpaperType.SOLID_COLOR
                && uiState.screenState is ScreenState.HomeScreenState
        val backgroundColor = if (useTransparentBars) {
            MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.background
        }.toArgb()
        SideEffect {
            (view.context as Activity).window.apply {
                statusBarColor = backgroundColor
                navigationBarColor = backgroundColor
            }
            ViewCompat.getWindowInsetsController(view)?.apply {
                val useLightBars = !darkTheme
                isAppearanceLightStatusBars = useLightBars
                isAppearanceLightNavigationBars = useLightBars
            }
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
    StatusAndNavigationBars(uiState)
    CustomMaterialMotion(
        targetState = uiState,
        animationForStateTransition = { old, new ->
            screenTransitionManager.loadAnimationForUiStateTransition(old, new)
        },
    ) { animatedUiState ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Screens(animatedUiState, mainViewModel, coroutinesScope)
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
            onUninstallLauncher = mainViewModel::onUninstallLauncher,
            onWriteFeedbackMail = mainViewModel::onWriteFeedbackMail,
            onOpenSettingsPage = mainViewModel::openSettingsPage,
            onSetIconSize = { appIconSize -> coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize) } },
            onSetWallpaperType = { wallpaperType ->
                coroutinesScope.launch { mainViewModel.onSetWallpaperType(wallpaperType) }
            },
            onSetWallpaper = { wallpaperRes -> coroutinesScope.launch { mainViewModel.onSetWallpaper(wallpaperRes) } },
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
