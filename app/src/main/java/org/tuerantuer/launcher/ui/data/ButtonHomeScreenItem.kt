package org.tuerantuer.launcher.ui.data

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import kotlinx.parcelize.Parcelize
import org.tuerantuer.launcher.ui.theme.almostBlack
import org.tuerantuer.launcher.ui.theme.almostWhite
import org.tuerantuer.launcher.util.CircleDrawable
import org.tuerantuer.launcher.util.CustomInsetDrawable

/**
 * A button on the home screen (e.g. to open the launcher settings) that shows up like an app.
 */
class ButtonHomeScreenItem(
    @StringRes val nameRes: Int,
    @DrawableRes val innerIconRes: Int,
    context: Context,
    override val onClick: () -> Unit,
    val iconColor:androidx.compose.ui.graphics.Color? = null,
    val backgroundColor: androidx.compose.ui.graphics.Color? = null,
    val borderColor: androidx.compose.ui.graphics.Color? = null,
) : HomeScreenItem {

    /**
     * @see HomeScreenItem.key
     */
    @Parcelize
    @Suppress("unused")
    private data class SubKey(val nameRes: Int) : Parcelable

    override val key: HomeScreenItem.HomeScreenItemKey =
        HomeScreenItem.HomeScreenItemKey(HomeScreenItem.KeyType.BUTTON, SubKey(nameRes))

    override val icon: Drawable = kotlin.run {
        val innerIcon = AppCompatResources.getDrawable(context, innerIconRes)!!
        innerIcon.setTintList(null)
        val innerIconColor = iconColor?.toArgb() ?: almostBlack.toArgb()
        val fillColor = backgroundColor?.toArgb() ?: almostWhite.toArgb()
        val borderColor = borderColor?.toArgb() ?: innerIconColor
        innerIcon.setTint(innerIconColor)
        val paddedInnerIcon = CustomInsetDrawable(innerIcon, wrappedDrawableRatio = 0.5f)
        val drawables = arrayOf(
            CircleDrawable(circleColor = fillColor,strokeColor = borderColor, circleRatio =  0.9f, context = context),
            paddedInnerIcon,
        )
        LayerDrawable(drawables).apply {}
    }

    @Composable
    override fun loadName(): String {
        return stringResource(nameRes)
    }
}
