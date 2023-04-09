package org.tuerantuer.launcher.ui.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.ui.ScreenState
import org.tuerantuer.launcher.ui.UiState
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
    onUninstallLauncher: () -> Unit = {},
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.settings),
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
        )
        val settingsButtons = listOf(
            SettingsButton(R.string.assistant) { },
            SettingsButton(R.string.set_this_launcher_as_default, onSetDefaultLauncher),
            SettingsButton(R.string.open_system_settings, onOpenSystemSettings),
            SettingsButton(R.string.share_launcher, onShareLauncher),
            SettingsButton(R.string.get_feedback_contact) { },
            SettingsButton(R.string.uninstall_apps) { },
            SettingsButton(R.string.uninstall_launcher, onUninstallLauncher),
        )
        LazyColumn(
            modifier = Modifier,
        ) {
            itemsIndexed(items = settingsButtons) { _, settingsButton ->
                Button(onClick = settingsButton.onClick) {
                    Text(text = stringResource(settingsButton.textRes))
                }
            }
        }
    }
}

data class SettingsButton(@StringRes val textRes: Int, val onClick: () -> Unit)

@Preview(
    showBackground = true,
)
@Composable
fun SettingsScreenPreview() {
    LauncherTheme {
        SettingsScreen(uiState = UiState(ScreenState.SettingsState))
    }
}
