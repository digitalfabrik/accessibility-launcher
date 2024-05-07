package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

/**
 * Shorthand for adding a [ExtendedFloatingActionButton] to the UI.
 */
@Composable
fun ExtendedFabComponent(
    modifier: Modifier = Modifier,
    textRes: Int,
    imageVector: ImageVector?,
    onClick: () -> Unit,
) {
    ExtendedFloatingActionButton(
        modifier = modifier.padding(16.dp),
        onClick = onClick,
        text = {
            Text(text = stringResource(textRes))
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