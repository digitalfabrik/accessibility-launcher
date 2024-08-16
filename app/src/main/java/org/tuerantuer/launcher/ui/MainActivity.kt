package org.tuerantuer.launcher.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
            LauncherTheme(
                wallpaperType = uiState.settings.wallpaperType,
                scalingFactor = uiState.settings.appTextSize.scalingFactor,
            ) {
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
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    val darkTheme = isSystemInDarkTheme()
    val systemBarsColor = when {
        view.isInEditMode -> MaterialTheme.colorScheme.background
        uiState.settings.wallpaperType != WallpaperType.SOLID_COLOR
                && uiState.screenState is ScreenState.HomeScreenState
        -> MaterialTheme.colorScheme.background.copy(alpha = 0.5f)
        else -> MaterialTheme.colorScheme.background
    }
    val insets = ViewCompat.getRootWindowInsets(view)
    val systemBarsInsets = insets?.getInsets(WindowInsetsCompat.Type.systemBars())
    // Android 15+ has edge-to-edge enabled by default and we need to pad the system bars here manually
    val padSystemBars = android.os.Build.VERSION.SDK_INT > 34
    val statusBarHeightPx = if (padSystemBars) systemBarsInsets?.top ?: 0 else 0
    val navigationBarHeightPx = if (padSystemBars) systemBarsInsets?.bottom ?: 0 else 0
    val density = LocalDensity.current
    val statusBarHeight = with(density) { statusBarHeightPx.toDp() }
    val navigationBarHeight = with(density) { navigationBarHeightPx.toDp() }

    SideEffect {
        val backgroundColorArgb = systemBarsColor.toArgb()
        // Ignored on Android 15+
        (view.context as Activity).window.apply {
            statusBarColor = backgroundColorArgb
            navigationBarColor = backgroundColorArgb
        }

        ViewCompat.getWindowInsetsController(view)?.apply {
            val useLightBars = !darkTheme
            isAppearanceLightStatusBars = useLightBars
            isAppearanceLightNavigationBars = useLightBars
        }
    }

    // On Android 15+ edge-to-edge is enforced and we emulate the system bars with colored boxes
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        // Status bar background for Android 15+
        Box(
            modifier = Modifier
                .height(statusBarHeight)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(systemBarsColor),
        )
        Box(
            modifier = Modifier
                .systemBarsPadding(),
        ) {
            content()
        }
        // Navigation bar background for Android 15+
        Box(
            modifier = Modifier
                .height(navigationBarHeight)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(systemBarsColor),
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LauncherApp(
    mainViewModel: MainViewModel,
    screenTransitionManager: ScreenTransitionManager,
    uiState: UiState,
) {
    val backgroundColor = when {
        uiState.settings.wallpaperType != WallpaperType.SOLID_COLOR
                && uiState.screenState is ScreenState.HomeScreenState
        -> MaterialTheme.colorScheme.background.copy(alpha = 0f)
        else -> MaterialTheme.colorScheme.background
    }
    val coroutinesScope = rememberCoroutineScope()
    StatusAndNavigationBars(uiState = uiState) {
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
                    .background(backgroundColor),
                horizontalAlignment = Alignment.CenterHorizontally,
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
            onSetIconSize = { appIconSize ->
                coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize, uiState.settings.isUserOnboarded) }
            },
            onSetTextSize = { appTextSize -> coroutinesScope.launch { mainViewModel.onSetTextSize(appTextSize) } },
            onSetFavorites = { favorites -> coroutinesScope.launch { mainViewModel.onSetFavorites(favorites) } },
        )
        is ScreenState.SettingsState -> SettingsScreen(
            uiState = uiState,
            onSetDefaultLauncher = mainViewModel::onSetDefaultLauncher,
            onShareLauncher = mainViewModel::onShareLauncher,
            onOpenSystemSettings = mainViewModel::onOpenSystemSettings,
            onOpenAccessibilitySettings = mainViewModel::onOpenAccessibilitySettings,
            onOpenSoundSettings = mainViewModel::onOpenSoundSettings,
            onOpenApplicationSettings = mainViewModel::onOpenApplicationSettings,
            onOpenDisplaySettings = mainViewModel::onOpenDisplaySettings,
            onUninstallLauncher = mainViewModel::onUninstallLauncher,
            onWriteFeedbackMail = mainViewModel::onWriteFeedbackMail,
            onOpenSettingsPage = mainViewModel::openSettingsPage,
            onSetIconSize = { appIconSize ->
                coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize, uiState.settings.isUserOnboarded) }
            },
            onSetTextSize = { appTextSize -> coroutinesScope.launch { mainViewModel.onSetTextSize(appTextSize) } },
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
