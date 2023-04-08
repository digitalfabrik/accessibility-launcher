package org.tuerantuer.launcher.ui.screen

import android.text.format.DateFormat
import android.widget.TextClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.ScreenState
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.components.AppItem
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import java.util.*

private const val DATE_PATTERN = "EEEE, MMMd"
private const val TIME_PATTERN = "HH:mm"

/**
 * The main screen of the launcher. Here, the user can see the clock, the favorites and can access other screens.
 *
 * @author Peter Huber
 * Created on 06/03/2023
 */
@Composable
fun HomeScreen(
    uiState: UiState,
    onShowAllApps: () -> Unit,
    onOpenSettings: () -> Unit,
    onEditFavorites: () -> Unit,
    onShowOnboarding: () -> Unit,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
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
                AppItem(
                    appItemInfo = appItemInfo,
                    onClick = { onOpenApp(appItemInfo) },
                    appIconSize = uiState.settings.appIconSize,
                )
            }
        }
    }
}

@Composable
fun Clock() {
    val defaultLocale = Locale.getDefault()
    Column(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val textColor = MaterialTheme.colorScheme.onBackground.toArgb()
        Spacer(modifier = Modifier.height(32.dp))
        val textSizeDate = 18.sp
        val textSizeTime = 72.dp
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    val format = getTimeFormat(defaultLocale)
                    format12Hour = format
                    format24Hour = format
                    textSize = textSizeTime.value
                    setTextColor(textColor)
                }
            },
        )
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    val format = getDateFormat(defaultLocale)
                    format12Hour = format
                    format24Hour = format
                    textSize = textSizeDate.value
                    setTextColor(textColor)
                }
            },
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

fun getDateFormat(locale: Locale): String {
    return DateFormat.getBestDateTimePattern(locale, DATE_PATTERN)
}

fun getTimeFormat(locale: Locale): String {
    return DateFormat.getBestDateTimePattern(locale, TIME_PATTERN)
}

@Preview(
    showBackground = true,
)
@Composable
fun HomeScreenPreview() {
    LauncherTheme {
        HomeScreen(
            uiState = UiState(ScreenState.HomeScreen),
            onShowAllApps = { },
            onOpenSettings = { },
            onEditFavorites = { },
            onShowOnboarding = { },
            onOpenApp = { },
        )
    }
}
