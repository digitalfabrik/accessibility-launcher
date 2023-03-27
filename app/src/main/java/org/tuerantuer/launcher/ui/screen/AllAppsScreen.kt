package org.tuerantuer.launcher.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.AppItemInfo
import org.tuerantuer.launcher.ui.UiState
import org.tuerantuer.launcher.ui.components.AppItem
import org.tuerantuer.launcher.ui.theme.LauncherTheme

/**
 * The screen that shows all installed apps.
 *
 * @author Peter Huber
 * Created on 27/03/2023
 */
@Composable
fun AllAppsScreen(
    uiState: UiState.AllAppsScreen,
    onOpenApp: (appItemInfo: AppItemInfo) -> Unit,
) {
    Text(
        text = stringResource(R.string.all_apps),
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 24.sp,
    )
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 86.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
    ) {
        items(
            items = uiState.allApps,
            key = { appItemInfo -> appItemInfo.key },
        ) { appItemInfo ->
            AppItem(appItemInfo, onClick = { onOpenApp(appItemInfo) })
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
            uiState = UiState.AllAppsScreen(allApps = emptyList()),
            onOpenApp = {},
        )
    }
}
