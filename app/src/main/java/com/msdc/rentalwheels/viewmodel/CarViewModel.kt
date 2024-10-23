package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal
import com.msdc.rentalwheels.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CarViewModel(private val repository: CarRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _carDetailState = MutableStateFlow<CarDetailState>(CarDetailState.Loading)
    val carDetailState: StateFlow<CarDetailState> = _carDetailState

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories().first()
                val recommendedCars = repository.getRecommendedCars().first()
                val cars = repository.getCars().first()
                val deals = repository.getDeals().first()
                _uiState.value = UiState.Success(
                    categories = categories,
                    recommendedCars = recommendedCars,
                    cars = cars,
                    deals = deals
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun loadCarDetails(carId: String) {
        viewModelScope.launch {
            _carDetailState.value = CarDetailState.Loading
            try {
                val car = repository.getCarById(carId).first()
                if (car != null) {
                    _carDetailState.value = CarDetailState.Success(car)
                } else {
                    _carDetailState.value = CarDetailState.Error("Car not found")
                }
            } catch (e: Exception) {
                _carDetailState.value = CarDetailState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                // Fetch fresh data from your repository
                val categories = repository.getCategories().first()
                val recommendedCars = repository.getRecommendedCars().first()
                val cars = repository.getCars().first()
                val deals = repository.getDeals().first()

                _uiState.value = UiState.Success(
                    categories = categories,
                    recommendedCars = recommendedCars,
                    cars = cars,
                    deals = deals
                )
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Failed to refresh data: ${e.message}")
            }
        }
    }

    fun loadMoreCars() {
        viewModelScope.launch {
            // Implement logic to load more cars
            // Update the uiState with the new cars
        }
    }

    fun clearCarDetails() {
        _carDetailState.value = CarDetailState.Initial
    }


    sealed class UiState {
        object Loading : UiState()
        data class Success(
            val categories: List<Category>,
            val recommendedCars: List<Car>,
            val cars: List<Car>,
            val deals: List<Deal>,
            val loadMoreError: String? = null
        ) : UiState()
        data class Error(val message: String) : UiState()
    }

    sealed class CarDetailState {
        object Initial : CarDetailState()
        object Loading : CarDetailState()
        data class Success(val car: Car) : CarDetailState()
        data class Error(val message: String) : CarDetailState()
    }

    class Factory(private val repository: CarRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}