package org.tuerantuer.launcher.ui.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.data.datastore.AppIconSize
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.UiState

/**
 * A component that allows the user to change the size of the app icons and see the result in a live preview.
 *
 * @author Peter Huber
 * Created on 24/04/2023
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
                    )
                }
            }

            // Add a fade-out scrim at the bottom of the app list.
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background),
                        ),
                    ),
            )
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            val allAppIconSizes = AppIconSize.values()
            val currentAppIconSize = uiState.settings.appIconSize
            val currentAppIconSizeIndex = allAppIconSizes.indexOf(currentAppIconSize)
            val smallerIconSize = allAppIconSizes.getOrNull(currentAppIconSizeIndex - 1)
            val largerIconSize = allAppIconSizes.getOrNull(currentAppIconSizeIndex + 1)
            if (smallerIconSize != null) {
                ExtendedFabComponent(
                    modifier = Modifier.padding(start = 0.dp, bottom = 16.dp),
                    onClick = { onSetIconSize(smallerIconSize) },
                    textRes = R.string.button_decrease_size,
                    imageVector = Icons.Filled.ZoomOut,
                )
            }
            if (largerIconSize != null) {
                ExtendedFabComponent(
                    modifier = Modifier.padding(end = 0.dp, bottom = 16.dp),
                    onClick = { onSetIconSize(largerIconSize) },
                    textRes = R.string.button_increase_size,
                    imageVector = Icons.Filled.ZoomIn,
                )
            }
        }
    }
}
