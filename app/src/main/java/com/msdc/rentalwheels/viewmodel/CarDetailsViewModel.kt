package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {
    data class UiState(
        val car: Car? = null,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    private val _uiState = MutableStateFlow(UiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun loadCar(carId: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            try {
                val car = carRepository.getCarById(carId).first()
                _uiState.value = UiState(car = car)
            } catch (e: Exception) {
                _uiState.value = UiState(error = e.message ?: "Unknown error occurred")
            }
        }
    }
}