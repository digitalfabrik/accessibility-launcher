package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Shorthand for adding a [ExtendedFloatingActionButton] to the UI.
 */
@Composable
fun ExtendedFabComponent(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    textRes: Int,
    imageVector: ImageVector?,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.padding(16.dp),
        shape = shape,
        onClick = onClick,
        containerColor = color,
        text = {
            Text(
                text = stringResource(textRes),
                color = textColor,
            )
        },
        icon = {
            if (imageVector != null) {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                )
            }
        },
    )
}