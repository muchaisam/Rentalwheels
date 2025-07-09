package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal
import com.msdc.rentalwheels.data.repository.CarRepository
import com.msdc.rentalwheels.modules.IoDispatcher
import com.msdc.rentalwheels.uistates.CarDetailState
import com.msdc.rentalwheels.uistates.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CarViewModel @Inject constructor(
    private val repository: CarRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _carDetailState = MutableStateFlow<CarDetailState>(CarDetailState.Loading)
    val carDetailState: StateFlow<CarDetailState> = _carDetailState
    private val _homeState = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
    val homeState = _homeState.asStateFlow()

    private val _selectedCarId = MutableStateFlow<String?>(null)
    val selectedCar: StateFlow<Car?> = _selectedCarId
        .filterNotNull()
        .flatMapLatest { carId ->
            repository.getCarById(carId)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            null
        )

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch(dispatcher) {
            try {
                combine(
                    repository.getCategories(),
                    repository.getCars(),
                    repository.getDeals()
                ) { categories, cars, deals ->
                    Triple(categories, cars, deals)
                }.collect { (categories, cars, deals) ->
                    val carsByFuelType = cars.groupBy { it.fuelType }
                    val carsByYear = cars.groupBy { it.year.toString() }
                    val recommendedCars = cars.filter { it.recommended }

                    _homeState.value = HomeScreenState.Success(
                        categories = categories,
                        recommendedCars = recommendedCars,
                        carsByFuelType = carsByFuelType,
                        carsByYear = carsByYear,
                        deals = deals
                    )
                }
            } catch (e: Exception) {
                _homeState.value = HomeScreenState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun selectCar(carId: String) {
        _selectedCarId.value = carId
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
                _carDetailState.value =
                    CarDetailState.Error(e.message ?: "An unknown error occurred")
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


    class Factory(
        private val repository: CarRepository,
        private val dispatchers: CoroutineDispatcher
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarViewModel(repository, dispatchers) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}