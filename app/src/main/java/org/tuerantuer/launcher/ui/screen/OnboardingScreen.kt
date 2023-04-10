package org.tuerantuer.launcher.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.data.AppIconSize
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.OnboardingPage
import org.tuerantuer.launcher.ui.ScreenState
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.motion.CustomMaterialMotion
import org.tuerantuer.launcher.ui.motion.DefaultSlideDistance
import org.tuerantuer.launcher.ui.motion.materialSharedAxisXIn
import org.tuerantuer.launcher.ui.motion.materialSharedAxisXOut
import org.tuerantuer.launcher.ui.motion.with
import org.tuerantuer.launcher.ui.theme.LauncherTheme

@ExperimentalAnimationApi
val sharedXMotionSpec = materialSharedAxisXIn() with materialSharedAxisXOut()

@ExperimentalAnimationApi
val sharedXMotionSpecReverse =
    materialSharedAxisXIn(-DefaultSlideDistance) with materialSharedAxisXOut(-DefaultSlideDistance)

/**
 * The screen where the user can set up the launcher. The screen shows up the first time the launcher is started.
 *
 * @author Peter Huber
 * Created on 07/03/2023
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun OnboardingScreen(
    uiState: UiState,
    onGoToNextStep: () -> Unit = {},
    onGoToPreviousStep: () -> Unit = {},
    onSetDefaultLauncher: () -> Unit = {},
    onCancelOnboarding: () -> Unit = {},
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    onSetFavorites: (newFavorites: List<AppItemInfo>) -> Unit = {},
) {
    val screenState = uiState.screenState
    require(screenState is ScreenState.OnboardingState)
    val page = screenState.onboardingPage
    val textRes = when (page) {
        OnboardingPage.SCREEN_1 -> R.string.welcome_1
        OnboardingPage.SCREEN_2 -> R.string.welcome_2
        OnboardingPage.SCREEN_3 -> R.string.welcome_3
        OnboardingPage.SCREEN_4 -> R.string.welcome_4
        OnboardingPage.SCREEN_5 -> R.string.setup_1
        OnboardingPage.SCREEN_6 -> R.string.setup_2
        OnboardingPage.SCREEN_7 -> R.string.setup_3
        OnboardingPage.SCREEN_8 -> R.string.setup_4
        OnboardingPage.SCREEN_9 -> R.string.setup_5
        OnboardingPage.SCREEN_SET_DEFAULT_LAUNCHER -> R.string.setup_6
        OnboardingPage.SCREEN_PRIVACY_POLICY -> R.string.setup_7
        OnboardingPage.SCREEN_TERMS_OF_SERVICE -> R.string.setup_8
        OnboardingPage.SCREEN_SET_SIZE_INTRO -> R.string.setup_9
        OnboardingPage.SCREEN_SET_SIZE_MAIN -> null
        OnboardingPage.SCREEN_SET_FAVORITES_INTRO_1 -> R.string.setup_10
        OnboardingPage.SCREEN_SET_FAVORITES_INTRO_2 -> R.string.setup_11
        OnboardingPage.SCREEN_SET_FAVORITES_INTRO_3 -> R.string.setup_12
        OnboardingPage.SCREEN_SETUP_FINISHED_1 -> R.string.setup_13
        OnboardingPage.SCREEN_SETUP_FINISHED_2 -> R.string.setup_14
        OnboardingPage.SCREEN_SETUP_FINISHED_3 -> R.string.setup_15
        else -> null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Toolbar(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                progress = page.pageNumber.toFloat() / OnboardingPage.LAST_PAGE.pageNumber.toFloat(),
                onGoToPreviousStep = onGoToPreviousStep,
                onCancelOnboarding = onCancelOnboarding,
            )
            CustomMaterialMotion(
                modifier = Modifier.fillMaxWidth().weight(1f),
                targetState = uiState,
                animationForStateTransition = { old, new ->
                    val oldPage = (old.screenState as? ScreenState.OnboardingState)?.onboardingPage
                        ?: return@CustomMaterialMotion null
                    val newPage = (new.screenState as? ScreenState.OnboardingState)?.onboardingPage
                        ?: return@CustomMaterialMotion null
                    when {
                        oldPage.pageNumber < newPage.pageNumber -> sharedXMotionSpec
                        oldPage.pageNumber > newPage.pageNumber -> sharedXMotionSpecReverse
                        old.settings.appIconSize < new.settings.appIconSize -> sharedXMotionSpec
                        old.settings.appIconSize > new.settings.appIconSize -> sharedXMotionSpecReverse
                        else -> sharedXMotionSpec
                    }
                },
            ) { animatedUiState ->
                MainContent(
                    uiState = uiState,
                    page = page,
                    onSetIconSize = onSetIconSize,
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shadowElevation = 8.dp,
                // only round top corners
                shape = MaterialTheme.shapes.medium.copy(
                    bottomEnd = CornerSize(0f),
                    bottomStart = CornerSize(0f),
                ),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            bottom = 64.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                ) {
                    Text(
                        text = stringResource(R.string.welcome),
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                    if (textRes != null) {
                        Text(
                            text = stringResource(textRes),
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 64.dp),
                            minLines = 4,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                    when (page) {
                        OnboardingPage.SCREEN_SET_DEFAULT_LAUNCHER -> {
                            Button(onClick = onSetDefaultLauncher) {
                                Text(text = stringResource(R.string.button_yes))
                            }
                            Button(onClick = onGoToNextStep) {
                                Text(text = stringResource(R.string.button_skip))
                            }
                        }
                        OnboardingPage.SCREEN_PRIVACY_POLICY, OnboardingPage.SCREEN_TERMS_OF_SERVICE -> {
                            Button(onClick = onGoToNextStep) {
                                Text(text = stringResource(R.string.button_accept))
                            }
                        }
                        OnboardingPage.SCREEN_SET_SIZE_MAIN -> {
                            ExtendedFloatingActionButton(
                                onClick = onGoToNextStep,
                                text = {
                                    Text(text = stringResource(R.string.button_set_size))
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = null,
                                    )
                                },
                            )
                        }
                        OnboardingPage.SCREEN_SET_FAVORITES_MAIN -> {
                            ExtendedFloatingActionButton(
                                onClick = onGoToNextStep,
                                text = {
                                    Text(text = stringResource(R.string.button_save_favorites))
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Done,
                                        contentDescription = null,
                                    )
                                },
                            )
                        }
                        OnboardingPage.SCREEN_SETUP_FINISHED_3 -> {
                            Button(onClick = onGoToNextStep) {
                                Text(text = stringResource(R.string.start))
                            }
                        }
                        else -> {
                            FloatingActionButton(onClick = onGoToNextStep) {
                                Icon(
                                    Icons.Filled.ArrowForward,
                                    contentDescription = stringResource(id = R.string.next_step),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.MainContent(
    uiState: UiState,
    page: OnboardingPage,
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
) {
    when (page) {
        in setOf(OnboardingPage.SCREEN_PRIVACY_POLICY, OnboardingPage.SCREEN_TERMS_OF_SERVICE) -> {
            val textRes = when (page) {
                OnboardingPage.SCREEN_PRIVACY_POLICY -> R.string.privacy_policy_text
                OnboardingPage.SCREEN_TERMS_OF_SERVICE -> R.string.terms_of_service_text
                else -> throw IllegalStateException()
            }
            ScrollableText(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                text = stringResource(textRes),
            )
        }
        OnboardingPage.SCREEN_SET_SIZE_MAIN -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    val iconSizeDp = uiState.settings.appIconSize.sizeDp.dp
                    Box(
                        modifier = Modifier
                            .width(iconSizeDp)
                            .height(iconSizeDp)
                            .background(Color.LightGray)
                            .align(Alignment.Center),
                    )
                }
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    val allAppIconSizes = AppIconSize.values()
                    val currentAppIconSize = uiState.settings.appIconSize
                    val currentAppIconSizeIndex = allAppIconSizes.indexOf(currentAppIconSize)
                    val smallerIconSize = allAppIconSizes.getOrNull(currentAppIconSizeIndex - 1)
                    val largerIconSize = allAppIconSizes.getOrNull(currentAppIconSizeIndex + 1)
                    if (smallerIconSize != null) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = { onSetIconSize(smallerIconSize) },
                        ) {
                            Icon(
                                Icons.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(id = R.string.button_decrease_size),
                            )
                        }
                    }
                    if (largerIconSize != null) {
                        FloatingActionButton(
                            modifier = Modifier.padding(16.dp),
                            onClick = { onSetIconSize(largerIconSize) },
                        ) {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = stringResource(id = R.string.button_increase_size),
                            )
                        }
                    }
                }
            }
        }
        OnboardingPage.SCREEN_SET_FAVORITES_MAIN -> {
            val selectedFavorites = remember { mutableStateOf(uiState.favorites) }
            val appIconSize = uiState.settings.appIconSize.sizeDp.dp
            EditFavoritesList(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                items = uiState.allApps,
                initiallySelectedItems = selectedFavorites.value,
                appIconSize = appIconSize,
                onAppChecked = { appItemInfo, isChecked ->
                    if (isChecked) {
                        selectedFavorites.value = selectedFavorites.value
                            .toMutableStateList().apply { add(appItemInfo) }
                    } else {
                        selectedFavorites.value = selectedFavorites.value
                            .toMutableStateList().apply { remove(appItemInfo) }
                    }
                },
                onAppMoved = { posA, posB ->
                },
            )
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .background(Color.LightGray)
                        .align(Alignment.Center),
                )
            }
        }
    }
}

@Composable
fun ScrollableText(
    modifier: Modifier = Modifier,
    text: String,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item {
            Text(
                text = text,
            )
        }
    }
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    progress: Float,
    onGoToPreviousStep: () -> Unit,
    onCancelOnboarding: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onGoToPreviousStep,
            modifier = Modifier
                .size(48.dp),
        ) {
            Icon(
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.go_back),
            )
        }
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = TweenSpec(durationMillis = 300),
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
        )
        IconButton(
            onClick = onCancelOnboarding,
            modifier = Modifier
                .size(48.dp),
        ) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = stringResource(id = R.string.cancel_setup_assistant),
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun OnoboardingPreview() {
    LauncherTheme {
        val screenState = ScreenState.OnboardingState(OnboardingPage.SCREEN_1)
        OnboardingScreen(uiState = UiState(screenState))
    }
}
