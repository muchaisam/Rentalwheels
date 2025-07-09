package com.msdc.rentalwheels.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.uistates.Booking
import com.msdc.rentalwheels.uistates.BookingStatus
import com.msdc.rentalwheels.uistates.DriverInfo
import com.msdc.rentalwheels.uistates.PaymentStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepository
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) {

    fun getUserBookings(userId: String? = null): Flow<List<Booking>> = flow {
        try {
            val currentUserId = userId ?: auth.currentUser?.uid

            if (currentUserId == null) {
                emit(emptyList())
                return@flow
            }

            val query =
                firestore
                    .collection("bookings")
                    .whereEqualTo("userId", currentUserId)
                    .orderBy("createdAt", Query.Direction.DESCENDING)

            val snapshot = query.get().await()
            val bookings =
                snapshot.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        parseBookingFromFirestore(doc.id, data)
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing booking document: ${doc.id}")
                        null
                    }
                }
            emit(bookings)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching user bookings")
            emit(emptyList()) // Emit empty list on error
        }
    }

    fun refreshUserBookings(userId: String? = null): Flow<List<Booking>> = flow {
        try {
            val currentUserId = userId ?: auth.currentUser?.uid

            if (currentUserId != null) {
                val query =
                    firestore
                        .collection("bookings")
                        .whereEqualTo("userId", currentUserId)
                        .orderBy("createdAt", Query.Direction.DESCENDING)

                val snapshot = query.get().await()
                val bookings =
                    snapshot.documents.mapNotNull { doc ->
                        try {
                            val data = doc.data ?: return@mapNotNull null
                            parseBookingFromFirestore(doc.id, data)
                        } catch (e: Exception) {
                            Timber.e(e, "Error parsing booking document: ${doc.id}")
                            null
                        }
                    }

                emit(bookings)
            } else {
                emit(emptyList())
            }
        } catch (e: Exception) {
            Timber.e(e, "Error refreshing user bookings from Firebase")
            emit(emptyList())
        }
    }

    suspend fun getBookingById(bookingId: String): Booking? {
        return try {
            val snapshot = firestore.collection("bookings").document(bookingId).get().await()

            val data = snapshot.data
            if (data != null) {
                parseBookingFromFirestore(snapshot.id, data)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error fetching booking: $bookingId")
            null
        }
    }

    suspend fun createBooking(booking: Booking): String {
        return try {
            val userId =
                auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

            val bookingData =
                mapOf(
                    "userId" to userId,
                    "carId" to booking.carId,
                    "status" to booking.status.name,
                    "startDate" to booking.startDate.toString(),
                    "endDate" to booking.endDate.toString(),
                    "pickupLocation" to booking.pickupLocation,
                    "returnLocation" to booking.returnLocation,
                    "totalCost" to booking.totalCost,
                    "paymentStatus" to booking.paymentStatus.name,
                    "withDriver" to booking.withDriver,
                    "specialRequests" to booking.specialRequests,
                    "createdAt" to LocalDateTime.now().toString(),
                    "updatedAt" to LocalDateTime.now().toString(),
                    "referenceNumber" to booking.referenceNumber
                )

            val docRef = firestore.collection("bookings").add(bookingData).await()

            // Also save to user's favorites for persistence
            saveUserAction("created_booking", booking.carId)

            docRef.id
        } catch (e: Exception) {
            Timber.e(e, "Error creating booking")
            throw e
        }
    }

    suspend fun cancelBooking(bookingId: String) {
        try {
            firestore
                .collection("bookings")
                .document(bookingId)
                .update(
                    mapOf(
                        "status" to BookingStatus.CANCELLED.name,
                        "updatedAt" to LocalDateTime.now().toString()
                    )
                )
                .await()

            saveUserAction("cancelled_booking", bookingId)
        } catch (e: Exception) {
            Timber.e(e, "Error cancelling booking: $bookingId")
            throw e
        }
    }

    suspend fun extendBooking(bookingId: String, newEndDate: LocalDateTime? = null) {
        try {
            val endDate = newEndDate ?: LocalDateTime.now().plusDays(1)

            firestore
                .collection("bookings")
                .document(bookingId)
                .update(
                    mapOf(
                        "endDate" to endDate.toString(),
                        "status" to BookingStatus.MODIFIED.name,
                        "updatedAt" to LocalDateTime.now().toString()
                    )
                )
                .await()

            saveUserAction("extended_booking", bookingId)
        } catch (e: Exception) {
            Timber.e(e, "Error extending booking: $bookingId")
            throw e
        }
    }

    suspend fun modifyBooking(
        bookingId: String,
        newStartDate: LocalDateTime,
        newEndDate: LocalDateTime
    ) {
        try {
            firestore
                .collection("bookings")
                .document(bookingId)
                .update(
                    mapOf(
                        "startDate" to newStartDate.toString(),
                        "endDate" to newEndDate.toString(),
                        "status" to BookingStatus.MODIFIED.name,
                        "updatedAt" to LocalDateTime.now().toString()
                    )
                )
                .await()

            saveUserAction("modified_booking", bookingId)
        } catch (e: Exception) {
            Timber.e(e, "Error modifying booking: $bookingId")
            throw e
        }
    }

    // User action persistence for analytics
    private suspend fun saveUserAction(action: String, targetId: String) {
        try {
            val userId = auth.currentUser?.uid ?: return

            val actionData =
                mapOf(
                    "userId" to userId,
                    "action" to action,
                    "targetId" to targetId,
                    "timestamp" to LocalDateTime.now().toString()
                )

            firestore.collection("user_actions").add(actionData).await()
        } catch (e: Exception) {
            Timber.e(e, "Error saving user action: $action")
        }
    }

    suspend fun saveCarToFavorites(carId: String) {
        saveUserAction("added_to_favorites", carId)
    }

    suspend fun removeCarFromFavorites(carId: String) {
        saveUserAction("removed_from_favorites", carId)
    }

    suspend fun getUserFavorites(): Set<String> {
        return try {
            val userId = auth.currentUser?.uid ?: return emptySet()

            val snapshot =
                firestore
                    .collection("user_actions")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("action", "added_to_favorites")
                    .get()
                    .await()

            snapshot.documents.mapNotNull { doc -> doc.getString("targetId") }.toSet()
        } catch (e: Exception) {
            Timber.e(e, "Error fetching user favorites")
            emptySet()
        }
    }

    private fun parseBookingFromFirestore(id: String, data: Map<String, Any>): Booking {
        return Booking(
            id = id,
            carId = data["carId"] as? String ?: "",
            car = parseCar(data["car"] as? Map<String, Any> ?: emptyMap()),
            status = parseBookingStatus(data["status"] as? String),
            startDate = parseDateTime(data["startDate"] as? String),
            endDate = parseDateTime(data["endDate"] as? String),
            pickupLocation = data["pickupLocation"] as? String ?: "",
            returnLocation = data["returnLocation"] as? String ?: "",
            totalCost = (data["totalCost"] as? Number)?.toDouble() ?: 0.0,
            paymentStatus = parsePaymentStatus(data["paymentStatus"] as? String),
            withDriver = data["withDriver"] as? Boolean ?: false,
            driverInfo = parseDriverInfo(data["driverInfo"] as? Map<String, Any>),
            specialRequests = data["specialRequests"] as? String ?: "",
            createdAt = parseDateTime(data["createdAt"] as? String),
            updatedAt = parseDateTime(data["updatedAt"] as? String),
            referenceNumber = data["referenceNumber"] as? String ?: ""
        )
    }

    private fun parseCar(data: Map<String, Any>): Car {
        return Car(
            id = data["id"] as? String ?: "",
            make = data["make"] as? String ?: "",
            model = data["model"] as? String ?: "",
            year = (data["year"] as? Number)?.toInt() ?: 2020,
            pricePerDay = (data["pricePerDay"] as? Number)?.toDouble() ?: 0.0,
            imageUrl = data["imageUrl"] as? String ?: "",
            fuelType = data["fuelType"] as? String ?: "Gasoline",
            type = data["type"] as? String ?: "SUV"
        )
    }

    private fun parseBookingStatus(status: String?): BookingStatus {
        return try {
            BookingStatus.valueOf(status ?: "PENDING")
        } catch (e: Exception) {
            BookingStatus.PENDING
        }
    }

    private fun parsePaymentStatus(status: String?): PaymentStatus {
        return try {
            PaymentStatus.valueOf(status ?: "PENDING")
        } catch (e: Exception) {
            PaymentStatus.PENDING
        }
    }

    private fun parseDateTime(dateString: String?): LocalDateTime {
        return try {
            LocalDateTime.parse(dateString ?: LocalDateTime.now().toString())
        } catch (e: Exception) {
            LocalDateTime.now()
        }
    }

    private fun parseDriverInfo(data: Map<String, Any>?): DriverInfo? {
        return if (data != null) {
            DriverInfo(
                id = data["id"] as? String ?: "",
                name = data["name"] as? String ?: "",
                phoneNumber = data["phoneNumber"] as? String ?: "",
                rating = (data["rating"] as? Number)?.toFloat() ?: 0f,
                imageUrl = data["imageUrl"] as? String ?: "",
                experience = data["experience"] as? String ?: ""
            )
        } else null
    }

    fun getBookingsByStatus(status: BookingStatus): Flow<List<Booking>> = flow {
        try {
            val userId = auth.currentUser?.uid

            if (userId == null) {
                emit(emptyList())
                return@flow
            }

            val query =
                firestore
                    .collection("bookings")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("status", status.name)
                    .orderBy("startDate", Query.Direction.ASCENDING)
                    .get()
                    .await()

            val bookings =
                query.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        parseBookingFromFirestore(doc.id, data)
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing booking document: ${doc.id}")
                        null
                    }
                }

            emit(bookings)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching bookings by status: $status")
            emit(emptyList())
        }
    }

    fun getBookingsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<Booking>> = flow {
        try {
            val userId = auth.currentUser?.uid

            if (userId == null) {
                emit(emptyList())
                return@flow
            }

            val query =
                firestore
                    .collection("bookings")
                    .whereEqualTo("userId", userId)
                    .whereGreaterThanOrEqualTo("startDate", startDate.toString())
                    .whereLessThanOrEqualTo("endDate", endDate.toString())
                    .orderBy("startDate", Query.Direction.ASCENDING)
                    .get()
                    .await()

            val bookings =
                query.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        parseBookingFromFirestore(doc.id, data)
                    } catch (e: Exception) {
                        Timber.e(e, "Error parsing booking document: ${doc.id}")
                        null
                    }
                }

            emit(bookings)
        } catch (e: Exception) {
            Timber.e(e, "Error fetching bookings by date range: $startDate - $endDate")
            emit(emptyList())
        }
    }
}
