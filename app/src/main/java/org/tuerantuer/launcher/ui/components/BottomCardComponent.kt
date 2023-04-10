package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A static bottom sheet component that can be used to display content inside.
 *
 * @author Peter Huber
 * Created on 10/04/2023
 */
@Composable
fun BottomSheetComponent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shadowElevation = 8.dp,
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
                .wrapContentHeight()
                .padding(
                    top = 32.dp,
                    bottom = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                ),
            content = content,
        )
    }
}
