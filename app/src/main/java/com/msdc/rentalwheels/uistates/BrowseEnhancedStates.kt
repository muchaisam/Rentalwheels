package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.data.model.Car

/** State for managing car favorites/wishlist functionality */
data class FavoritesState(
    val favoriteCarIds: Set<String> = emptySet(),
    val isLoading: Boolean = false
) {
    fun isFavorite(carId: String): Boolean = favoriteCarIds.contains(carId)
}

/** State for managing car comparison functionality */
data class ComparisonState(
    val selectedCars: List<Car> = emptyList(),
    val isComparing: Boolean = false,
    val maxComparisons: Int = 3
) {
    val canAddMore: Boolean
        get() = selectedCars.size < maxComparisons
    val hasSelections: Boolean
        get() = selectedCars.isNotEmpty()

    fun canAddCar(carId: String): Boolean {
        return canAddMore && !selectedCars.any { it.id == carId }
    }

    fun getCarIndex(carId: String): Int {
        return selectedCars.indexOfFirst { it.id == carId }
    }
}

/** Enhanced range filter state for sliders */
data class RangeFilterState(
    val priceRange: Pair<Float, Float> = Pair(0f, 10000f),
    val yearRange: Pair<Float, Float> = Pair(2010f, 2024f),
    val maxPrice: Float = 10000f,
    val minPrice: Float = 0f,
    val maxYear: Float = 2024f,
    val minYear: Float = 2010f
) {
    val currentPriceRange: String
        get() = "$${priceRange.first.toInt()} - $${priceRange.second.toInt()}"

    val currentYearRange: String
        get() = "${yearRange.first.toInt()} - ${yearRange.second.toInt()}"
}

/** State for pull-to-refresh functionality */
data class RefreshState(val isRefreshing: Boolean = false, val lastRefreshTime: Long = 0L) {
    val canRefresh: Boolean
        get() =
            !isRefreshing &&
                    (System.currentTimeMillis() - lastRefreshTime) > 2000 // 2 second cooldown
}
