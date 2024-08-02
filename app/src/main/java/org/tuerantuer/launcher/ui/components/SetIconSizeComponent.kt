package org.tuerantuer.launcher.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * A component that allows the user to change the size of the app icons and see the result in a live preview.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetIconSizeComponent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onSetIconSize: (appIconSize: AppIconSize) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(LauncherTheme.all.onWallpaperBackground)
                .weight(1f),
        ) {
            val homeScreenItems =
                uiState.allApps.map { appItemInfo -> AppHomeScreenItem(appItemInfo, onClick = { }) }
            val appIconSize = uiState.settings.appIconSize.sizeDp.dp
            val animatedAppIconSize by animateDpAsState(
                targetValue = appIconSize,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing,
                ),
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = appIconSize),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            ) {
                items(
                    items = homeScreenItems,
                    key = { homeScreenItems -> homeScreenItems.key },
                ) { homeScreenItem ->
                    HomeScreenItemComponent(
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing,
                            ),
                        ),
                        homeScreenItem = homeScreenItem,
                        iconSize = animatedAppIconSize,
                        useOnWallpaperStyle = true,
                    )
                }
            }

            // Add a fade-out scrim at the bottom of the app list.
            ScrollScrimComponent(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            )
        }
        ScalingButtonComponent(
            currentSize = uiState.settings.appIconSize,
            onSetSize = onSetIconSize,
        )
    }
}
