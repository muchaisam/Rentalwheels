package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.data.model.Car

sealed class CarDetailState {
    object Initial : CarDetailState()
    object Loading : CarDetailState()
    data class Success(val car: Car) : CarDetailState()
    data class Error(val message: String) : CarDetailState()
}