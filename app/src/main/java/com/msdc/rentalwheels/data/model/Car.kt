package com.msdc.rentalwheels.data.model

data class Car(
    val id: String = "",
    val make: String = "", // Renamed from brand for consistency
    val model: String = "",
    val category: String = "",
    val type: String = "", // SUV, Sedan, Hatchback, etc.
    val year: Int = 0,
    val dailyRate: Int = 0,
    val pricePerDay: Double = 0.0, // More precise pricing
    val features: List<String> = emptyList(),
    val imageUrl: String = "",
    val mileage: Int = 0,
    val engine: String = "",
    val transmission: String = "",
    val fuelType: String = "",
    val description: String = "",
    val price: Int = 0, // Keep for backward compatibility
    val recommended: Boolean = false,
    // New features for enhanced functionality
    val isElectric: Boolean = false,
    val hasGPS: Boolean = false,
    val hasAirConditioning: Boolean = true,
    val hasBluetoothConnectivity: Boolean = false,
    val seatingCapacity: Int = 5,
    val isAvailable: Boolean = true,
    val rating: Float = 0.0f,
    val reviewCount: Int = 0,
    val location: String = "",
    val owner: String = "",
    val insuranceIncluded: Boolean = true,
    val depositRequired: Double = 0.0,
    val cancellationPolicy: String = "Free cancellation up to 24 hours before pickup",
    val imageUrls: List<String> = emptyList(), // Multiple images
    val specifications: CarSpecifications = CarSpecifications(),
    val availability: CarAvailability = CarAvailability()
)

data class CarSpecifications(
    val fuelCapacity: String = "",
    val topSpeed: String = "",
    val acceleration: String = "", // 0-100 km/h
    val safetyRating: Int = 0, // Out of 5
    val dimensions: CarDimensions = CarDimensions(),
    val weight: String = "",
    val driveType: String = "" // FWD, RWD, AWD
)

data class CarDimensions(
    val length: String = "",
    val width: String = "",
    val height: String = "",
    val wheelbase: String = "",
    val groundClearance: String = ""
)

data class CarAvailability(
    val isAvailable: Boolean = true,
    val availableFrom: String = "",
    val availableUntil: String = "",
    val blockedDates: List<String> = emptyList(),
    val minimumRentalDays: Int = 1,
    val maximumRentalDays: Int = 30
)

// Extension properties for backward compatibility
val Car.brand: String
    get() = make
