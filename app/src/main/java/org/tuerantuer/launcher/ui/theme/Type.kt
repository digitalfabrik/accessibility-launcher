package org.tuerantuer.launcher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Determines the line height based on the font size.
 */
const val LINE_HEIGHT_MULTIPLIER = 1.1f

/**
 * Creates a [Typography] object with custom settings.
 *
 * Here are a few examples where the different text styles are used:
 * - title medium is used for the the titles in the onboarding screen
 * - title small for Onboarding screen text
 * - bodyLarge for HomescreenItemComponents & Buttons
 * - bodyMedium for Slider text in the settings screens
 * - bodySmall for text in the onboarding screens
 * - labelLarge for button text and options on the settingsscreen (assistent / standard einstelllen / ...)
 */
fun createTypography(scalingFactor: Float): Typography {
    val defaultTypography = Typography()

    // Enable hyphenation for all text styles. This makes the text more readable, especially large text.
    return Typography(
        displayLarge = defaultTypography.displayLarge.scaleSizeWithHyphens(scalingFactor),
        displayMedium = defaultTypography.displayMedium.scaleSizeWithHyphens(scalingFactor),
        displaySmall = defaultTypography.displaySmall.scaleSizeWithHyphens(scalingFactor),
        headlineLarge = defaultTypography.headlineLarge.scaleSizeWithHyphens(scalingFactor),
        headlineMedium = defaultTypography.headlineMedium.scaleSizeWithHyphens(scalingFactor),
        headlineSmall = defaultTypography.headlineSmall.scaleSizeWithHyphens(scalingFactor),
        titleLarge = defaultTypography.titleLarge.scaleSizeWithHyphens(scalingFactor),
        titleMedium = defaultTypography.titleMedium.scaleSizeWithHyphens(
            unscaledFontSize = 24.sp,
            scalingFactor = scalingFactor,
        ),
        titleSmall = defaultTypography.titleSmall.scaleSizeWithHyphens(
            unscaledFontSize = 20.sp,
            scalingFactor = scalingFactor,
        ),
        bodyLarge = defaultTypography.bodyLarge.scaleSizeWithHyphens(
            unscaledFontSize = 24.sp,
            scalingFactor = scalingFactor,
        ),
        bodyMedium = defaultTypography.bodyMedium.scaleSizeWithHyphens(
            unscaledFontSize = 20.sp,
            scalingFactor = scalingFactor,
        ),
        bodySmall = defaultTypography.bodySmall.scaleSizeWithHyphens(scalingFactor),
        labelLarge = defaultTypography.labelLarge.scaleSizeWithHyphens(scalingFactor),
        labelMedium = defaultTypography.labelMedium.scaleSizeWithHyphens(scalingFactor),
        labelSmall = defaultTypography.labelSmall.scaleSizeWithHyphens(scalingFactor),
    )
}

fun TextStyle.scaleSizeWithHyphens(scalingFactor: Float, unscaledFontSize: TextUnit = fontSize): TextStyle {
    val scaledFontSize = unscaledFontSize * scalingFactor
    return copy(
        hyphens = Hyphens.Auto,
        fontSize = scaledFontSize,
        lineHeight = scaledFontSize * LINE_HEIGHT_MULTIPLIER,
    )
}
