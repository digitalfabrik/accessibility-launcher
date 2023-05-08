package org.tuerantuer.launcher.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Wrapper for [LazyColumn] that only allows one item.
 */
@Composable
fun ScrollableColumn(
    modifier: Modifier = Modifier,
    content: @Composable LazyItemScope.() -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        item(content = content)
    }
}