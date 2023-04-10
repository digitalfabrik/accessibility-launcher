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
import org.tuerantuer.launcher.ui.motion.CustomMaterialMotion
import org.tuerantuer.launcher.ui.motion.ScreenTransitionManager
import org.tuerantuer.launcher.ui.screen.AllAppsScreen
import org.tuerantuer.launcher.ui.screen.EditFavoritesScreen
import org.tuerantuer.launcher.ui.screen.HomeScreen
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
            LauncherTheme {
                LauncherApp(mainViewModel, screenTransitionManager)
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
) {
    val coroutinesScope = rememberCoroutineScope()
//    val openDialog = remember { mutableStateOf(false) }
    val uiState by mainViewModel.uiState.collectAsStateWithLifecycle()
//        // Error Dialog
//        val error = (uiState as? ErrorProneState)?.error
//        if (error != null) {
//            ConstructErrorDialog(e = error, open = openDialog)
//            displayError(
//                uiState,
//                scaffoldState,
//                mainViewModel,
//                coroutinesScope,
//                openDialog,
//                LocalContext.current,
//            )
//        }

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
//                    .background(MaterialTheme.colorScheme.background),
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
        is ScreenState.LoadHomeScreenState -> {}
        is ScreenState.HomeScreenState -> HomeScreen(
            uiState = uiState,
            onShowAllApps = { mainViewModel.onShowAllApps() },
            onOpenSettings = { mainViewModel.onOpenSettings() },
            onEditFavorites = { mainViewModel.onEditFavorites() },
            onShowOnboarding = { mainViewModel.onOpenOnboarding() },
            onOpenApp = { mainViewModel.openApp(it) },
        )
        is ScreenState.AllAppsScreenState -> AllAppsScreen(
            uiState = uiState,
            onOpenApp = { mainViewModel.openApp(it) },
        )
        is ScreenState.OnboardingState -> OnboardingScreen(
            uiState = uiState,
            onGoToPreviousStep = { mainViewModel.goBack() },
            onGoToNextStep = { mainViewModel.onGoToNextOnboardingStep() },
            onSetDefaultLauncher = { mainViewModel.onSetDefaultLauncher() },
            onCancelOnboarding = { mainViewModel.cancelOnboarding() },
            onSetIconSize = { appIconSize -> coroutinesScope.launch { mainViewModel.onSetIconSize(appIconSize) } },
            onSetFavorites = { favorites -> coroutinesScope.launch { mainViewModel.onSetFavorites(favorites) } },
        )
        is ScreenState.SettingsState -> SettingsScreen(
            uiState = uiState,
            onSetDefaultLauncher = { mainViewModel.onSetDefaultLauncher() },
            onShareLauncher = { mainViewModel.onShareLauncher() },
            onOpenSystemSettings = { mainViewModel.onOpenSystemSettings() },
            onUninstallLauncher = { mainViewModel.onUninstallLauncher() },
        )
        is ScreenState.EditFavoritesScreenState -> EditFavoritesScreen(
            uiState = uiState,
            onCancelEdits = { /*TODO*/ },
            onApplyEdits = { newFavorites -> coroutinesScope.launch { mainViewModel.onSetFavorites(newFavorites) } },
        )
    }
}
