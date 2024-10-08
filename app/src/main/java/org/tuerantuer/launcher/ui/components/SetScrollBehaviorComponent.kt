package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RadioButtonChecked
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.ui.data.UiState

@Composable
fun ScrollBehaviorScreen(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onSetScrollBehavior: (useButtons: Boolean) -> Unit = {},
) {
    val gestureIcon = if (uiState.settings.useScrollButtons) {
        Icons.Outlined.RadioButtonUnchecked
    } else {
        Icons.Outlined.RadioButtonChecked
    }
    val buttonIcon = if (uiState.settings.useScrollButtons) {
        Icons.Outlined.RadioButtonChecked
    } else {
        Icons.Outlined.RadioButtonUnchecked
    }

    ScrollableColumn(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 8.dp)
            .fillMaxSize(),
    ) {

        ScrollTextTitle(R.string.scroll_behavior_title, modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScrollTextTitle(R.string.scroll_gesture)
            ExtendedFabComponent(
                onClick = {
                    onSetScrollBehavior(false)
                },
                textRes = null,
                imageVector = gestureIcon,
                shape = RoundedCornerShape(32.dp),
                foregroundColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        }
        ScrollTextBody(R.string.scroll_gesture_description)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ScrollTextTitle(R.string.scroll_buttons)
            ExtendedFabComponent(
                onClick = {
                    onSetScrollBehavior(true)
                },
                textRes = null,
                imageVector = buttonIcon,
                shape = RoundedCornerShape(32.dp),
                foregroundColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        }
        ScrollTextBody(R.string.scroll_buttons_description)
    }
}

@Composable
fun ScrollTextBody(textRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(textRes),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@Composable
fun ScrollTextTitle(textRes: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .wrapContentHeight(),
    ) {
        Text(
            modifier = Modifier,
            text = stringResource(textRes),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}