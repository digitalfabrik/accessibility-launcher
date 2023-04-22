package org.tuerantuer.launcher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.style.Hyphens

val Typography = run {
    val defaultTypography = Typography()

    // Enable hyphenation for all text styles. This makes the text more readable, especially large text.
    Typography(
        displayLarge = defaultTypography.displayLarge.copy(hyphens = Hyphens.Auto),
        displayMedium = defaultTypography.displayMedium.copy(hyphens = Hyphens.Auto),
        displaySmall = defaultTypography.displaySmall.copy(hyphens = Hyphens.Auto),
        headlineLarge = defaultTypography.headlineLarge.copy(hyphens = Hyphens.Auto),
        headlineMedium = defaultTypography.headlineMedium.copy(hyphens = Hyphens.Auto),
        headlineSmall = defaultTypography.headlineSmall.copy(hyphens = Hyphens.Auto),
        titleLarge = defaultTypography.titleLarge.copy(hyphens = Hyphens.Auto),
        titleMedium = defaultTypography.titleMedium.copy(hyphens = Hyphens.Auto),
        titleSmall = defaultTypography.titleSmall.copy(hyphens = Hyphens.Auto),
        bodyLarge = defaultTypography.bodyLarge.copy(hyphens = Hyphens.Auto),
        bodyMedium = defaultTypography.bodyMedium.copy(hyphens = Hyphens.Auto),
        bodySmall = defaultTypography.bodySmall.copy(hyphens = Hyphens.Auto),
        labelLarge = defaultTypography.labelLarge.copy(hyphens = Hyphens.Auto),
        labelMedium = defaultTypography.labelMedium.copy(hyphens = Hyphens.Auto),
        labelSmall = defaultTypography.labelSmall.copy(hyphens = Hyphens.Auto),
    )
}
