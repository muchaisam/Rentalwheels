package com.msdc.rentalwheels.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.data.model.Car

/**
 * Professional Recommended Car Item Now uses PremiumCarCard with compact variant for consistency
 */
@Composable
fun RecommendedCarItem(
    car: Car,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onFavorite: (() -> Unit)? = null,
    isFavorite: Boolean = false
) {
    PremiumCarCard(
        car = car,
        onClick = onClick,
        modifier = modifier,
        variant = CarCardVariant.Compact,
        onFavorite = onFavorite,
        isFavorite = isFavorite,
        showPricing = true
    )
}
