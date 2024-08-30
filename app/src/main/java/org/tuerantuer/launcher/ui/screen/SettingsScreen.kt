package org.tuerantuer.launcher.ui.screen

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Hearing
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.data.datastore.AppTextSize
import org.tuerantuer.launcher.data.datastore.WallpaperType
import org.tuerantuer.launcher.ui.components.BottomSheetComponent
import org.tuerantuer.launcher.ui.components.ExtendedFabComponent
import org.tuerantuer.launcher.ui.components.HeaderComponent
import org.tuerantuer.launcher.ui.components.ScrollBehaviorScreen
import org.tuerantuer.launcher.ui.components.ScrollableColumn
import org.tuerantuer.launcher.ui.components.SetIconSizeComponent
import org.tuerantuer.launcher.ui.components.SetTextSizeComponent
import org.tuerantuer.launcher.ui.components.SettingSwitchItem
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.SettingsPage
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.motion.CustomMaterialMotion
import org.tuerantuer.launcher.ui.motion.sharedXMotionSpec
import org.tuerantuer.launcher.ui.motion.sharedXMotionSpecReverse
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * The screen where the user can change the launcher settings.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(
    uiState: UiState,
    onSetDefaultLauncher: () -> Unit = {},
    onShareLauncher: () -> Unit = {},
    onOpenSystemSettings: () -> Unit = {},
    onOpenAccessibilitySettings: () -> Unit = {},
    onOpenDisplaySettings: () -> Unit = {},
    onOpenSoundSettings: () -> Unit = {},
    onOpenApplicationSettings: () -> Unit = {},
    onUninstallLauncher: () -> Unit = {},
    onWriteFeedbackMail: () -> Unit = {},
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    onSetTextSize: (appTextSize: AppTextSize) -> Unit = {},
    onGoBack: () -> Unit = {},
    onSetWallpaperType: (wallpaperType: WallpaperType) -> Unit = {},
    onSetWallpaper: (wallpaperRes: Int) -> Unit = {},
    onSetScrollBehavior: (useButtons: Boolean) -> Unit = {},
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .zIndex(10f),
    ) {
        Header(
            uiState,
            onGoBack = onGoBack,
        )
        CustomMaterialMotion(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            targetState = uiState,
            animationForStateTransition = { old, new ->
                val oldPage = (old.screenState as? ScreenState.SettingsState)?.settingsPage
                    ?: return@CustomMaterialMotion null
                val newPage = (new.screenState as? ScreenState.SettingsState)?.settingsPage
                    ?: return@CustomMaterialMotion null
                val comparison = oldPage.compareTo(newPage)
                when {
                    comparison > 0 -> sharedXMotionSpecReverse
                    comparison < 0 -> sharedXMotionSpec
                    else -> null
                }
            },
        ) { animatedUiState ->
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                MainContent(
                    uiState = animatedUiState,
                    onSetDefaultLauncher = onSetDefaultLauncher,
                    onShareLauncher = onShareLauncher,
                    onOpenSystemSettings = onOpenSystemSettings,
                    onOpenAccessibilitySettings = onOpenAccessibilitySettings,
                    onOpenDisplaySettings = onOpenDisplaySettings,
                    onOpenSoundSettings = onOpenSoundSettings,
                    onOpenApplicationSettings = onOpenApplicationSettings,
                    onUninstallLauncher = onUninstallLauncher,
                    onWriteFeedbackMail = onWriteFeedbackMail,
                    onOpenSettingsPage = onOpenSettingsPage,
                    onSetIconSize = onSetIconSize,
                    onSetTextSize = onSetTextSize,
                    onGoBack = onGoBack,
                    onSetWallpaperType = onSetWallpaperType,
                    onSetWallpaper = onSetWallpaper,
                    onSetScrollBehavior = onSetScrollBehavior,
                )
            }
        }
    }
}

