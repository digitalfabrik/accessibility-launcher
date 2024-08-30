package org.tuerantuer.launcher.ui.components

import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * A static bottom sheet component that can be used to display content inside.
 */
@Composable
fun ScrollButtonComponent(
    modifier: Modifier = Modifier,
    scrollState: LazyGridState,
    coroutineScope: CoroutineScope,
) {
    val backgroundColor = LauncherTheme.all.onWallpaperBackground

    val scrollAmountInPx = with(LocalDensity.current) {
        (LocalConfiguration.current.screenHeightDp.dp.toPx() / 2)
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shadowElevation = if (backgroundColor.alpha != 0f) 8.dp else 0.dp,
        // make it a shade more grayer / darker  than the regular surface
        color= backgroundColor,
        // only round top corners
        shape = MaterialTheme.shapes.medium.copy(
            bottomEnd = CornerSize(0f),
            bottomStart = CornerSize(0f),
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ExtendedFabComponent(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollBy(
                                scrollAmountInPx,
                                tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                            )
                        }
                    },
                    textRes = R.string.scroll_down,
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(32.dp),
                )
                ExtendedFabComponent(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollBy(
                                -scrollAmountInPx,
                                tween(durationMillis = 500, easing = LinearOutSlowInEasing)
                            )
                        }
                    },
                    textRes = R.string.scroll_up,
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(32.dp),
                )
            }
        }
    }
}