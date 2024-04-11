package org.tuerantuer.launcher.ui.screen

import android.text.format.DateFormat
import android.widget.TextClock
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.ui.components.HomeScreenItemComponent
import org.tuerantuer.launcher.ui.components.ScrollScrimComponent
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.ButtonHomeScreenItem
import org.tuerantuer.launcher.ui.data.HomeScreenItem
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import org.tuerantuer.launcher.util.extension.setShadow
import timber.log.Timber
import java.util.Locale

private const val DATE_PATTERN = "EEEE, MMMd"
private const val TIME_PATTERN = "HH:mm"

/**
 * The main screen of the launcher. Here, the user can see the clock, the favorites and can access other screens.
 * If the grid content exceeds the visible screen space, scroll buttons are shown.
 */
@Composable
fun HomeScreen(
    uiState: UiState,
    onShowAllApps: () -> Unit,
    onOpenSettings: () -> Unit,
    onEditFavorites: () -> Unit,
    onShowOnboarding: () -> Unit,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit,
    gridState: LazyGridState,
    coroutineScope: CoroutineScope,
) {
    val showScrollButtonState = remember {
        Timber.d("Carikom: remember: showScrollButtonState")
        mutableStateOf(false)
    } // false
    val scrollButtonsOpacity by animateFloatAsState (
        targetValue = if (showScrollButtonState.value) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "",
    )
    // todo hallo zukunfts ich, warum 2 compositionen? jetzt dein problem
    Timber.d("Carikom: showScrollButtonState: ${showScrollButtonState.value}")
    LaunchedEffect(key1 = uiState.allApps.size) {
        Timber.d("Carikom: LaunchedEffect: uiState.allApps.size: ${uiState.allApps.size}" )
        val totalItemsCount = gridState.layoutInfo.totalItemsCount
        Timber.d("Carikom: totalItemsCount: $totalItemsCount" )
        val visibleItemsCount = gridState.layoutInfo.visibleItemsInfo.size
        Timber.d("Carikom: visibleItemsCount: $visibleItemsCount" )
        val shouldShow = totalItemsCount > visibleItemsCount
        if (shouldShow != showScrollButtonState.value) { // as long as there are less items than visible, this will not be called
//            showScrollButtonState.value = shouldShow
            Timber.d("Carikom: updated state: ${showScrollButtonState.value}")
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LauncherTheme.all.onWallpaperBackground),
    ) {
        Box(
            modifier = Modifier
                .weight(1f),
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
            val appIconSize = uiState.settings.appIconSize.sizeDp.dp
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = appIconSize),
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
                        iconSize = appIconSize,
                    )
                }
            }

            ScrollScrimComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .alpha(scrollButtonsOpacity),
            )
        }

        if (showScrollButtonState.value) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background) // todo
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(vertical = 16.dp)
                    .alpha(scrollButtonsOpacity),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val scrollDistance = 1000.dp.value
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        gridState.animateScrollBy(
                            -scrollDistance,
                            tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                        )
                    }
                }) {
                    Icon(
                        Icons.Filled.ArrowUpward,
                        contentDescription = "up",
                    )
                }
                Spacer(modifier = Modifier.width(50.dp))
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        gridState.animateScrollBy(
                            value = scrollDistance,
                            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                        )
                    }
                }) {
                    Icon(
                        Icons.Filled.ArrowDownward,
                        contentDescription = "down",
                    )
                }
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
            gridState = LazyGridState(),
            coroutineScope = CoroutineScope(GlobalScope.coroutineContext),
        )
    }
}
