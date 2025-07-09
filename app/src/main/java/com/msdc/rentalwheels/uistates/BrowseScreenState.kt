package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.data.model.Car

data class BrowseFilters(
    val selectedCategory: String = "All",
    val selectedFuelType: String = "All",
    val selectedTransmission: String = "All",
    val minPrice: Int = 0,
    val maxPrice: Int = 10000,
    val minYear: Int = 2010,
    val maxYear: Int = 2024,
    val withDriver: Boolean = false,
    val sortBy: SortOption = SortOption.PRICE_LOW_TO_HIGH
)

enum class SortOption(val displayName: String) {
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    YEAR_NEWEST("Newest First"),
    YEAR_OLDEST("Oldest First"),
    BRAND_A_TO_Z("Brand A-Z"),
    BRAND_Z_TO_A("Brand Z-A"),
    MOST_POPULAR("Most Popular")
}

sealed interface BrowseScreenState {
    data object Loading : BrowseScreenState

    data class Success(
        val allCars: List<Car>,
        val filteredCars: List<Car>,
        val availableCategories: List<String>,
        val availableFuelTypes: List<String>,
        val availableTransmissions: List<String>,
        val totalVehicles: Int,
        val filters: BrowseFilters,
        val isLoadingMore: Boolean = false,
        val searchQuery: String = "",
        // Enhanced features
        val favoriteCarIds: Set<String> = emptySet(),
        val selectedCarsForComparison: List<Car> = emptyList(),
        val isRefreshing: Boolean = false
    ) : BrowseScreenState

    data class Error(val message: String) : BrowseScreenState
}
