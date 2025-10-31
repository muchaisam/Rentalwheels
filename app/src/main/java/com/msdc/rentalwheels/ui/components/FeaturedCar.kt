package com.msdc.rentalwheels.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.data.model.Car

/**
 * Featured Car Component - Now uses PremiumCarCard with Featured variant Maintains the featured
 * styling while using the design system
 */
@Composable
fun FeaturedCar(
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
        variant = CarCardVariant.Featured,
        onFavorite = onFavorite,
        isFavorite = isFavorite,
        showPricing = true
    )
}
