package org.tuerantuer.launcher.ui.screen

import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.textview.MaterialTextView
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.data.datastore.Settings
import org.tuerantuer.launcher.ui.components.ExtendedFabComponent
import org.tuerantuer.launcher.ui.components.ScrollScrimComponent
import org.tuerantuer.launcher.ui.components.ScrollableColumn
import org.tuerantuer.launcher.ui.components.SetIconSizeComponent
import org.tuerantuer.launcher.ui.data.OnboardingPage
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.motion.CustomMaterialMotion
import org.tuerantuer.launcher.ui.motion.sharedXMotionSpec
import org.tuerantuer.launcher.ui.motion.sharedXMotionSpecReverse
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * The screen where the user can set up the launcher. The screen shows up the first time the launcher is started.
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
        OnboardingPage.SETTINGS_INTRODUCTION -> R.string.setup_settings_intro
        OnboardingPage.SET_AS_DEFAULT_1 -> R.string.setup_4
        OnboardingPage.SET_AS_DEFAULT_2 -> R.string.setup_5
        OnboardingPage.SET_AS_DEFAULT_3 -> R.string.setup_6
        OnboardingPage.PRIVACY_POLICY -> R.string.setup_7
        OnboardingPage.SET_SIZE_INTRO -> R.string.setup_9
        OnboardingPage.SET_SIZE_MAIN -> R.string.confirm_size
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
                settings = uiState.settings,
                progress = page.pageNumber.toFloat() / OnboardingPage.SETUP_FINISHED_3.pageNumber.toFloat(),
                onGoToPreviousStep = onGoToPreviousStep,
                onCancelOnboarding = onCancelOnboarding,
            )
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
            CustomMaterialMotion(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                targetState = uiState,
                animationForStateTransition = { old, new ->
                    val oldPage = (old.screenState as? ScreenState.OnboardingState)?.onboardingPage
                        ?: return@CustomMaterialMotion null
                    val newPage = (new.screenState as? ScreenState.OnboardingState)?.onboardingPage
                        ?: return@CustomMaterialMotion null
                    when {
                        getMainContentIconResFromPage(oldPage) == getMainContentIconResFromPage(newPage) -> null
                        oldPage.pageNumber < newPage.pageNumber -> sharedXMotionSpec
                        oldPage.pageNumber > newPage.pageNumber -> sharedXMotionSpecReverse
                        old.settings.appIconSize < new.settings.appIconSize -> sharedXMotionSpec
                        old.settings.appIconSize > new.settings.appIconSize -> sharedXMotionSpecReverse
                        else -> sharedXMotionSpec
                    }
                },
            ) { animatedUiState ->
                MainContent(
                    uiState = animatedUiState,
                    onSetIconSize = onSetIconSize,
                    selectedFavorites = selectedFavorites,
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f),
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
                    if (contentTextRes != null) {
                        Text(
                            text = stringResource(contentTextRes),
                            Modifier
                                .wrapContentHeight()
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                    SheetButtons(
                        page = page,
                        onSetDefaultLauncher = onSetDefaultLauncher,
                        onGoToNextStep = onGoToNextStep,
                        onSetFavorites = {
                            // TODO: Don't sort favorites by name. Instead, allow the user to reorder them.
                            onSetFavorites.invoke(selectedFavorites.value.sorted())
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
                    ExtendedFabComponent(
                        onClick = {
                            onSetDefaultLauncher.invoke()
                            onGoToNextStep.invoke()
                        },
                        textRes = R.string.button_yes,
                        imageVector = Icons.Outlined.Home,
                    )
                    Button(
                        shape = FloatingActionButtonDefaults.extendedFabShape,
                        onClick = onGoToNextStep,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        ),
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp), // makes it the same height as the FAB
                            text = stringResource(R.string.button_skip),
                        )
                    }
                }
            }
            OnboardingPage.PRIVACY_POLICY -> {
                ExtendedFabComponent(
                    onClick = onGoToNextStep,
                    textRes = R.string.button_accept,
                    imageVector = null,
                )
            }
            OnboardingPage.SET_SIZE_MAIN -> {
                ExtendedFabComponent(
                    onClick = onGoToNextStep,
                    textRes = R.string.button_set_size,
                    imageVector = null,
                )
            }
            OnboardingPage.SET_FAVORITES_MAIN -> {
                ExtendedFabComponent(
                    onClick = {
                        onSetFavorites.invoke()
                        onGoToNextStep.invoke()
                    },
                    textRes = R.string.button_save_favorites,
                    imageVector = null,
                )
            }
            OnboardingPage.SETUP_FINISHED_3 -> {
                ExtendedFabComponent(
                    onClick = onGoToNextStep,
                    textRes = R.string.start,
                    imageVector = Icons.Outlined.Home,
                )
            }
            else -> {
                ExtendedFabComponent(
                    onClick = onGoToNextStep,
                    textRes = R.string.next_step,
                    imageVector = Icons.Filled.ArrowForward,
                )
            }
        }
    }
}

@Composable
fun ColumnScope.MainContent(
    uiState: UiState,
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    selectedFavorites: MutableState<List<AppItemInfo>>,
) {
    val screenState = uiState.screenState as ScreenState.OnboardingState
    when (val page = screenState.onboardingPage) {
        OnboardingPage.PRIVACY_POLICY -> {
            ScrollableHtmlText(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.privacy_policy_text),
            )
        }
        OnboardingPage.SET_SIZE_MAIN -> {
            SetIconSizeComponent(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onSetIconSize = onSetIconSize,
            )
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
            val imageRes = getMainContentIconResFromPage(page)
            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
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

private fun getMainContentIconResFromPage(page: OnboardingPage): Int? {
    return when (page) {
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
}

@Composable
fun ScrollableHtmlText(
    modifier: Modifier = Modifier,
    text: String,
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val linkColor = MaterialTheme.colorScheme.primary
    Box(modifier = modifier) {
        ScrollableColumn {
            AndroidView(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                factory = {
                    MaterialTextView(it).apply {
                        movementMethod = LinkMovementMethod.getInstance()
                        setLinkTextColor(linkColor.toArgb())
                    }
                },
                update = { textView ->
                    textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE)
                    textView.setTextColor(textColor.toArgb())
                },
            )
        }
        ScrollScrimComponent(
            Modifier
                .fillMaxWidth()
                .height(32.dp),
        )
    }
}

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    settings: Settings,
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
                .size(48.dp)
                .padding(4.dp),
        ) {
            Icon(
                Icons.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.go_back),
                modifier = Modifier.size(48.dp)
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
        // Only allow canceling onboarding if the user has already completed it once
        if (settings.isUserOnboarded) {
            IconButton(
                onClick = onCancelOnboarding,
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = stringResource(id = R.string.cancel_setup_assistant),
                    modifier = Modifier.size(48.dp)
                )
            }
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
