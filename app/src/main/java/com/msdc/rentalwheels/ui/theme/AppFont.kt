package com.msdc.rentalwheels.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.msdc.rentalwheels.R

object AppFont {
    val inter =
        FontFamily(
            Font(R.font.iregular),
        )
}

val Typography: Typography =
    Typography(
        displayLarge =
        RentalWheelsDesignSystem.Typography.displayLarge.copy(
            fontFamily = AppFont.inter
        ),
        displayMedium =
        RentalWheelsDesignSystem.Typography.displayMedium.copy(
            fontFamily = AppFont.inter
        ),
        displaySmall =
        RentalWheelsDesignSystem.Typography.displaySmall.copy(
            fontFamily = AppFont.inter
        ),
        headlineLarge =
        RentalWheelsDesignSystem.Typography.headlineLarge.copy(
            fontFamily = AppFont.inter
        ),
        headlineMedium =
        RentalWheelsDesignSystem.Typography.headlineMedium.copy(
            fontFamily = AppFont.inter
        ),
        headlineSmall =
        RentalWheelsDesignSystem.Typography.headlineSmall.copy(
            fontFamily = AppFont.inter
        ),
        titleLarge =
        RentalWheelsDesignSystem.Typography.titleLarge.copy(
            fontFamily = AppFont.inter
        ),
        titleMedium =
        RentalWheelsDesignSystem.Typography.titleMedium.copy(
            fontFamily = AppFont.inter
        ),
        titleSmall =
        RentalWheelsDesignSystem.Typography.titleSmall.copy(
            fontFamily = AppFont.inter
        ),
        bodyLarge =
        RentalWheelsDesignSystem.Typography.bodyLarge.copy(
            fontFamily = AppFont.inter
        ),
        bodyMedium =
        RentalWheelsDesignSystem.Typography.bodyMedium.copy(
            fontFamily = AppFont.inter
        ),
        bodySmall =
        RentalWheelsDesignSystem.Typography.bodySmall.copy(
            fontFamily = AppFont.inter
        ),
        labelLarge =
        RentalWheelsDesignSystem.Typography.labelLarge.copy(
            fontFamily = AppFont.inter
        ),
        labelMedium =
        RentalWheelsDesignSystem.Typography.labelMedium.copy(
            fontFamily = AppFont.inter
        ),
        labelSmall =
        RentalWheelsDesignSystem.Typography.labelSmall.copy(
            fontFamily = AppFont.inter
        )
    )
