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
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.AppHomeScreenItem
import org.tuerantuer.launcher.ui.ButtonHomeScreenItem
import org.tuerantuer.launcher.ui.HomeScreenItem
import org.tuerantuer.launcher.ui.ScreenState
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.components.HomeScreenItemComponent
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
        val homeScreenItems = mutableListOf<HomeScreenItem>().apply {
            uiState.favorites.mapTo(this) { appItemInfo ->
                AppHomeScreenItem(appItemInfo, onClick = { onOpenApp(appItemInfo) })
            }
            val context = LocalContext.current
            add(
                ButtonHomeScreenItem(
                    nameRes = R.string.all_apps,
                    innerIconRes = R.drawable.baseline_apps_24,
                    context = context,
                    onClick = onShowAllApps,
                ),
            )
            add(
                ButtonHomeScreenItem(
                    nameRes = R.string.change_favorites,
                    innerIconRes = R.drawable.outline_interests_24,
                    context = context,
                    onClick = onEditFavorites,
                ),
            )
            add(
                ButtonHomeScreenItem(
                    nameRes = R.string.setup_assistant,
                    innerIconRes = R.drawable.outline_help_outline_24,
                    context = context,
                    onClick = onShowOnboarding,
                ),
            )
            add(
                ButtonHomeScreenItem(
                    nameRes = R.string.settings,
                    innerIconRes = R.drawable.outline_settings_24,
                    context = context,
                    onClick = onOpenSettings,
                ),
            )
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 86.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            item(span = { GridItemSpan(Int.MAX_VALUE) }) {
                Clock()
            }
            items(
                items = homeScreenItems,
                key = { homeScreenItem -> homeScreenItem.key },
            ) { homeScreenItem ->
                HomeScreenItemComponent(
                    homeScreenItem = homeScreenItem,
                    iconSize = uiState.settings.appIconSize,
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
