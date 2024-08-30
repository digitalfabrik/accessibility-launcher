package org.tuerantuer.launcher.util.extension

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll

fun Modifier.setScrollingEnabled(enabled: Boolean): Modifier = if (enabled) {
    this
} else {
    this.nestedScroll(NoOpNestedScrollConnection)
}

// No operation nested scroll connection to block nested scroll events
private object NoOpNestedScrollConnection : NestedScrollConnection {
    override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
    ): Offset {
        return available // Consume all scroll events
    }
}