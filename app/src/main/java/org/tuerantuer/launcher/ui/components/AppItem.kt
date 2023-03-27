package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import org.tuerantuer.launcher.itemInfo.AppItemInfo

/**
 * TODO: add description
 *
 * @author Peter Huber
 * Created on 27/03/2023
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppItem(
    appItemInfo: AppItemInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(4.dp).fillMaxWidth(),
        ) {
            Image(
                modifier = Modifier.width(48.dp).height(48.dp).padding(top = 8.dp),
                painter = rememberDrawablePainter(appItemInfo.icon),
                contentDescription = null,
            )
            Text(
                text = appItemInfo.name,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp),
                maxLines = 1,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}
