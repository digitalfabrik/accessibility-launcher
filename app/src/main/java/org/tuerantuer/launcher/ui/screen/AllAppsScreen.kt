package org.tuerantuer.launcher.ui.screen

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.ui.components.HeaderComponent
import org.tuerantuer.launcher.ui.components.HomeScreenItemComponent
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.ui.components.ScrollScrimComponent

/**
 * The screen that shows all installed apps.
 */
@Composable
fun AllAppsScreen(
    uiState: UiState,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit = {},
    onGoBack: () -> Unit = {},
    coroutineScope: CoroutineScope,
) {
    val gridState = rememberLazyGridState()
    // this next line sets the initial scroll button state to false, meaning that the scroll buttons are not visible
    val showScrollButtonState = remember { mutableStateOf(false) }

    val scrollButtonsOpacity by animateFloatAsState (
        targetValue = if (showScrollButtonState.value) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(key1 = uiState.allApps.size) {
        val totalItemsCount = gridState.layoutInfo.totalItemsCount
        val visibleItemsCount = gridState.layoutInfo.visibleItemsInfo.size
        val shouldShow = totalItemsCount > visibleItemsCount
        if (shouldShow != showScrollButtonState.value) {
            showScrollButtonState.value = shouldShow
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LauncherTheme.all.onWallpaperBackground), //todo only use this when there is no wallpaper?
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeaderComponent(
                text = stringResource(R.string.all_apps),
                onGoBack = onGoBack,
            )
            val homeScreenItems =
                uiState.allApps.map { appItemInfo ->
                    AppHomeScreenItem(
                        appItemInfo,
                        onClick = { onOpenApp(appItemInfo) })
                }
            val appIconSize = uiState.settings.appIconSize.sizeDp.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = appIconSize),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items(
                        items = homeScreenItems,
                        key = { homeScreenItems -> homeScreenItems.key },
                    ) { homeScreenItem ->
                        HomeScreenItemComponent(
                            homeScreenItem = homeScreenItem,
                            iconSize = appIconSize,
                        )
                    }
                }
                ScrollScrimComponent(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .alpha(scrollButtonsOpacity)
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent, // This makes the start of the gradient fully transparent
//                                    Color.Transparent,
//                                ),
//                            ),
//                        ),

                )
            }
            if (showScrollButtonState.value) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
//                        .background(Color(0f, 0f, 0f, 0.1f)) // todo only use this when there is no wallpaper?
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 16.dp),
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
                            contentDescription = stringResource(id = R.string.button_scroll_up),
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
                            contentDescription = stringResource(id = R.string.button_scroll_down),
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun AllAppsScreenPreview() {
    LauncherTheme {
        AllAppsScreen(
            uiState = UiState(ScreenState.AllAppsScreenState),
            coroutineScope = CoroutineScope(GlobalScope.coroutineContext), // todo change?
        )
    }
}
