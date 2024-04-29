package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * A component that displays a gradient at the bottom of the screen to indicate that there is more content to scroll to.
 * Has to be placed inside a [Box] layout.
 */
@Composable
fun BoxScope.ScrollScrimComponent(modifier: Modifier = Modifier) {
    Box(
        modifier
            .align(Alignment.BottomCenter)
            .background(
                brush = Brush.verticalGradient(
                    // todo: background: black or white gradient, looks weird on wallpaper. I'd say this is better.
                    colors = listOf(Color.Transparent, LauncherTheme.all.onWallpaperBackground), // .copy(alpha = 0.4f)
                ),
            ),
    )
}