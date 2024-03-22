package org.tuerantuer.launcher.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.sp

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
        titleMedium = defaultTypography.titleMedium.copy(hyphens = Hyphens.Auto, fontSize = 24.sp),
        titleSmall = defaultTypography.titleSmall.copy(hyphens = Hyphens.Auto, fontSize = 20.sp),
        bodyLarge = defaultTypography.bodyLarge.copy(hyphens = Hyphens.Auto, fontSize = 24.sp),
        bodyMedium = defaultTypography.bodyMedium.copy(hyphens = Hyphens.Auto, fontSize = 20.sp),
        bodySmall = defaultTypography.bodySmall.copy(hyphens = Hyphens.Auto),
        labelLarge = defaultTypography.labelLarge.copy(hyphens = Hyphens.Auto, fontSize = 20.sp),
        labelMedium = defaultTypography.labelMedium.copy(hyphens = Hyphens.Auto),
        labelSmall = defaultTypography.labelSmall.copy(hyphens = Hyphens.Auto),
    )
}
