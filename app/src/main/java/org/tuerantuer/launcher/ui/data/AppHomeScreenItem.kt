package org.tuerantuer.launcher.ui.data

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.compose.runtime.Composable
import kotlinx.parcelize.Parcelize
import org.tuerantuer.launcher.app.AppItemInfo

/**
 * Represents an app on the home screen.
 *
 * @author Peter Huber
 * Created on 08/04/2023
 */
class AppHomeScreenItem(
    private val appItemInfo: AppItemInfo,
    override val onClick: () -> Unit,
) : HomeScreenItem {

    /**
     * @see HomeScreenItem.key
     */
    @Parcelize
    @Suppress("unused")
    private data class SubKey(val packageName: String, val className: String, val user: Long) : Parcelable

    override val key: HomeScreenItem.HomeScreenItemKey

    override val icon: Drawable? = appItemInfo.icon

    init {
        val componentKey = appItemInfo.componentKey
        val subKey = SubKey(
            componentKey.packageName,
            componentKey.className,
            appItemInfo.componentKeySer.userHandleSer,
        )
        key = HomeScreenItem.HomeScreenItemKey(HomeScreenItem.KeyType.APP, subKey)
    }

    @Composable
    override fun loadName(): String = appItemInfo.name
}
