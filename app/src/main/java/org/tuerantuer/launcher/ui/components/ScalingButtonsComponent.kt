package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.tuerantuer.launcher.R

/**
 * Buttons used in the set text and icon size screens to increase or decrease the size of the text or icons.
 */
@Composable
inline fun <reified T : Enum<T>> ScalingButtonComponent(
    modifier: Modifier = Modifier,
    currentSize: T,
    crossinline onSetSize: (T) -> Unit,
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        val allValues = enumValues<T>()
        val currentSizeIndex = allValues.indexOf(currentSize)
        val smallerSize = allValues.getOrNull(currentSizeIndex - 1)
        val largerSize = allValues.getOrNull(currentSizeIndex + 1)

        // Show button to decrease text size if a smaller size is available
        if (smallerSize != null) {
            ExtendedFabComponent(
                modifier = Modifier.padding(start = 0.dp, bottom = 16.dp).weight(1f),
                onClick = {
                    onSetSize.invoke(smallerSize)
                },
                textRes = R.string.button_decrease_size,
                imageVector = Icons.Filled.ZoomOut,
            )
        }
        // Show button to increase text size if a larger size is available
        if (largerSize != null) {
            ExtendedFabComponent(
                modifier = Modifier.padding(end = 0.dp, bottom = 16.dp).weight(1f),
                onClick = {
                    onSetSize.invoke(largerSize)
                },
                textRes = R.string.button_increase_size,
                imageVector = Icons.Filled.ZoomIn,
            )
        }
    }
}