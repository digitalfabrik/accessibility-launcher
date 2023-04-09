package org.tuerantuer.launcher.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.coroutines.Job
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.ScreenState
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * The place where the user can edit their favorites.
 *
 * @author Peter Huber
 * Created on 27/03/2023
 */
@Composable
fun EditFavoritesScreen(
    uiState: UiState,
    onCancelEdits: () -> Unit,
    onApplyEdits: (newFavorites: List<AppItemInfo>) -> Unit,
) {
    val appIconSize = uiState.settings.appIconSize.sizeDp.dp
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shadowElevation = 8.dp,
            // only round bottom corners
            shape = MaterialTheme.shapes.medium.copy(
                topStart = CornerSize(0f),
                topEnd = CornerSize(0f),
            ),
        ) {
            Text(
                text = stringResource(R.string.select_favorites),
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(16.dp),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        val selectedFavorites = remember { mutableStateOf(uiState.favorites) }
        EditFavoritesList(
            modifier = Modifier.weight(1f),
            items = uiState.allApps,
            initiallySelectedItems = selectedFavorites.value,
            appIconSize = appIconSize,
            onAppChecked = { appItemInfo, isChecked ->
                if (isChecked) {
                    selectedFavorites.value = selectedFavorites.value
                        .toMutableStateList().apply { add(appItemInfo) }
                } else {
                    selectedFavorites.value = selectedFavorites.value
                        .toMutableStateList().apply { remove(appItemInfo) }
                }
            },
            onAppMoved = { posA, posB ->
            },
        )
        Surface(
            modifier = Modifier
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
            ) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = { onApplyEdits.invoke(selectedFavorites.value) },
                    text = {
                        Text(text = stringResource(R.string.button_save_favorites))
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Interests,
                            contentDescription = null,
                        )
                    },
                )
                Text(
                    modifier = Modifier.padding(top = 32.dp),
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.edit_favorites_info),
                )
            }
        }
    }
}

