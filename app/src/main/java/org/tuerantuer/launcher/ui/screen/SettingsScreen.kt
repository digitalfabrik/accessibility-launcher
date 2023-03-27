package org.tuerantuer.launcher.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.tuerantuer.launcher.R
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
    uiState: UiState.Settings,
    onSetDefaultLauncher: () -> Unit,
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
        Button(onClick = onSetDefaultLauncher) {
            Text(text = stringResource(id = R.string.change_default_launcher))
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun SettingsScreenPreview() {
    LauncherTheme {
        SettingsScreen(
            uiState = UiState.Settings,
            onSetDefaultLauncher = { },
        )
    }
}
