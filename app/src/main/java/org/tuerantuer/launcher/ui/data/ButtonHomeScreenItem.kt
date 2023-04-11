package org.tuerantuer.launcher.ui.data

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import org.tuerantuer.launcher.util.CustomInsetDrawable

/**
 * A button on the home screen (e.g. to open the launcher settings) that shows up like an app.
 *
 * @author Peter Huber
 * Created on 09/04/2023
 */
class ButtonHomeScreenItem(
    @StringRes val nameRes: Int,
    @DrawableRes val innerIconRes: Int,
    context: Context,
    override val onClick: () -> Unit,
) : HomeScreenItem {

    /**
     * @see HomeScreenItem.key
     */
    @Parcelize
    @Suppress("unused")
    private class SubKey(val nameRes: Int) : Parcelable

    override val key: HomeScreenItem.HomeScreenItemKey =
        HomeScreenItem.HomeScreenItemKey(HomeScreenItem.KeyType.BUTTON, SubKey(nameRes))

    override val icon: Drawable = AdaptiveIconDrawable(
        ColorDrawable(Color.WHITE),
        CustomInsetDrawable(AppCompatResources.getDrawable(context, innerIconRes)!!, wrappedDrawableRatio = 0.33f),
    )

    @Composable
    override fun loadName(): String {
        return stringResource(nameRes)
    }
}
