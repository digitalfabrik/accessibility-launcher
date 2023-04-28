package org.tuerantuer.launcher.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.ui.components.BottomSheetComponent
import org.tuerantuer.launcher.ui.components.ExtendedFabComponent
import org.tuerantuer.launcher.ui.components.HeaderComponent
import org.tuerantuer.launcher.ui.components.ScrollableColumn
import org.tuerantuer.launcher.ui.components.SetIconSizeComponent
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.SettingsPage
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * The screen where the user can change the launcher settings.
 *
 * @author Peter Huber
 * Created on 27/03/2023
 */
@Composable
fun SettingsScreen(
    uiState: UiState,
    onSetDefaultLauncher: () -> Unit = {},
    onShareLauncher: () -> Unit = {},
    onOpenSystemSettings: () -> Unit = {},
    onOpenAccessibilitySettings: () -> Unit = {},
    onOpenDisplaySettings: () -> Unit = {},
    onOpenNotificationSettings: () -> Unit = {},
    onOpenSoundSettings: () -> Unit = {},
    onUninstallLauncher: () -> Unit = {},
    onWriteFeedbackMail: () -> Unit = {},
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    val screenState = uiState.screenState
    require(screenState is ScreenState.SettingsState)
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        when (screenState.settingsPage) {
            SettingsPage.Overview -> SettingsOverviewScreen(
                onOpenSettingsPage = onOpenSettingsPage,
                onGoBack = onGoBack,
            )
            SettingsPage.Assistant -> SettingsAssistantScreen(
                onOpenSettingsPage = onOpenSettingsPage,
                onGoBack = onGoBack,
            )
            SettingsPage.SetDefaultLauncher -> SetDefaultLauncherScreen(
                onSetDefaultLauncher = onSetDefaultLauncher,
                onGoBack = onGoBack,
            )
            SettingsPage.ShareLauncher -> ShareLauncherScreen(
                onShareLauncher = onShareLauncher,
                onGoBack = onGoBack,
            )
            SettingsPage.Feedback -> FeedbackScreen(
                onGoBack = onGoBack,
                onWriteFeedbackMail = onWriteFeedbackMail,
            )
            SettingsPage.SystemSettings -> SystemSettingsScreen(
                onOpenSystemSettings = onOpenSystemSettings,
                onGoBack = onGoBack,
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
            SettingsPage.Wallpaper -> WallpaperScreen(
                onGoBack = onGoBack,
            )
            SettingsPage.DisplayTimeout -> DisplayTimeoutScreen(
                onGoBack = onGoBack,
                onOpenDisplaySettings = onOpenDisplaySettings,
            )
            SettingsPage.Notifications -> NotificationsScreen(
                onGoBack = onGoBack,
                onOpenNotificationSettings = onOpenNotificationSettings,
            )
            SettingsPage.InputDelay -> InputDelayScreen(
                onGoBack = onGoBack,
            )
            SettingsPage.NotificationSounds -> NotificationSoundsScreen(
                onGoBack = onGoBack,
                onOpenSoundSettings = onOpenSoundSettings,
            )
            SettingsPage.ScreenReader -> ScreenReaderScreen(
                onGoBack = onGoBack,
                onOpenAccessibilitySettings = onOpenAccessibilitySettings,
            )
            SettingsPage.VoiceCommands -> VoiceCommandsScreen(
                onGoBack = onGoBack,
                onOpenAccessibilitySettings = onOpenAccessibilitySettings,
            )
            SettingsPage.HearingAssistant -> HearingAssistantScreen(
                onGoBack = onGoBack,
                onOpenSettingsPage = onOpenSettingsPage,
            )
            SettingsPage.SpeechAssistant -> SpeechAssistantScreen(
                onGoBack = onGoBack,
                onOpenSettingsPage = onOpenSettingsPage,
            )
            SettingsPage.VisualAssistant -> VisualAssistantScreen(
                onGoBack = onGoBack,
                onOpenSettingsPage = onOpenSettingsPage,
            )
            SettingsPage.UninstallApps -> {}
        }
    }
}

@Composable
fun ColumnScope.SettingsOverviewScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.settings, onGoBack)
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
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.SettingsAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.settings, onGoBack)
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
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.assistant_visual, onGoBack)
    SettingsFrame {
        val settingsButtons = listOf(
            SettingsButtonData(R.string.display_scale) {
                onOpenSettingsPage(SettingsPage.IconSize)
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
        )
        SettingsButtonList(settingsButtons)
    }
}

