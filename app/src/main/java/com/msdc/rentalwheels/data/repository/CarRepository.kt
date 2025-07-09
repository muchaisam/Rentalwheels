package com.msdc.rentalwheels.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    // Helper function to safely deserialize Car documents
    private fun deserializeCar(document: DocumentSnapshot): Car? {
        return try {
            val carData = document.data ?: return null

            Car(
                id = document.getString("id") ?: document.id,
                make = document.getString("make") ?: document.getString("brand") ?: "",
                model = document.getString("model") ?: "",
                category = document.getString("category") ?: "",
                type = document.getString("type") ?: "",
                year = document.getLong("year")?.toInt() ?: 0,
                dailyRate = document.getLong("dailyRate")?.toInt() ?: 0,
                pricePerDay = document.getDouble("pricePerDay") ?: 0.0,
                features = document.get("features") as? List<String> ?: emptyList(),
                imageUrl = document.getString("imageUrl") ?: "",
                mileage = document.getLong("mileage")?.toInt() ?: 0,
                engine = document.getString("engine") ?: "",
                transmission = document.getString("transmission") ?: "",
                fuelType = document.getString("fuelType") ?: "",
                description = document.getString("description") ?: "",
                price = document.getLong("price")?.toInt() ?: 0,
                // Handle the recommended field conversion from String to Boolean
                recommended =
                when (val rec = document.get("recommended")) {
                    is Boolean -> rec
                    is String -> rec.lowercase() == "true"
                    else -> false
                },
                isElectric = document.getBoolean("isElectric") ?: false,
                hasGPS = document.getBoolean("hasGPS") ?: false,
                hasAirConditioning = document.getBoolean("hasAirConditioning") ?: true,
                hasBluetoothConnectivity = document.getBoolean("hasBluetoothConnectivity")
                    ?: false,
                seatingCapacity = document.getLong("seatingCapacity")?.toInt() ?: 5,
                isAvailable = document.getBoolean("isAvailable") ?: true,
                rating = document.getDouble("rating")?.toFloat() ?: 0.0f,
                reviewCount = document.getLong("reviewCount")?.toInt() ?: 0,
                location = document.getString("location") ?: "",
                owner = document.getString("owner") ?: "",
                insuranceIncluded = document.getBoolean("insuranceIncluded") ?: true,
                depositRequired = document.getDouble("depositRequired") ?: 0.0,
                cancellationPolicy = document.getString("cancellationPolicy")
                    ?: "Free cancellation up to 24 hours before pickup",
                imageUrls = document.get("imageUrls") as? List<String> ?: emptyList()
            )
        } catch (e: Exception) {
            Timber.e(e, "Error deserializing car document: ${document.id}")
            null
        }
    }

    fun getCars(limit: Long = 10, lastDocumentId: String? = null): Flow<List<Car>> = flow {
        val query = firestore.collection("cars").orderBy("brand").limit(limit)

        lastDocumentId?.let { id ->
            val lastDoc = firestore.collection("cars").document(id).get().await()
            query.startAfter(lastDoc)
        }

        val snapshot = query.get().await()
        val cars = snapshot.documents.mapNotNull { document -> deserializeCar(document) }
        emit(cars)
    }

    fun getDeals(): Flow<List<Deal>> = flow {
        val snapshot =
            firestore
                .collection("deals")
                .orderBy("discountPercentage", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .await()
        val deals = snapshot.toObjects(Deal::class.java)
        emit(deals)
    }

    // In repository
    fun getCategories(): Flow<List<Category>> = flow {
        try {
            val snapshot = firestore.collection("categories").get().await()
            val categories = snapshot.toObjects(Category::class.java)

            emit(categories)
        } catch (e: Exception) {
            Timber.tag("CategoryRepo").e(e, "Error fetching categories")
            emit(emptyList())
        }
    }

    fun getRecommendedCars(limit: Long = 5): Flow<List<Car>> = flow {
        // Try querying for boolean true first
        var snapshot =
            firestore
                .collection("cars")
                .whereEqualTo("recommended", true)
                .limit(limit)
                .get()
                .await()

        var cars = snapshot.documents.mapNotNull { document -> deserializeCar(document) }

        // If no results, try querying for string "true"
        if (cars.isEmpty()) {
            snapshot =
                firestore
                    .collection("cars")
                    .whereEqualTo("recommended", "true")
                    .limit(limit)
                    .get()
                    .await()
            cars = snapshot.documents.mapNotNull { document -> deserializeCar(document) }
        }

        emit(cars)
    }

    fun getCarById(carId: String): Flow<Car?> = flow {
        val carDoc = firestore.collection("cars").document(carId).get().await()
        val car = deserializeCar(carDoc)
        emit(car)
    }

    fun getCarsByFuelType(fuelType: String): Flow<List<Car>> = flow {
        val snapshot = firestore.collection("cars").whereEqualTo("fuelType", fuelType).get().await()
        val cars = snapshot.documents.mapNotNull { document -> deserializeCar(document) }
        emit(cars)
    }

    fun getCarsByYear(year: Int): Flow<List<Car>> = flow {
        val snapshot = firestore.collection("cars").whereEqualTo("year", year).get().await()
        val cars = snapshot.documents.mapNotNull { document -> deserializeCar(document) }
        emit(cars)
    }
}
