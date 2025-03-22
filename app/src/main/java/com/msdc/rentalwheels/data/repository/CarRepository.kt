package com.msdc.rentalwheels.data.repository

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
    fun getCars(limit: Long = 10, lastDocumentId: String? = null): Flow<List<Car>> = flow {
        val query = firestore.collection("cars")
            .orderBy("brand")
            .limit(limit)

        lastDocumentId?.let { id ->
            val lastDoc = firestore.collection("cars").document(id).get().await()
            query.startAfter(lastDoc)
        }

        val snapshot = query.get().await()
        val cars = snapshot.toObjects(Car::class.java)
        emit(cars)
    }

    fun getDeals(): Flow<List<Deal>> = flow {
        val snapshot = firestore.collection("deals")
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
            val snapshot = firestore.collection("categories")
                .get()
                .await()
            val categories = snapshot.toObjects(Category::class.java)

            emit(categories)
        } catch (e: Exception) {
            Timber.tag("CategoryRepo").e(e, "Error fetching categories")
            emit(emptyList())
        }
    }

    fun getRecommendedCars(limit: Long = 5): Flow<List<Car>> = flow {
        val snapshot = firestore.collection("cars")
            .whereEqualTo("recommended", true)
            .limit(limit)
            .get()
            .await()
        val cars = snapshot.documents.mapNotNull { doc ->
            doc.toObject(Car::class.java)?.copy(id = doc.id)
        }
        emit(cars)
    }

    fun getCarById(carId: String): Flow<Car?> = flow {
        val carDoc = firestore.collection("cars").document(carId).get().await()
        val car = carDoc.toObject(Car::class.java)
        emit(car)
    }

    fun getCarsByFuelType(fuelType: String): Flow<List<Car>> = flow {
        val snapshot = firestore.collection("cars")
            .whereEqualTo("fuelType", fuelType)
            .get()
            .await()
        val cars = snapshot.toObjects(Car::class.java)
        emit(cars)
    }

    fun getCarsByYear(year: Int): Flow<List<Car>> = flow {
        val snapshot = firestore.collection("cars")
            .whereEqualTo("year", year)
            .get()
            .await()
        val cars = snapshot.toObjects(Car::class.java)
        emit(cars)
    }
}