@Composable
fun EditFavoritesList(
    modifier: Modifier = Modifier,
    items: List<AppItemInfo>,
    initiallySelectedItems: List<AppItemInfo>,
    appIconSize: Dp,
    onAppMoved: (Int, Int) -> Unit,
    onAppChecked: (appItemInfo: AppItemInfo, isChecked: Boolean) -> Unit,
) {
//    val scope = rememberCoroutineScope()
//    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    val dragDropListState = rememberDragDropListState(onMove = onAppMoved)
    LazyColumn(
        modifier = modifier,
//            .pointerInput(Unit) {
//                detectDragGesturesAfterLongPress(
//                    onDrag = { change, offset ->
//                        change.consume()
//                        dragDropListState.onDrag(offset)
//
//                        if (overscrollJob?.isActive == true) {
//                            return@detectDragGesturesAfterLongPress
//                        }
//
//                        dragDropListState.checkForOverScroll()
//                            .takeIf { it != 0f }
//                            ?.let { overscrollJob = scope.launch { dragDropListState.lazyListState.scrollBy(it) } }
//                            ?: run { overscrollJob?.cancel() }
//                    },
//                    onDragStart = { offset -> dragDropListState.onDragStart(offset) },
//                    onDragEnd = { dragDropListState.onDragInterrupted() },
//                    onDragCancel = { dragDropListState.onDragInterrupted() },
//                )
//            },
        state = dragDropListState.lazyListState,
    ) {
        itemsIndexed(items) { index, item ->
            Column(
                modifier = Modifier
                    .composed {
                        val offsetOrNull =
                            dragDropListState.elementDisplacement.takeIf {
                                index == dragDropListState.currentIndexOfDraggedItem
                            }

                        Modifier
                            .graphicsLayer {
                                translationY = offsetOrNull ?: 0f
                            }
                    }
                    .fillMaxWidth(),
            ) { AppListItem(item, isInitiallyChecked = item in initiallySelectedItems, appIconSize, onAppChecked) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListItem(
    appItemInfo: AppItemInfo,
    isInitiallyChecked: Boolean,
    appIconSize: Dp,
    onAppChecked: (AppItemInfo, Boolean) -> Unit,
) {
    var isChecked by remember { mutableStateOf(isInitiallyChecked) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .run {
                if (isChecked) {
                    border(3.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                } else {
                    this
                }
            },
        shape = RoundedCornerShape(8.dp),
        onClick = { isChecked = !isChecked; onAppChecked(appItemInfo, isChecked) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it; onAppChecked(appItemInfo, isChecked) },
            )
            Image(
                modifier = Modifier.width(appIconSize).height(appIconSize).padding(vertical = 8.dp),
                painter = rememberDrawablePainter(appItemInfo.icon),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = appItemInfo.name,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun rememberDragDropListState(
    lazyListState: LazyListState = rememberLazyListState(),
    onMove: (Int, Int) -> Unit,
): DragDropListState {
    return remember { DragDropListState(lazyListState = lazyListState, onMove = onMove) }
}

class DragDropListState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
) {
    var draggedDistance by mutableStateOf(0f)

    // used to obtain initial offsets on drag start
    var initiallyDraggedElement by mutableStateOf<LazyListItemInfo?>(null)

    var currentIndexOfDraggedItem by mutableStateOf<Int?>(null)

    val initialOffsets: Pair<Int, Int>?
        get() = initiallyDraggedElement?.let { Pair(it.offset, it.offsetEnd) }

    val elementDisplacement: Float?
        get() = currentIndexOfDraggedItem
            ?.let { lazyListState.getVisibleItemInfoFor(absoluteIndex = it) }
            ?.let { item -> (initiallyDraggedElement?.offset ?: 0f).toFloat() + draggedDistance - item.offset }

    val currentElement: LazyListItemInfo?
        get() = currentIndexOfDraggedItem?.let {
            lazyListState.getVisibleItemInfoFor(absoluteIndex = it)
        }

    var overscrollJob by mutableStateOf<Job?>(null)

    fun onDragStart(offset: Offset) {
        lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item -> offset.y.toInt() in item.offset..(item.offset + item.size) }
            ?.also {
                currentIndexOfDraggedItem = it.index
                initiallyDraggedElement = it
            }
    }

    fun onDragInterrupted() {
        draggedDistance = 0f
        currentIndexOfDraggedItem = null
        initiallyDraggedElement = null
        overscrollJob?.cancel()
    }

    fun onDrag(offset: Offset) {
        draggedDistance += offset.y

        initialOffsets?.let { (topOffset, bottomOffset) ->
            val startOffset = topOffset + draggedDistance
            val endOffset = bottomOffset + draggedDistance

            currentElement?.let { hovered ->
                lazyListState.layoutInfo.visibleItemsInfo
                    .filterNot { item ->
                        item.offsetEnd < startOffset || item.offset > endOffset || hovered.index == item.index
                    }
                    .firstOrNull { item ->
                        val delta = startOffset - hovered.offset
                        when {
                            delta > 0 -> (endOffset > item.offsetEnd)
                            else -> (startOffset < item.offset)
                        }
                    }
                    ?.also { item ->
                        currentIndexOfDraggedItem?.let { current -> onMove.invoke(current, item.index) }
                        currentIndexOfDraggedItem = item.index
                    }
            }
        }
    }

    fun checkForOverScroll(): Float {
        return initiallyDraggedElement?.let {
            val startOffset = it.offset + draggedDistance
            val endOffset = it.offsetEnd + draggedDistance

            return@let when {
                draggedDistance > 0 -> (endOffset - lazyListState.layoutInfo.viewportEndOffset)
                    .takeIf { diff -> diff > 0 }
                draggedDistance < 0 -> (startOffset - lazyListState.layoutInfo.viewportStartOffset)
                    .takeIf { diff -> diff < 0 }
                else -> null
            }
        } ?: 0f
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun EditFavoritesScreenPreview() {
    LauncherTheme {
        EditFavoritesScreen(
            uiState = UiState(ScreenState.EditFavoritesScreenState),
            onCancelEdits = { },
            onApplyEdits = { },
        )
    }
}

/*
    LazyListItemInfo.index is the item's absolute index in the list
    Based on the item's "relative position" with the "currently top" visible item,
    this returns LazyListItemInfo corresponding to it
*/
fun LazyListState.getVisibleItemInfoFor(absoluteIndex: Int): LazyListItemInfo? {
    return this.layoutInfo.visibleItemsInfo.getOrNull(absoluteIndex - this.layoutInfo.visibleItemsInfo.first().index)
}

/*
  Bottom offset of the element in Vertical list
*/
val LazyListItemInfo.offsetEnd: Int
    get() = this.offset + this.size

/*
   Moving element in the list
*/
fun <T> MutableList<T>.move(from: Int, to: Int) {
    if (from == to) {
        return
    }

    val element = this.removeAt(from) ?: return
    this.add(to, element)
}
