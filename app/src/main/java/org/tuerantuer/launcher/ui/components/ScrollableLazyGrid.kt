package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun ScrollableLazyVerticalGrid(
    columns: GridCells,
    modifier: Modifier = Modifier,
    scrollEnabled: Boolean,
    state: LazyGridState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: LazyGridScope.() -> Unit,
) {
    val appliedModifier = modifier.then(
        if (scrollEnabled) {
            Modifier // Normal modifier when scrolling is enabled
        } else {
            Modifier
                .disableScrolling()
                .nestedScroll(NoOpNestedScrollConnection) // Apply no-op nested scroll connection
        },
    )


    LazyVerticalGrid(
        columns = columns,
        modifier = appliedModifier,
        state = state,
        contentPadding = contentPadding,
        content = content,
    )
}

fun Modifier.disableScrolling(): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        detectVerticalDragGestures { change, _ ->
            change.consume() // Consume all vertical drag gestures
        }
    },
)

// No operation nested scroll connection to block nested scroll events
private object NoOpNestedScrollConnection : NestedScrollConnection {
    override fun onPreScroll(
        available: androidx.compose.ui.geometry.Offset,
        source: androidx.compose.ui.input.nestedscroll.NestedScrollSource,
    ): androidx.compose.ui.geometry.Offset {
        return available // Consume all scroll events
    }
}
