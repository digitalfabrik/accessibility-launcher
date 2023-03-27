package org.tuerantuer.launcher.ui.screen

import android.text.format.DateFormat
import android.widget.TextClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.components.AppItem
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import java.util.*

/**
 * TODO: add description
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
@Composable
fun HomeScreen(
    uiState: UiState.HomeScreen,
    onShowAllApps: () -> Unit,
    onOpenSettings: () -> Unit,
    onEditFavorites: () -> Unit,
    onShowOnboarding: () -> Unit,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Clock()
        Button(onClick = onShowOnboarding) {
            Text(text = stringResource(id = R.string.setup_assistant))
        }
        Button(onClick = onOpenSettings) {
            Text(text = stringResource(id = R.string.settings))
        }
        Button(onClick = onEditFavorites) {
            Text(text = stringResource(id = R.string.change_favorites))
        }
        Button(onClick = onShowAllApps) {
            Text(text = stringResource(id = R.string.all_apps))
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 86.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        ) {
            items(
                items = uiState.favorites,
                key = { appItemInfo -> appItemInfo.key },
            ) { appItemInfo ->
                AppItem(appItemInfo, onClick = { onOpenApp(appItemInfo) })
            }
        }
    }
}

@Composable
fun Clock() {
    Column(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    val format = getClockFormat()
                    format12Hour = format
                    format24Hour = format
                    textSize = 30f
                    setTextColor(android.graphics.Color.WHITE)
                }
            },
            modifier = Modifier.padding(5.dp),
        )
    }
}

fun getClockFormat(): String {
    val defaultLocale = Locale.getDefault()
//    is24HourFormat = DateFormat.is24HourFormat(context)
//    val skeletonWithoutAmPm = when {
//        is24HourFormat -> TIME_PATTERN_24_H_WITHOUT_AM_PM
//        else -> TIME_PATTERN_12_H_WITHOUT_AM_PM
//    }
    val skeletonWithoutAmPm = "HH:mm"
    val timePattern = DateFormat.getBestDateTimePattern(defaultLocale, skeletonWithoutAmPm)
//    val timePatternWithoutAmPm = timePattern
//        .replace(TIME_PATTERN_AM_PM, "") // remove AM/PM period indicator.
//        .replace(" ", "")
    val DATE_PATTERN = "EEE, MMMd"
    val shortDatePattern = DateFormat.getBestDateTimePattern(defaultLocale, DATE_PATTERN)
    return timePattern + "\n" + shortDatePattern
}

@Preview(
    showBackground = true,
)
@Composable
fun HomeScreenPreview() {
    LauncherTheme {
        HomeScreen(
            uiState = UiState.HomeScreen(favorites = emptyList()),
            onShowAllApps = { },
            onOpenSettings = { },
            onEditFavorites = { },
            onShowOnboarding = { },
            onOpenApp = { },
        )
    }
}
