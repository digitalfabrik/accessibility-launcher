package org.tuerantuer.launcher.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.data.datastore.AppTextSize
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LINE_HEIGHT_MULTIPLIER
import org.tuerantuer.launcher.ui.theme.LauncherTheme

@Composable
fun SetTextSizeComponent(
    modifier: Modifier = Modifier,
    uiState: UiState,
    onSetTextSize: (appTextSize: AppTextSize) -> Unit = {},
) {
    val textContent = stringResource(R.string.confirm_font_size)

    // transition
    val appTextSize = uiState.settings.appTextSize
    val transition = updateTransition(targetState = appTextSize, label = "TextSizeTransition")

    // scale and fade in/out animations
    val textScale by transition.animateFloat(
        label = "Scale",
        transitionSpec = {
            tween(durationMillis = 300)
        },
    ) { appTextSize ->
        appTextSize.scalingFactor
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .background(LauncherTheme.all.onWallpaperBackground),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                val unscaledFontSize = MaterialTheme.typography.titleSmall.fontSize / appTextSize.scalingFactor
                // Un-apply the scaling factor to the font size and apply the scale through graphicsLayer, so tha we can
                // smoothly animate the scale of the text.
                Text(
                    text = textContent,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .wrapContentHeight()
                        .graphicsLayer(
                            scaleX = textScale,
                            scaleY = textScale,
                            alpha = 1f,
                        )
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = unscaledFontSize,
                        lineHeight = unscaledFontSize * (1f / LINE_HEIGHT_MULTIPLIER),
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        }

        ScalingButtonComponent(
            currentSize = appTextSize,
            onSetSize = onSetTextSize,
        )
    }
}