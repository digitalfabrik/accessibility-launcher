package org.tuerantuer.launcher.ui.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.ui.UiState

/**
 * The screen where the user can set up the launcher. The screen shows up the first time the launcher is started.
 *
 * @author Peter Huber
 * Created on 07/03/2023
 */
@Composable
fun OnboardingScreen(uiState: UiState.Onboarding) {
    Text(
        text = stringResource(R.string.setup_assistant),
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 24.sp,
    )
}
