package org.tuerantuer.launcher.data.datastore

/**
 * The size of the app icons in the app grid.
 */
enum class AppIconSize(val sizeDp: Int) {
    M(72),
    L(84),
    XL(96),
    XXL(128);

    fun findMatchingTextSize(): AppTextSize {
        return when (this) {
            M -> AppTextSize.M
            L -> AppTextSize.L
            XL -> AppTextSize.XL
            XXL -> AppTextSize.XL
        }
    }
}