@Composable
fun ColumnScope.HearingAssistantScreen(
    onOpenSettingsPage: (settingsPage: SettingsPage) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.assistant_hearing, onGoBack)
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
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.assistant_speech, onGoBack)
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
    SettingsHeader(R.string.display_scale, onGoBack)
    SetIconSizeComponent(
        modifier = Modifier.fillMaxSize().weight(1f),
        uiState = uiState,
        onSetIconSize = onSetIconSize,
    )
    BottomSheetComponent {
        ExtendedFabComponent(
            onClick = onGoBack,
            textRes = R.string.button_set_size,
            imageVector = Icons.Outlined.Done,
        )
        Text(
            text = stringResource(R.string.settings_info_short),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun ColumnScope.WallpaperScreen(
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.wallpaper, onGoBack)
    SettingsFrame {
    }
}

@Composable
fun ColumnScope.DisplayTimeoutScreen(
    onGoBack: () -> Unit = {},
    onOpenDisplaySettings: () -> Unit = {},
) {
    SettingsHeader(R.string.display_timeout, onGoBack)
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
    onGoBack: () -> Unit = {},
    onOpenNotificationSettings: () -> Unit = {},
) {
    SettingsHeader(R.string.notifications, onGoBack)
    SettingsFrame {
        SettingsBody(R.string.notifications_description)
        SettingsFab(
            R.string.system_settings,
            Icons.Outlined.AppSettingsAlt,
            onOpenNotificationSettings,
            R.string.system_settings_description,
        )
    }
    SettingsInfoCard()
}

@Composable
fun ColumnScope.InputDelayScreen(
    onGoBack: () -> Unit = {},
    onOpenSystemSettings: () -> Unit = {},
) {
    SettingsHeader(R.string.input_delay, onGoBack)
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
    onGoBack: () -> Unit = {},
    onOpenSoundSettings: () -> Unit = {},
) {
    SettingsHeader(R.string.notification_sounds, onGoBack)
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
fun ColumnScope.ScreenReaderScreen(
    onGoBack: () -> Unit = {},
    onOpenAccessibilitySettings: () -> Unit = {},
) {
    SettingsHeader(R.string.screen_reader, onGoBack)
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
    onGoBack: () -> Unit = {},
    onOpenAccessibilitySettings: () -> Unit = {},
) {
    SettingsHeader(R.string.voice_commands, onGoBack)
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
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.set_this_launcher_as_default, onGoBack)
    SettingsFrame {
        SettingsBody(R.string.set_this_launcher_as_default_description)
        SettingsFab(R.string.set_this_launcher_as_default, Icons.Outlined.Home, onSetDefaultLauncher)
    }
}

@Composable
fun ColumnScope.FeedbackScreen(
    onGoBack: () -> Unit = {},
    onWriteFeedbackMail: () -> Unit = {},
) {
    SettingsHeader(R.string.get_feedback_contact, onGoBack)
    SettingsFrame {
        SettingsBody(R.string.get_feedback_contact_description)
        SettingsFab(R.string.write_email, Icons.Outlined.Mail, onWriteFeedbackMail)
    }
}

@Composable
fun ColumnScope.SystemSettingsScreen(
    onOpenSystemSettings: () -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.open_system_settings, onGoBack)
    SettingsFrame {
        SettingsBody(R.string.open_system_settings_description)
        SettingsFab(R.string.open_system_settings, Icons.Outlined.AppSettingsAlt, onOpenSystemSettings)
    }
}

@Composable
fun ColumnScope.ShareLauncherScreen(
    onShareLauncher: () -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    SettingsHeader(R.string.share_launcher, onGoBack)
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
    SettingsHeader(R.string.uninstall_launcher, onGoBack)
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

data class SettingsButtonData(
    @StringRes val textRes: Int,
    @StringRes val secondaryTextRes: Int? = null,
    val icon: ImageVector? = null,
    val descriptionRes: Int? = null,
    val onClick: () -> Unit,
)

@Composable
fun SettingsHeader(textRes: Int, onGoBack: () -> Unit) {
    HeaderComponent(
        modifier = Modifier.padding(bottom = 16.dp),
        text = stringResource(textRes),
        onGoBack = onGoBack,
    )
}

@Composable
fun ColumnScope.SettingsBody(textRes: Int) {
    ScrollableColumn(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            text = stringResource(textRes),
            textAlign = TextAlign.Center,
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
                    Text(text = stringResource(buttonTextRes))
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
fun SettingsButtonList(settingsButtons: List<SettingsButtonData>) {
    LazyColumn(
        modifier = Modifier,
    ) {
        itemsIndexed(items = settingsButtons) { _, settingsButton ->
            SettingsButton(settingsButton)
        }
    }
}

@Composable
fun SettingsButton(settingsButton: SettingsButtonData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = settingsButton.onClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val icon = settingsButton.icon
            if (icon != null) {
                Icon(
                    modifier = Modifier
                        .padding(16.dp),
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
                    style = MaterialTheme.typography.labelLarge,
                    text = stringResource(settingsButton.textRes),
                )
                val secondaryTextRes = settingsButton.secondaryTextRes
                if (secondaryTextRes != null) {
                    Text(
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

@Composable
fun SettingsInfoCard() {
    BottomSheetComponent {
        Text(
            text = stringResource(R.string.settings_info),
            textAlign = TextAlign.Center,
        )
    }
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
