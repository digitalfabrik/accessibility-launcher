package org.tuerantuer.launcher.ui

import android.graphics.drawable.Drawable
import android.os.Parcelable
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

    @Parcelize
    open class HomeScreenItemKey(val type: KeyType, val subkey: Parcelable) : Parcelable

    val key: HomeScreenItemKey
    val onClick: () -> Unit
    val icon: Drawable?

    @Composable
    fun loadName(): String
}
