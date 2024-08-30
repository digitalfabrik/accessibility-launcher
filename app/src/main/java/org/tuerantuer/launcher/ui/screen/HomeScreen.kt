package org.tuerantuer.launcher.ui.screen

import android.text.format.DateFormat
import android.widget.TextClock
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.ui.components.HomeScreenItemComponent
import org.tuerantuer.launcher.ui.components.ScrollButtonComponent
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.ButtonHomeScreenItem
import org.tuerantuer.launcher.ui.data.HomeScreenItem
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import org.tuerantuer.launcher.util.extension.setScrollingEnabled
import org.tuerantuer.launcher.util.extension.setShadow
import java.util.Locale

private const val DATE_PATTERN = "EEEE, MMMd"
private const val TIME_PATTERN = "HH:mm"

/**
 * The main screen of the launcher. Here, the user can see the clock, the favorites and can access other screens.
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
    val scrollState = rememberLazyGridState()
    val gestureScrollingEnabled = !uiState.settings.useScrollButtons
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LauncherTheme.all.onWallpaperBackground),
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
                    iconColor = Color.White,
                    backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                    borderColor = MaterialTheme.colorScheme.tertiaryContainer,
                ),
            )
        }
        val additionalItems = listOf(
            ButtonHomeScreenItem.createDefault(
                nameRes = R.string.change_favorites,
                innerIconRes = R.drawable.outline_interests_24,
                context = LocalContext.current,
                onClick = onEditFavorites,
            ),
            ButtonHomeScreenItem.createDefault(
                nameRes = R.string.setup_assistant,
                innerIconRes = R.drawable.outline_help_outline_24,
                context = LocalContext.current,
                onClick = onShowOnboarding,
            ),
            ButtonHomeScreenItem.createDefault(
                nameRes = R.string.settings,
                innerIconRes = R.drawable.outline_settings_24,
                context = LocalContext.current,
                onClick = onOpenSettings,
            ),
        )

        val appIconSize = uiState.settings.appIconSize.sizeDp.dp
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = appIconSize),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .setScrollingEnabled(gestureScrollingEnabled),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Clock()
            }
            items(
                items = homeScreenItems,
                key = { homeScreenItem -> homeScreenItem.key },
            ) { homeScreenItem ->
                HomeScreenItemComponent(
                    homeScreenItem = homeScreenItem,
                    iconSize = appIconSize,
                )
            }
            // Divider between favorites and additional items
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(3.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(3.dp),
                        ),
                )
            }
            items(
                items = additionalItems,
                key = { homeScreenItem -> homeScreenItem.key },
            ) { homeScreenItem ->
                HomeScreenItemComponent(
                    homeScreenItem = homeScreenItem,
                    iconSize = appIconSize,
                )
            }
        }
        if (uiState.settings.useScrollButtons) {
                ScrollButtonComponent(
                    scrollState = scrollState,
                    coroutineScope = coroutineScope,
                )
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
        val text = LauncherTheme.all.onWallpaperText
        val textColor = text.color.toArgb()
        val shadow = text.shadow
        Spacer(modifier = Modifier.height(32.dp))
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    val format = getTimeFormat(defaultLocale)
                    format12Hour = format
                    format24Hour = format
                    // Use dp instead of sp, because the clock should not be scaled with the system font size setting
                    // (it's always big enough to be readable)
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, 72f)
                    setTextColor(textColor)
                    if (shadow != null) {
                        setShadow(shadow)
                    }
                }
            },
        )
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    val format = getDateFormat(defaultLocale)
                    format12Hour = format
                    format24Hour = format
                    val fontSize = text.fontSize
                    require(fontSize.isSp) {
                        "If you change the unit type of the font, you must also change the line below "
                    }
                    setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, fontSize.value)
                    setTextColor(textColor)
                    if (shadow != null) {
                        setShadow(shadow)
                    }
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
            uiState = UiState(ScreenState.HomeScreenState),
            onShowAllApps = { },
            onOpenSettings = { },
            onEditFavorites = { },
            onShowOnboarding = { },
            onOpenApp = { },
        )
    }
}
