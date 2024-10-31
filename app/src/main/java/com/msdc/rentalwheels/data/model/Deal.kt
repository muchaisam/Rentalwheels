package com.msdc.rentalwheels.data.model



data class Deal(
    val id: String = "",
    val carId: String = "",
    val title: String = "",
    val description: String = "",
    val discountPercentage: Int = 0,
    val discountedRate: Int = 0,
    val originalRate: Int = 0,
    val validFrom: String = "",
    val validTo: String = "",
    val imageUrl: String = ""
)