package org.tuerantuer.launcher.ui.screen

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.ui.data.OnboardingPage
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
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
    val selectedFavorites = remember { mutableStateOf(uiState.favorites) }
    val page = screenState.onboardingPage
    val contentTextRes = when (page) {
        OnboardingPage.INTRODUCTION_1 -> R.string.welcome_1
        OnboardingPage.INTRODUCTION_2 -> R.string.welcome_2
        OnboardingPage.INTRODUCTION_3 -> R.string.welcome_3
        OnboardingPage.INTRODUCTION_4 -> R.string.welcome_4
        OnboardingPage.PROGRESS_EXPLANATION_1 -> R.string.setup_1
        OnboardingPage.PROGRESS_EXPLANATION_2 -> R.string.setup_2
        OnboardingPage.PROGRESS_EXPLANATION_3 -> R.string.setup_3
        OnboardingPage.SET_AS_DEFAULT_1 -> R.string.setup_4
        OnboardingPage.SET_AS_DEFAULT_2 -> R.string.setup_5
        OnboardingPage.SET_AS_DEFAULT_3 -> R.string.setup_6
        OnboardingPage.PRIVACY_POLICY -> R.string.setup_7
        OnboardingPage.TERMS_OF_SERVICE -> R.string.setup_8
        OnboardingPage.SET_SIZE_INTRO -> R.string.setup_9
        OnboardingPage.SET_SIZE_MAIN -> null
        OnboardingPage.SET_FAVORITES_INTRO_1 -> R.string.setup_10
        OnboardingPage.SET_FAVORITES_INTRO_2 -> R.string.setup_11
        OnboardingPage.SET_FAVORITES_INTRO_3 -> R.string.setup_12
        OnboardingPage.SETUP_FINISHED_1 -> R.string.setup_13
        OnboardingPage.SETUP_FINISHED_2 -> R.string.setup_14
        OnboardingPage.SETUP_FINISHED_3 -> R.string.setup_15
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
                progress = page.pageNumber.toFloat() / OnboardingPage.SETUP_FINISHED_3.pageNumber.toFloat(),
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
                    selectedFavorites = selectedFavorites,
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
                        .run { if (contentTextRes != null) heightIn(min = 260.dp) else this }
                        .padding(
                            top = 32.dp,
                            bottom = 0.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                ) {
                    val headerTextRes = when (page) {
                        OnboardingPage.INTRODUCTION_1,
                        OnboardingPage.INTRODUCTION_2,
                        OnboardingPage.INTRODUCTION_3,
                        OnboardingPage.INTRODUCTION_4,
                        -> R.string.welcome
                        OnboardingPage.SETUP_FINISHED_3,
                        -> R.string.done
                        else -> R.string.setup
                    }
                    Text(
                        text = stringResource(headerTextRes),
                        Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                    if (contentTextRes != null) {
                        Text(
                            text = stringResource(contentTextRes),
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                    SheetButtons(
                        page = page,
                        onSetDefaultLauncher = onSetDefaultLauncher,
                        onGoToNextStep = onGoToNextStep,
                        onSetFavorites = {
                            // TODO: Don't sort favorites by name. Instead, allow the user to reorder them.
                            onSetFavorites.invoke(selectedFavorites.value.sortedBy { it.name })
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun SheetButtons(
    page: OnboardingPage,
    onSetDefaultLauncher: () -> Unit,
    onGoToNextStep: () -> Unit,
    onSetFavorites: () -> Unit,
) {
    Box(
        modifier = Modifier.heightIn(min = 100.dp),
        contentAlignment = Alignment.Center,
    ) {
        when (page) {
            OnboardingPage.SET_AS_DEFAULT_3 -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    ExtendedFab(
                        onClick = {
                            onSetDefaultLauncher.invoke()
                            onGoToNextStep.invoke()
                        },
                        textRes = R.string.button_yes,
                        imageVector = Icons.Outlined.Home,
                    )
                    Button(
                        onClick = onGoToNextStep,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    ) {
                        Text(text = stringResource(R.string.button_skip))
                    }
                }
            }
            OnboardingPage.PRIVACY_POLICY, OnboardingPage.TERMS_OF_SERVICE -> {
                ExtendedFab(
                    onClick = onGoToNextStep,
                    textRes = R.string.button_accept,
                    imageVector = Icons.Outlined.Done,
                )
            }
            OnboardingPage.SET_SIZE_MAIN -> {
                ExtendedFab(
                    onClick = onGoToNextStep,
                    textRes = R.string.button_set_size,
                    imageVector = Icons.Outlined.Done,
                )
            }
            OnboardingPage.SET_FAVORITES_MAIN -> {
                ExtendedFab(
                    onClick = {
                        onSetFavorites.invoke()
                        onGoToNextStep.invoke()
                    },
                    textRes = R.string.button_save_favorites,
                    imageVector = Icons.Outlined.Done,
                )
            }
            OnboardingPage.SETUP_FINISHED_3 -> {
                ExtendedFab(
                    onClick = onGoToNextStep,
                    textRes = R.string.start,
                    imageVector = Icons.Outlined.Home,
                )
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

@Composable
fun ExtendedFab(
    modifier: Modifier = Modifier,
    textRes: Int,
    imageVector: ImageVector,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = onClick,
        text = {
            Text(text = stringResource(textRes))
        },
        icon = {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )
        },
    )
}

@Composable
fun ColumnScope.MainContent(
    uiState: UiState,
    page: OnboardingPage,
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    selectedFavorites: MutableState<List<AppItemInfo>>,
) {
    when (page) {
        in setOf(OnboardingPage.PRIVACY_POLICY, OnboardingPage.TERMS_OF_SERVICE) -> {
            val textRes = when (page) {
                OnboardingPage.PRIVACY_POLICY -> R.string.privacy_policy_text
                OnboardingPage.TERMS_OF_SERVICE -> R.string.terms_of_service_text
                else -> throw IllegalStateException()
            }
            ScrollableText(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(textRes),
            )
        }
        OnboardingPage.SET_SIZE_MAIN -> {
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
        OnboardingPage.SET_FAVORITES_MAIN -> {
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
                val imageRes = when (page) {
                    OnboardingPage.INTRODUCTION_1,
                    OnboardingPage.INTRODUCTION_2,
                    OnboardingPage.INTRODUCTION_3,
                    OnboardingPage.INTRODUCTION_4,
                    -> R.drawable.onboarding_image_1
                    OnboardingPage.PROGRESS_EXPLANATION_1,
                    -> R.drawable.onboarding_image_2
                    OnboardingPage.PROGRESS_EXPLANATION_2,
                    -> R.drawable.onboarding_image_3
                    OnboardingPage.PROGRESS_EXPLANATION_3,
                    -> R.drawable.onboarding_image_4
                    OnboardingPage.SET_AS_DEFAULT_1,
                    OnboardingPage.SET_AS_DEFAULT_2,
                    OnboardingPage.SET_AS_DEFAULT_3,
                    -> R.drawable.onboarding_image_5
                    OnboardingPage.SET_SIZE_INTRO,
                    -> R.drawable.onboarding_image_7
                    OnboardingPage.SET_FAVORITES_INTRO_1,
                    OnboardingPage.SET_FAVORITES_INTRO_2,
                    OnboardingPage.SET_FAVORITES_INTRO_3,
                    -> R.drawable.onboarding_image_8
                    OnboardingPage.SETUP_FINISHED_1,
                    OnboardingPage.SETUP_FINISHED_2,
                    OnboardingPage.SETUP_FINISHED_3,
                    -> R.drawable.onboarding_image_9
                    else -> null
                }
                if (imageRes != null) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth()
                            .padding(32.dp)
                            .heightIn(0.dp, 240.dp),
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                    )
                }
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
                modifier = Modifier
                    .padding(16.dp),
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
                .height(8.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(4.dp)),
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
        val screenState = ScreenState.OnboardingState(OnboardingPage.INTRODUCTION_1)
        OnboardingScreen(uiState = UiState(screenState))
    }
}