@Composable
fun Header(
    uiState: UiState,
    onGoBack: () -> Unit = {},
) {
    val screenState = uiState.screenState
    require(screenState is ScreenState.SettingsState)
    val textRes = when (screenState.settingsPage) {
        SettingsPage.Overview -> R.string.settings
        SettingsPage.Assistant -> R.string.assistant
        SettingsPage.VisualAssistant -> R.string.assistant_visual
        SettingsPage.HearingAssistant -> R.string.assistant_hearing
        SettingsPage.SpeechAssistant -> R.string.assistant_speech
        SettingsPage.IconSize -> R.string.display_scale_icons
        SettingsPage.TextSize -> R.string.display_scale_text
        SettingsPage.Wallpaper -> R.string.wallpaper
        SettingsPage.DisplayTimeout -> R.string.display_timeout
        SettingsPage.Notifications -> R.string.notifications
        SettingsPage.InputDelay -> R.string.input_delay
        SettingsPage.ScrollBehavior -> R.string.scroll_behavior
        SettingsPage.NotificationSounds -> R.string.notification_sounds
        SettingsPage.ScreenReader -> R.string.screen_reader
        SettingsPage.VoiceCommands -> R.string.voice_commands
        SettingsPage.SetDefaultLauncher -> R.string.set_this_launcher_as_default
        SettingsPage.Feedback -> R.string.get_feedback_contact
        SettingsPage.SystemSettings -> R.string.open_system_settings
        SettingsPage.ShareLauncher -> R.string.share_launcher
        SettingsPage.UninstallLauncher -> R.string.uninstall_launcher
        SettingsPage.UninstallApps -> R.string.uninstall_apps
        SettingsPage.Licenses -> R.string.read_licenses
        SettingsPage.LicensesApache20 -> R.string.read_licenses
    }
    HeaderComponent(
        text = stringResource(textRes),
        onGoBack = onGoBack,
    )
}

@Composable
fun ColumnScope.MainContent(
    uiState: UiState,
    onSetDefaultLauncher: () -> Unit = {},
    onShareLauncher: () -> Unit = {},
    onOpenSystemSettings: () -> Unit = {},
    onOpenAccessibilitySettings: () -> Unit = {},
    onOpenDisplaySettings: () -> Unit = {},
    onOpenSoundSettings: () -> Unit = {},
    onOpenApplicationSettings: () -> Unit = {},
    onUninstallLauncher: () -> Unit = {},
    onWriteFeedbackMail: () -> Unit = {},
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    onSetTextSize: (appTextSize: AppTextSize) -> Unit = {},
    onGoBack: () -> Unit = {},
    onSetWallpaperType: (wallpaperType: WallpaperType) -> Unit = {},
    onSetWallpaper: (wallpaperRes: Int) -> Unit = {},
    onSetScrollBehavior: (useButtons: Boolean) -> Unit = {},
) {
    val screenState = uiState.screenState
    require(screenState is ScreenState.SettingsState)
    when (screenState.settingsPage) {
        SettingsPage.Overview -> SettingsOverviewScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.Assistant -> SettingsAssistantScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.SetDefaultLauncher -> SetDefaultLauncherScreen(
            onSetDefaultLauncher = onSetDefaultLauncher,
        )
        SettingsPage.ShareLauncher -> ShareLauncherScreen(
            onShareLauncher = onShareLauncher,
        )
        SettingsPage.Feedback -> FeedbackScreen(
            onWriteFeedbackMail = onWriteFeedbackMail,
        )
        SettingsPage.SystemSettings -> SystemSettingsScreen(
            onOpenSystemSettings = onOpenSystemSettings,
        )
        SettingsPage.UninstallLauncher -> UninstallLauncherScreen(
            onUninstallLauncher = onUninstallLauncher,
            onGoBack = onGoBack,
        )
        SettingsPage.IconSize -> IconSizeScreen(
            uiState = uiState,
            onSetIconSize = onSetIconSize,
            onGoBack = onGoBack,
        )
        SettingsPage.TextSize -> TextSizeScreen(
            uiState = uiState,
            onSetTextSize = onSetTextSize,
            onGoBack = onGoBack,
        )
        SettingsPage.Wallpaper -> WallpaperScreen(
            uiState = uiState,
            onSetWallpaperType = onSetWallpaperType,
            onSetWallpaper = onSetWallpaper,
        )
        SettingsPage.DisplayTimeout -> DisplayTimeoutScreen(
            onOpenDisplaySettings = onOpenDisplaySettings,
        )
        SettingsPage.Notifications -> NotificationsScreen(
            onOpenSystemSettings = onOpenSystemSettings,
        )
        SettingsPage.InputDelay -> InputDelayScreen(
        )
        SettingsPage.ScrollBehavior -> ScrollBehaviorScreen(
            uiState = uiState,
            onSetScrollBehavior = onSetScrollBehavior,
        )
        SettingsPage.NotificationSounds -> NotificationSoundsScreen(
            onOpenSoundSettings = onOpenSoundSettings,
        )
        SettingsPage.UninstallApps -> UninstallAppsScreen(
            onOpenApplicationSettings = onOpenApplicationSettings,
            onGoBack = onGoBack,
        )
        SettingsPage.ScreenReader -> ScreenReaderScreen(
            onOpenAccessibilitySettings = onOpenAccessibilitySettings,
        )
        SettingsPage.VoiceCommands -> VoiceCommandsScreen(
            onOpenAccessibilitySettings = onOpenAccessibilitySettings,
        )
        SettingsPage.HearingAssistant -> HearingAssistantScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.SpeechAssistant -> SpeechAssistantScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.VisualAssistant -> VisualAssistantScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.Licenses -> LicensesScreen(
            onOpenSettingsPage = onOpenSettingsPage,
        )
        SettingsPage.LicensesApache20 -> LicenseApache20Screen()
    }
}

