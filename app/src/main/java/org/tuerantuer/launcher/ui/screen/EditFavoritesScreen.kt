package org.tuerantuer.launcher.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.tuerantuer.launcher.R
import org.tuerantuer.launcher.itemInfo.ItemInfo
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
    uiState: UiState.EditFavoritesScreen,
    onCancelEdits: () -> Unit,
    onApplyEdits: (newFavorites: List<ItemInfo>) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.select_favorites),
            Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
        )
        Button(onClick = { onApplyEdits.invoke(emptyList()) }) {
            Text(text = stringResource(id = R.string.save_favorites))
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun EditFavoritesScreenPreview() {
    LauncherTheme {
        EditFavoritesScreen(
            uiState = UiState.EditFavoritesScreen(favorites = emptyList()),
            onCancelEdits = { },
            onApplyEdits = { },
        )
    }
}
