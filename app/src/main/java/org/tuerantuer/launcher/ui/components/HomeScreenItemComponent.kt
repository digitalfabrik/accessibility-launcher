package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.tuerantuer.launcher.ui.data.HomeScreenItem
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * Visual representation of a [HomeScreenItem].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenItemComponent(
    modifier: Modifier = Modifier,
    homeScreenItem: HomeScreenItem,
    iconSize: Dp,
    useOnWallpaperStyle: Boolean = true,
) {
    Card(
        onClick = homeScreenItem.onClick,
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier
                    .size(iconSize)
                    .padding(top = 16.dp),
                painter = rememberDrawablePainter(homeScreenItem.icon),
                contentDescription = null,
            )
            val textStyle = if (useOnWallpaperStyle) {
                LauncherTheme.all.onWallpaperText
            } else {
                MaterialTheme.typography.labelLarge
            }
            Text(
                text = homeScreenItem.loadName(),
                style = textStyle,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp),
                maxLines = 2,
            )
        }
    }
}
