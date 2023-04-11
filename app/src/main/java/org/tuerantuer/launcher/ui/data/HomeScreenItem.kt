package org.tuerantuer.launcher.ui.data

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize

/**
 * Either an app or a button on the home screen.
 *
 * @author Peter Huber
 * Created on 08/04/2023
 */
interface HomeScreenItem {

    enum class KeyType {
        APP,
        BUTTON,
    }

    /**
     * If two [HomeScreenItem]s have the same key, they are considered to be the same item. [LazyColumn] uses this to
     * determine whether an item has changed or not (and thus needs to be re-rendered).
     */
    @Parcelize
    @Suppress("unused")
    data class HomeScreenItemKey(val type: KeyType, val subKey: Parcelable) : Parcelable

    val key: HomeScreenItemKey
    val onClick: () -> Unit
    val icon: Drawable?

    @Composable
    fun loadName(): String
}