@Composable
fun ColumnScope.SettingsOverviewScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
) {
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(R.string.assistant, icon = Icons.Outlined.SettingsSuggest) {
                onOpenSettingsPage(
                    SettingsPage.Assistant,
                )
            },
            SettingsButtonData(R.string.set_this_launcher_as_default) { onOpenSettingsPage(SettingsPage.SetDefaultLauncher) },
            SettingsButtonData(R.string.open_system_settings) { onOpenSettingsPage(SettingsPage.SystemSettings) },
            SettingsButtonData(R.string.share_launcher) { onOpenSettingsPage(SettingsPage.ShareLauncher) },
            SettingsButtonData(R.string.get_feedback_contact) { onOpenSettingsPage(SettingsPage.Feedback) },
            SettingsButtonData(R.string.uninstall_apps) { onOpenSettingsPage(SettingsPage.UninstallApps) },
            SettingsButtonData(R.string.uninstall_launcher) { onOpenSettingsPage(SettingsPage.UninstallLauncher) },
            SettingsButtonData(R.string.read_licenses) { onOpenSettingsPage(SettingsPage.Licenses) },
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.SettingsAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
) {
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(
                textRes = R.string.assistant_visual,
                secondaryTextRes = R.string.assistant_visual_description,
                icon = Icons.Outlined.Visibility,
            ) {
                onOpenSettingsPage(SettingsPage.VisualAssistant)
            },
            SettingsButtonData(
                textRes = R.string.assistant_hearing,
                secondaryTextRes = R.string.assistant_hearing_description,
                icon = Icons.Outlined.Hearing,
            ) {
                onOpenSettingsPage(SettingsPage.HearingAssistant)
            },
            SettingsButtonData(
                textRes = R.string.assistant_speech,
                secondaryTextRes = R.string.assistant_speech_description,
                icon = Icons.Outlined.Message,
            ) {
                onOpenSettingsPage(SettingsPage.SpeechAssistant)
            },
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.VisualAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
) {
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(R.string.display_scale_icons) {
                onOpenSettingsPage(SettingsPage.IconSize)
            },
            SettingsButtonData(R.string.display_scale_text) {
                onOpenSettingsPage(SettingsPage.TextSize)
            },
            SettingsButtonData(R.string.wallpaper) {
                onOpenSettingsPage(SettingsPage.Wallpaper)
            },
            SettingsButtonData(R.string.display_timeout) {
                onOpenSettingsPage(SettingsPage.DisplayTimeout)
            },
            SettingsButtonData(R.string.notifications) {
                onOpenSettingsPage(SettingsPage.Notifications)
            },
            SettingsButtonData(R.string.input_delay) {
                onOpenSettingsPage(SettingsPage.InputDelay)
            },
            SettingsButtonData(R.string.scroll_behavior) {
                onOpenSettingsPage(SettingsPage.ScrollBehavior)
            },
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.HearingAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
) {
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(R.string.notification_sounds) {
                onOpenSettingsPage(SettingsPage.NotificationSounds)
            },
            SettingsButtonData(R.string.screen_reader) {
                onOpenSettingsPage(SettingsPage.ScreenReader)
            },
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.SpeechAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
) {
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(R.string.voice_commands) {
                onOpenSettingsPage(SettingsPage.VoiceCommands)
            },
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.IconSizeScreen(
    uiState: UiState,
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SetIconSizeComponent(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        uiState = uiState,
        onSetIconSize = onSetIconSize,
    )
    BottomSheetComponent {
        ExtendedFabComponent(
            onClick = onGoBack,
            textRes = R.string.button_set_font_size,
            imageVector = Icons.Outlined.Done,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun ColumnScope.TextSizeScreen(
    uiState: UiState,
    onSetTextSize: (appTextSize: AppTextSize) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SetTextSizeComponent(
        modifier = Modifier
            .fillMaxSize()
            .weight(1f),
        uiState = uiState,
        onSetTextSize = onSetTextSize,
    )
    BottomSheetComponent {
        ExtendedFabComponent(
            onClick = onGoBack,
            textRes = R.string.button_set_font_size,
            imageVector = Icons.Outlined.Done,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun ColumnScope.WallpaperScreen(
    uiState: UiState,
    onSetWallpaperType: (wallpaperType: WallpaperType) -> Unit = {},
    onSetWallpaper: (wallpaperRes: Int) -> Unit = {},
) {
    SettingsFrame {
        val wallpaperType = uiState.settings.wallpaperType
        val showCustomWallpaper = wallpaperType != WallpaperType.SOLID_COLOR

        val images = listOf(R.raw.wp_1, R.raw.wp_2, R.raw.wp_3, R.raw.wp_4, R.raw.wp_5, R.raw.wp_6)
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(128.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Column {
                    SettingSwitchItem(
                        title = stringResource(R.string.enable_custom_wallpaper),
                        checked = showCustomWallpaper,
                        onCheckedChange = { checked ->
                            onSetWallpaperType(if (checked) WallpaperType.CUSTOM_WALLPAPER else WallpaperType.SOLID_COLOR)
                        },
                    )
                    AnimatedVisibility(visible = showCustomWallpaper) {
                        Column {
                            SettingSwitchItem(
                                title = stringResource(R.string.darken_custom_wallpaper),
                                description = stringResource(R.string.darken_custom_wallpaper_description),
                                checked = wallpaperType == WallpaperType.DARKENED_CUSTOM_WALLPAPER,
                                onCheckedChange = { checked ->
                                    val newWallpaperType =
                                        if (checked) WallpaperType.DARKENED_CUSTOM_WALLPAPER else WallpaperType.CUSTOM_WALLPAPER
                                    onSetWallpaperType(newWallpaperType)
                                },
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                                text = stringResource(R.string.tap_to_apply_wallpaper),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
            items(images) { imageRes ->
                AnimatedVisibility(visible = showCustomWallpaper) {
                    WallpaperItem(imageRes = imageRes, onClick = { onSetWallpaper(imageRes) })
                }
            }
        }
    }
}


@Composable
fun WallpaperItem(imageRes: Int, onClick: () -> Unit) {
    val animatedScrimColor by animateColorAsState(
        targetValue = LauncherTheme.all.onWallpaperBackground,
        animationSpec = TweenSpec(durationMillis = 300),
    )
    val cornerRadius = 8.dp
    Box(
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.0f)
                .clip(RoundedCornerShape(cornerRadius))
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRoundRect(
                            color = animatedScrimColor,
                            cornerRadius = CornerRadius(cornerRadius.toPx()),
                        )
                    }
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageRes)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun ColumnScope.DisplayTimeoutScreen(
    onOpenDisplaySettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.display_timeout_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenDisplaySettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.NotificationsScreen(
    onOpenSystemSettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.notifications_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenSystemSettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.InputDelayScreen(
    onOpenSystemSettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.input_delay_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenSystemSettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.NotificationSoundsScreen(
    onOpenSoundSettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.notification_sounds_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenSoundSettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.UninstallAppsScreen(
    onOpenApplicationSettings: () -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.uninstall_apps_description)
        SettingsFab(
            R.string.uninstall_apps,
            Icons.Outlined.Delete,
            onOpenApplicationSettings,
            R.string.uninstall_apps_confirmation,
        )
        SettingsFab(R.string.uninstall_apps_no_button, onClick = onGoBack)
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.ScreenReaderScreen(
    onOpenAccessibilitySettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.screen_reader_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenAccessibilitySettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.VoiceCommandsScreen(
    onOpenAccessibilitySettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.voice_commands_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenAccessibilitySettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.SetDefaultLauncherScreen(
    onSetDefaultLauncher: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.set_this_launcher_as_default_description)
        SettingsFab(
            buttonTextRes = R.string.set_this_launcher_as_default,
            imageVector = Icons.Outlined.Home,
            onClick = {
                onSetDefaultLauncher()
            },
        )
    }
}

@Composable
fun ColumnScope.FeedbackScreen(
    onWriteFeedbackMail: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.get_feedback_contact_description)
        SettingsFab(R.string.write_email, Icons.Outlined.Mail, onWriteFeedbackMail)
    }
}

@Composable
fun ColumnScope.SystemSettingsScreen(
    onOpenSystemSettings: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.open_system_settings_description)
        SettingsFab(R.string.open_system_settings, Icons.Outlined.AppSettingsAlt, onOpenSystemSettings)
    }
}

@Composable
fun ColumnScope.ShareLauncherScreen(
    onShareLauncher: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.share_launcher_description)
        SettingsFab(R.string.share_launcher, Icons.Outlined.Share, onShareLauncher)
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.UninstallLauncherScreen(
    onUninstallLauncher: () -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsFrame {
        SettingsBody(R.string.uninstall_launcher_description)
        SettingsFab(
            buttonTextRes = R.string.uninstall_launcher_button_yes,
            imageVector = Icons.Outlined.Delete,
            onClick = onUninstallLauncher,
            secondaryTextRes = R.string.uninstall_launcher_buttons_description,
        )
        SettingsFab(R.string.uninstall_launcher_button_no, onClick = onGoBack)
    }
}

/**
 * Lists all open source licenses of the used libraries.
 */
@Composable
fun ColumnScope.LicensesScreen(
    onOpenSettingsPage: (SettingsPage) -> Unit = {},
) {
    val openApache20ScreenAction = { onOpenSettingsPage(SettingsPage.LicensesApache20) }
    SettingsFrame {
        val settingsButtonsData = listOf(
            SettingsButtonData(
                textRes = R.string.license_accompanist,
                secondaryTextRes = R.string.license_accompanist_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_aosp,
                secondaryTextRes = R.string.license_aosp_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_androidx,
                secondaryTextRes = R.string.license_androidx_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_coil,
                secondaryTextRes = R.string.license_coil_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_hilt,
                secondaryTextRes = R.string.license_hilt_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_kotlin,
                secondaryTextRes = R.string.license_kotlin_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_launcher_3,
                secondaryTextRes = R.string.license_launcher_3_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_material_components,
                secondaryTextRes = R.string.license_material_components_author,
                onClick = openApache20ScreenAction,
            ),
            SettingsButtonData(
                textRes = R.string.license_timber,
                secondaryTextRes = R.string.license_timber_author,
                onClick = openApache20ScreenAction,
            ),
        )
        LazyColumn(
            modifier = Modifier,
        ) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    text = stringResource(R.string.read_licenses_description),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            itemsIndexed(items = settingsButtonsData) { _, settingsButton ->
                SettingsButton(settingsButton)
            }
        }
    }
}

/**
 * Shows the Apache 2.0 license text.
 */
@Composable
fun ColumnScope.LicenseApache20Screen() {
    SettingsFrame {
        ScrollableHtmlText(
            modifier = Modifier
                .weight(1f),
            text = stringResource(R.string.license_apache_20),
        )
    }
}

data class SettingsButtonData(
    @StringRes val textRes: Int,
    @StringRes val secondaryTextRes: Int? = null,
    val icon: ImageVector? = null,
    val descriptionRes: Int? = null,
    val onClick: () -> Unit,
)

@Composable
fun ColumnScope.SettingsBody(textRes: Int) {
    ScrollableColumn(
        modifier = Modifier
            .weight(1f, fill = false)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(textRes),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun ColumnScope.SettingsFrame(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier
            .padding(16.dp)
            .weight(1f),
        content = content,
    )
}

@Composable
fun SettingsFab(
    buttonTextRes: Int,
    imageVector: ImageVector? = null,
    onClick: () -> Unit,
    secondaryTextRes: Int? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        if (secondaryTextRes != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center, text = stringResource(secondaryTextRes),
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            ExtendedFloatingActionButton(
                onClick = onClick,
                text = {
                    Text(
                        text = stringResource(buttonTextRes),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.offset((-8).dp),
                    )
                },
                icon = {
                    if (imageVector != null) {
                        Icon(
                            imageVector = imageVector,
                            contentDescription = null,
                        )
                    }
                },
            )
        }
    }
}

@Composable
fun SettingsButtonList(settingsButtonsData: List<SettingsButtonData>) {
    LazyColumn(
        modifier = Modifier,
    ) {
        itemsIndexed(items = settingsButtonsData) { _, settingsButton ->
            SettingsButton(settingsButton)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsButton(settingsButton: SettingsButtonData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        onClick = settingsButton.onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val icon = settingsButton.icon
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
                    imageVector = icon,
                    contentDescription = null,
                )
            }
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .weight(1f)
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.labelLarge,
                    text = stringResource(settingsButton.textRes),
                )
                val secondaryTextRes = settingsButton.secondaryTextRes
                if (secondaryTextRes != null) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        text = stringResource(secondaryTextRes),
                    )
                }
            }
            Icon(
                modifier = Modifier
                    .padding(16.dp),
                imageVector = Icons.Outlined.KeyboardArrowRight,
                contentDescription = null,
            )
        }
    }
}

/**
 * Displays a link to our wiki and explains that here, users can find more information about the setting.
 */
@Composable
fun SettingsInfoCard() {
    //TODO: Reimplement once we have a wiki
//    BottomSheetComponent {
//        Text(
//            modifier = Modifier
//                .fillMaxWidth(),
//            text = stringResource(R.string.settings_info),
//            textAlign = TextAlign.Center,
//        )
//    }
}

@Preview(
    showBackground = true,
)
@Composable
fun SettingsScreenPreview() {
    LauncherTheme {
        SettingsScreen(uiState = UiState(ScreenState.SettingsState(SettingsPage.Overview)))
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun SettingsAssistantScreen() {
    LauncherTheme {
        SettingsAssistantScreen()
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun SetDefaultLauncherScreen() {
    LauncherTheme {
        SetDefaultLauncherScreen()
    }
}
