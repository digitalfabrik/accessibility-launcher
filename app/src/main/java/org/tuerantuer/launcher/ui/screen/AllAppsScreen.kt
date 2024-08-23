package org.tuerantuer.launcher.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.app.AppItemInfo
import org.tuerantuer.launcher.ui.components.ExtendedFabComponent
import org.tuerantuer.launcher.ui.components.HeaderComponent
import org.tuerantuer.launcher.ui.components.HomeScreenItemComponent
import org.tuerantuer.launcher.ui.data.AppHomeScreenItem
import org.tuerantuer.launcher.ui.data.ScreenState
import org.tuerantuer.launcher.ui.data.UiState
import org.tuerantuer.launcher.ui.theme.LauncherTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.tuerantuer.launcher.ui.components.ScrollScrimComponent
import java.util.Locale

// Used to remove whitespaces from a string.
private val WHITESPACE_REGEX = "\\s+".toRegex()

/**
 * The screen that shows all installed apps.
 *
 * todo This isn't done yet. I'll update it once the homescreen works without the scroll buttons flickering
 */
@Composable
fun AllAppsScreen(
    uiState: UiState,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit = {},
    onGoBack: () -> Unit = {},
    coroutineScope: CoroutineScope,
) {
    val searchQuery = remember { mutableStateOf("") }
    val filteredList = remember { mutableStateOf(uiState.allApps) }
    val searchBarFocusRequester = remember { FocusRequester() }
    val isSearchBarVisible = remember { mutableStateOf(true) }

    // react to changes in the allApps list and update the filtered list (eg. when an app is installed/uninstalled)
    LaunchedEffect(uiState.allApps) {
        if (uiState.allApps !== filteredList.value) {
            filteredList.value = filter(searchQuery.value, uiState.allApps)
        }
    }

    // Request focus and show keyboard when the screen is displayed
    LaunchedEffect(isSearchBarVisible.value) {
        if (isSearchBarVisible.value) {
            delay(300) // Small delay to ensure UI is ready and prevent flickering
            searchBarFocusRequester.requestFocus()
        }
    }

    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LauncherTheme.all.onWallpaperBackground)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { focusManager.clearFocus() },
            ),
    ) {
        HeaderComponent(
            text = stringResource(R.string.all_apps),
            onGoBack = onGoBack,
        )
        AnimatedVisibility(visible = isSearchBarVisible.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(LauncherTheme.all.onWallpaperBackground)
                        .focusRequester(searchBarFocusRequester),
                    value = searchQuery.value,
                    singleLine = true,
                    onValueChange = {
                        searchQuery.value = it
                        filteredList.value = filter(searchQuery.value, uiState.allApps)
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = LauncherTheme.all.onWallpaperText.color,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.search),
                            style = LauncherTheme.all.onWallpaperText.copy(textIndent = TextIndent(8.sp)),
                        )
                    },
                    textStyle = LauncherTheme.all.onWallpaperText.copy(textIndent = TextIndent(8.sp)),
                    shape = RoundedCornerShape(32.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchQuery.value
                            focusManager.clearFocus()
                        },
                    ),
                )
            }
        }
        SearchToggleButton( isSearchBarVisible = isSearchBarVisible )
        val appIconSize = uiState.settings.appIconSize.sizeDp.dp
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = appIconSize),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(
                items = filteredList.value.map { appItemInfo ->
                    AppHomeScreenItem(
                        appItemInfo,
                        onClick = { onOpenApp(appItemInfo) },
                    )
                },
                key = { homeScreenItems ->
                    homeScreenItems.key
                },
            ) { homeScreenItem ->
                HomeScreenItemComponent(
                    homeScreenItem = homeScreenItem,
                    iconSize = appIconSize,
    val gridState = rememberLazyGridState()
    val showScrollButtonState = rememberSaveable { mutableStateOf(false) }

    val scrollButtonsOpacity by animateFloatAsState (
        targetValue = if (showScrollButtonState.value) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing),
        label = "",
    )

    LaunchedEffect(key1 = uiState.allApps.size) {
        val totalItemsCount = gridState.layoutInfo.totalItemsCount
        val visibleItemsCount = gridState.layoutInfo.visibleItemsInfo.size
        val shouldShow = totalItemsCount > visibleItemsCount
        if (shouldShow != showScrollButtonState.value) {
            showScrollButtonState.value = shouldShow
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LauncherTheme.all.onWallpaperBackground),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            HeaderComponent(
                text = stringResource(R.string.all_apps),
                onGoBack = onGoBack,
            )
            val homeScreenItems =
                uiState.allApps.map { appItemInfo ->
                    AppHomeScreenItem(
                        appItemInfo,
                        onClick = { onOpenApp(appItemInfo) })
                }
            val appIconSize = uiState.settings.appIconSize.sizeDp.dp
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = appIconSize),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    items(
                        items = homeScreenItems,
                        key = { homeScreenItems -> homeScreenItems.key },
                    ) { homeScreenItem ->
                        HomeScreenItemComponent(
                            homeScreenItem = homeScreenItem,
                            iconSize = appIconSize,
                        )
                    }
                }
                ScrollScrimComponent(
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .alpha(scrollButtonsOpacity)

                )
            }
            ScrollButtons(
                showScrollButtonState = showScrollButtonState.value,
                scrollButtonsOpacity = scrollButtonsOpacity,
                gridState = gridState,
                coroutineScope = coroutineScope,
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun AllAppsScreenPreview() {
    LauncherTheme {
        AllAppsScreen(
            uiState = UiState(ScreenState.AllAppsScreenState),
            coroutineScope = CoroutineScope(GlobalScope.coroutineContext),
        )
    }
}

/**
 * Filters the list of apps based on the search query.
 */
fun filter(searchQuery: String, allAppsList: List<AppItemInfo>): List<AppItemInfo> {
    if (searchQuery.isEmpty()) {
        return allAppsList
    }
    return allAppsList.filter { appItemInfo ->
        normalizeName(appItemInfo.name).contains(normalizeName(searchQuery))
    }
}

/**
 * Normalizes the name of an app by removing whitespaces and making it lowercase.
 */
fun normalizeName(name: String): String {
    return name.replace(WHITESPACE_REGEX, "").lowercase(Locale.ROOT)
}

@Composable
fun SearchToggleButton(
    isSearchBarVisible: MutableState<Boolean>
) {
    val textRes by rememberUpdatedState(if (isSearchBarVisible.value) R.string.close_search else R.string.open_search)
    val color by animateColorAsState(
        targetValue = if (isSearchBarVisible.value) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.secondaryContainer
        },
        animationSpec = tween(durationMillis = 300), label = ""
    )

    ExtendedFabComponent(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(32.dp),
        imageVector = null,
        color = color,
        textColor = MaterialTheme.colorScheme.onTertiaryContainer,
        textRes = textRes,
        onClick = {
            isSearchBarVisible.value = !isSearchBarVisible.value
        }
    )
}