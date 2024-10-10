package com.msdc.rentalwheels.data.model

data class Car(
    val id: String = "",
    val brand: String = "",
    val model: String = "",
    val category: String = "",
    val year: Int = 0,
    val dailyRate: Int = 0,
    val features: List<String> = emptyList(),
    val imageUrl: String = "",
    val mileage: Int = 0,
    val engine: String = "",
    val transmission: String = "",
    val fuelType: String = "",
    val description: String = "",
    val price: Int = 0,
    val recommended: Boolean = false
)