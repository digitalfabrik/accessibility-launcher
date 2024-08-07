package org.tuerantuer.launcher.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import java.util.Locale

// Used to remove whitespaces from a string.
private val WHITESPACE_REGEX = "\\s+".toRegex()

/**
 * The screen that shows all installed apps.
 */
@Composable
fun AllAppsScreen(
    uiState: UiState,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit = {},
    onGoBack: () -> Unit = {},
) {
    val searchQuery = remember { mutableStateOf("") }
    val filteredList = remember { mutableStateOf(uiState.allApps) }
    val focusRequester = remember { FocusRequester() }
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
            focusRequester.requestFocus()
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
            )
    ) {
        HeaderComponent(
            text = stringResource(R.string.all_apps),
            onGoBack = onGoBack,
        )
        AnimatedVisibility(visible = isSearchBarVisible.value) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp)
                        .background(LauncherTheme.all.onWallpaperBackground)
                        .focusRequester(focusRequester),
                    value = searchQuery.value,
                    singleLine = true,
                    onValueChange = {
                        searchQuery.value = it
                        filteredList.value = filter(searchQuery.value, uiState.allApps)
                    },
                    trailingIcon = {
                        Icon(
                            // icon on the right side instead of left
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = LauncherTheme.all.onWallpaperText.color,
                        )
                    },
                    placeholder = { Text(stringResource(R.string.search), style = LauncherTheme.all.onWallpaperText) },
                    textStyle = LauncherTheme.all.onWallpaperText,
                    shape = RoundedCornerShape(32.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            searchQuery.value
                            focusManager.clearFocus()
                        },
                    ),
                )

                ExtendedFabComponent(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    imageVector = null,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    textColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    textRes = R.string.close_search,
                    onClick = {
                        isSearchBarVisible.value = false
                        focusManager.clearFocus()
                    },
                )
            }
        }

        AnimatedVisibility(visible = !isSearchBarVisible.value) {
            ExtendedFabComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(32.dp),
                imageVector = null,
                color = MaterialTheme.colorScheme.secondaryContainer,
                textColor = MaterialTheme.colorScheme.onSecondaryContainer,
                textRes = R.string.open_search,
                onClick = { isSearchBarVisible.value = true },
            )
        }
        val appIconSize = uiState.settings.appIconSize.sizeDp.dp
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = appIconSize),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(
                items = filteredList.value.map { appItemInfo ->
                    AppHomeScreenItem(
                        appItemInfo,
                        onClick = { onOpenApp(appItemInfo) }
                    )
                },
                key = { homeScreenItems ->
                    homeScreenItems.key
                },
            ) { homeScreenItem ->
                HomeScreenItemComponent(
                    homeScreenItem = homeScreenItem,
                    iconSize = appIconSize,
                )
            }
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