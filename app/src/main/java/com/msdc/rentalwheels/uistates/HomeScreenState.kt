package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Success(
        val categories: List<Category>,
        val recommendedCars: List<Car>,
        val carsByFuelType: Map<String, List<Car>>,
        val carsByYear: Map<String, List<Car>>,
        val deals: List<Deal>,
        val isLoadingMore: Boolean = false,
        val loadMoreError: String? = null
    ) : HomeScreenState
    data class Error(val message: String) : HomeScreenState
}