package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.persistence.UserPreferencesManager
import com.msdc.rentalwheels.data.repository.BookingRepository
import com.msdc.rentalwheels.data.repository.CarRepository
import com.msdc.rentalwheels.modules.IoDispatcher
import com.msdc.rentalwheels.uistates.Booking
import com.msdc.rentalwheels.uistates.BookingAnalytics
import com.msdc.rentalwheels.uistates.BookingFilter
import com.msdc.rentalwheels.uistates.BookingStatus
import com.msdc.rentalwheels.uistates.BookingsScreenState
import com.msdc.rentalwheels.uistates.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BookingsViewModel
@Inject
constructor(
    private val repository: BookingRepository,
    private val carRepository: CarRepository,
    private val userPreferencesManager: UserPreferencesManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _bookingsState = MutableStateFlow<BookingsScreenState>(BookingsScreenState.Loading)
    val bookingsState: StateFlow<BookingsScreenState> = _bookingsState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(BookingFilter.ALL)
    private val _isRefreshing = MutableStateFlow(false)
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val _showCartMode = MutableStateFlow(false)

    fun loadBookings() {
        viewModelScope.launch(dispatcher) {
            try {
                _bookingsState.value = BookingsScreenState.Loading

                // Combine bookings and recommended cars from Firestore
                combine(
                    repository.getUserBookings(),
                    carRepository.getRecommendedCars(
                        10
                    ) // Get more recommended cars for better selection
                ) { bookings, recommendedCars ->
                    val now = LocalDateTime.now()

                    // Categorize bookings
                    val upcomingBookings =
                        bookings.filter {
                            it.status == BookingStatus.CONFIRMED && it.startDate.isAfter(now)
                        }

                    val activeBookings =
                        bookings.filter {
                            it.status == BookingStatus.ACTIVE ||
                                    (it.status == BookingStatus.CONFIRMED &&
                                            it.startDate.isBefore(now) &&
                                            it.endDate.isAfter(now))
                        }

                    val pastBookings =
                        bookings.filter {
                            it.status == BookingStatus.COMPLETED ||
                                    (it.endDate.isBefore(now) &&
                                            it.status != BookingStatus.CANCELLED)
                        }

                    val cancelledBookings = bookings.filter { it.status == BookingStatus.CANCELLED }

                    val totalSpent = pastBookings.sumOf { it.totalCost }
                    val hasBookings = bookings.isNotEmpty()

                    // Filter to get available cars from Firestore recommended cars
                    val availableCars = recommendedCars.filter { it.isAvailable }.take(8)

                    BookingsScreenState.Success(
                        allBookings = bookings,
                        upcomingBookings = upcomingBookings,
                        activeBookings = activeBookings,
                        pastBookings = pastBookings,
                        cancelledBookings = cancelledBookings,
                        availableCars =
                        availableCars, // Use real recommended cars from Firestore
                        cartItems = _cartItems.value,
                        isRefreshing = _isRefreshing.value,
                        selectedFilter = _selectedFilter.value,
                        searchQuery = _searchQuery.value,
                        totalSpent = totalSpent,
                        favoriteCarIds = extractFavoriteCarIds(bookings),
                        hasBookings = hasBookings,
                        showCartMode = _showCartMode.value
                    )
                }
                    .collect { state -> _bookingsState.value = state }
            } catch (e: Exception) {
                Timber.e(e, "Error loading bookings")
                _bookingsState.value =
                    BookingsScreenState.Error(
                        message = e.message ?: "Failed to load bookings. Please try again."
                    )
            }
        }
    }

    fun refreshBookings() {
        viewModelScope.launch(dispatcher) {
            try {
                _isRefreshing.value = true
                updateCurrentStateWithRefreshing(true)

                // Reload bookings
                loadBookings()
            } finally {
                _isRefreshing.value = false
                updateCurrentStateWithRefreshing(false)
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.cancelBooking(bookingId)
                // Reload bookings to reflect changes
                loadBookings()
            } catch (e: Exception) {
                Timber.e(e, "Error cancelling booking: $bookingId")
                // Could show a snackbar or error state here
            }
        }
    }

    fun extendBooking(bookingId: String, newEndDate: LocalDateTime? = null) {
        viewModelScope.launch(dispatcher) {
            try {
                val endDate = newEndDate ?: LocalDateTime.now().plusDays(1)
                repository.extendBooking(bookingId, endDate)
                // Reload bookings to reflect changes
                loadBookings()
            } catch (e: Exception) {
                Timber.e(e, "Error extending booking: $bookingId")
            }
        }
    }

    fun modifyBooking(bookingId: String, newStartDate: LocalDateTime, newEndDate: LocalDateTime) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.modifyBooking(bookingId, newStartDate, newEndDate)
                loadBookings()
            } catch (e: Exception) {
                Timber.e(e, "Error modifying booking: $bookingId")
            }
        }
    }

    fun searchBookings(query: String) {
        _searchQuery.value = query
        filterBookings()
    }

    fun setFilter(filter: BookingFilter) {
        _selectedFilter.value = filter
        filterBookings()
    }

    private fun filterBookings() {
        val currentState = _bookingsState.value
        if (currentState is BookingsScreenState.Success) {
            val query = _searchQuery.value
            val filter = _selectedFilter.value

            var filteredBookings =
                when (filter) {
                    BookingFilter.ALL -> currentState.allBookings
                    BookingFilter.UPCOMING -> currentState.upcomingBookings
                    BookingFilter.ACTIVE -> currentState.activeBookings
                    BookingFilter.PAST -> currentState.pastBookings
                    BookingFilter.CANCELLED -> currentState.cancelledBookings
                }

            if (query.isNotBlank()) {
                filteredBookings =
                    filteredBookings.filter { booking ->
                        booking.car.make.contains(query, ignoreCase = true) ||
                                booking.car.model.contains(query, ignoreCase = true) ||
                                booking.referenceNumber.contains(query, ignoreCase = true) ||
                                booking.pickupLocation.contains(query, ignoreCase = true) ||
                                booking.returnLocation.contains(query, ignoreCase = true)
                    }
            }

            _bookingsState.value = currentState.copy(searchQuery = query, selectedFilter = filter)
        }
    }

    fun retryBooking(originalBookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                // Get original booking details and create a new booking
                repository.getBookingById(originalBookingId)?.let { originalBooking ->
                    val newBooking =
                        originalBooking.copy(
                            id = "", // Will be generated by repository
                            status = BookingStatus.PENDING,
                            startDate = LocalDateTime.now().plusDays(1),
                            endDate = LocalDateTime.now().plusDays(2),
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now()
                        )
                    repository.createBooking(newBooking)
                    loadBookings()
                }
            } catch (e: Exception) {
                Timber.e(e, "Error retrying booking: $originalBookingId")
            }
        }
    }

    fun getBookingAnalytics(): BookingAnalytics {
        val currentState = _bookingsState.value
        return if (currentState is BookingsScreenState.Success) {
            val completedBookings = currentState.pastBookings

            BookingAnalytics(
                totalBookings = currentState.allBookings.size,
                totalSpent = currentState.totalSpent,
                averageRentalDuration = calculateAverageRentalDuration(completedBookings),
                mostRentedCarBrand = findMostRentedCarBrand(completedBookings),
                preferredPickupLocation = findPreferredLocation(completedBookings),
                monthlySpending = calculateMonthlySpending(completedBookings),
                favoriteFeatures = extractFavoriteFeatures(completedBookings)
            )
        } else {
            BookingAnalytics()
        }
    }

    private fun updateCurrentStateWithRefreshing(isRefreshing: Boolean) {
        val currentState = _bookingsState.value
        if (currentState is BookingsScreenState.Success) {
            _bookingsState.value = currentState.copy(isRefreshing = isRefreshing)
        }
    }

    private fun extractFavoriteCarIds(bookings: List<Booking>): Set<String> {
        return bookings
            .groupBy { it.carId }
            .filter { it.value.size >= 2 } // Cars booked at least twice
            .keys
            .toSet()
    }

    private fun calculateAverageRentalDuration(bookings: List<Booking>): Int {
        if (bookings.isEmpty()) return 0

        val totalDays =
            bookings.sumOf { booking ->
                java.time.Duration.between(booking.startDate, booking.endDate).toDays()
            }

        return (totalDays / bookings.size).toInt()
    }

    private fun findMostRentedCarBrand(bookings: List<Booking>): String {
        return bookings.groupBy { it.car.make }.maxByOrNull { it.value.size }?.key ?: ""
    }

    private fun findPreferredLocation(bookings: List<Booking>): String {
        return bookings.groupBy { it.pickupLocation }.maxByOrNull { it.value.size }?.key ?: ""
    }

    private fun calculateMonthlySpending(bookings: List<Booking>): Map<String, Double> {
        return bookings
            .groupBy {
                "${it.startDate.year}-${it.startDate.monthValue.toString().padStart(2, '0')}"
            }
            .mapValues { (_, bookingsInMonth) -> bookingsInMonth.sumOf { it.totalCost } }
    }

    private fun extractFavoriteFeatures(bookings: List<Booking>): List<String> {
        return bookings
            .flatMap { it.car.features }
            .groupBy { it }
            .toList()
            .sortedByDescending { it.second.size }
            .take(5)
            .map { it.first }
    }

    // User Action Persistence Methods
    fun saveBookingToFavorites(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                userPreferencesManager.addBookingToFavorites(bookingId)
                Timber.d("Booking $bookingId saved to favorites")
            } catch (e: Exception) {
                Timber.e(e, "Error saving booking to favorites")
            }
        }
    }

    fun removeBookingFromFavorites(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                userPreferencesManager.removeBookingFromFavorites(bookingId)
                Timber.d("Booking $bookingId removed from favorites")
            } catch (e: Exception) {
                Timber.e(e, "Error removing booking from favorites")
            }
        }
    }

    fun trackBookingAction(action: String, bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                userPreferencesManager.trackUserAction(
                    action,
                    mapOf(
                        "bookingId" to bookingId,
                        "timestamp" to System.currentTimeMillis().toString()
                    )
                )
                Timber.d("Tracked booking action: $action for booking $bookingId")
            } catch (e: Exception) {
                Timber.e(e, "Error tracking booking action")
            }
        }
    }

    fun refreshWithFirebaseData() {
        viewModelScope.launch(dispatcher) {
            try {
                _isRefreshing.value = true
                _bookingsState.value = BookingsScreenState.Loading

                // Force reload from Firebase without cache
                repository.refreshUserBookings().collect { bookings ->
                    processBookingsData(bookings)
                }
            } catch (e: Exception) {
                Timber.e(e, "Error refreshing Firebase data")
                _bookingsState.value =
                    BookingsScreenState.Error(message = "Failed to refresh data from server")
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun processBookingsData(bookings: List<Booking>) {
        val now = LocalDateTime.now()

        // Categorize bookings
        val upcomingBookings =
            bookings.filter {
                it.status == BookingStatus.CONFIRMED && it.startDate.isAfter(now)
            }

        val activeBookings =
            bookings.filter {
                it.status == BookingStatus.ACTIVE ||
                        (it.status == BookingStatus.CONFIRMED &&
                                it.startDate.isBefore(now) &&
                                it.endDate.isAfter(now))
            }

        val pastBookings =
            bookings.filter {
                it.status == BookingStatus.COMPLETED ||
                        (it.endDate.isBefore(now) && it.status != BookingStatus.CANCELLED)
            }

        val cancelledBookings = bookings.filter { it.status == BookingStatus.CANCELLED }

        val totalSpent = pastBookings.sumOf { it.totalCost }

        _bookingsState.value =
            BookingsScreenState.Success(
                allBookings = bookings,
                upcomingBookings = upcomingBookings,
                activeBookings = activeBookings,
                pastBookings = pastBookings,
                cancelledBookings = cancelledBookings,
                isRefreshing = _isRefreshing.value,
                selectedFilter = _selectedFilter.value,
                searchQuery = _searchQuery.value,
                totalSpent = totalSpent,
                favoriteCarIds = extractFavoriteCarIds(bookings)
            )
    }

    // Cart Management Methods
    fun toggleCartMode() {
        _showCartMode.value = !_showCartMode.value
        updateCurrentState()
    }

    fun addToCart(car: Car) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItem = currentItems.find { it.car.id == car.id }

        if (existingItem != null) {
            val index = currentItems.indexOf(existingItem)
            currentItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            currentItems.add(
                CartItem(
                    car = car,
                    startDate = LocalDateTime.now().plusDays(1),
                    endDate = LocalDateTime.now().plusDays(2)
                )
            )
        }

        _cartItems.value = currentItems
        updateCurrentState()

        // Track analytics
        trackBookingAction("added_to_cart", car.id)
    }

    fun removeFromCart(carId: String) {
        _cartItems.value = _cartItems.value.filterNot { it.car.id == carId }
        updateCurrentState()
        trackBookingAction("removed_from_cart", carId)
    }

    fun updateCartItem(carId: String, updatedItem: CartItem) {
        val currentItems = _cartItems.value.toMutableList()
        val index = currentItems.indexOfFirst { it.car.id == carId }
        if (index != -1) {
            currentItems[index] = updatedItem
            _cartItems.value = currentItems
            updateCurrentState()
        }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        updateCurrentState()
        trackBookingAction("cart_cleared", "")
    }

    fun getCartTotal(): Double {
        return _cartItems.value.sumOf { it.totalCost }
    }

    fun processCartBookings() {
        viewModelScope.launch(dispatcher) {
            try {
                val cartItems = _cartItems.value
                if (cartItems.isEmpty()) return@launch

                // Create bookings from cart items
                cartItems.forEach { cartItem ->
                    val booking =
                        Booking(
                            carId = cartItem.car.id,
                            car = cartItem.car,
                            status = BookingStatus.PENDING,
                            startDate = cartItem.startDate,
                            endDate = cartItem.endDate,
                            pickupLocation = cartItem.pickupLocation,
                            totalCost = cartItem.totalCost,
                            withDriver = cartItem.withDriver,
                            createdAt = LocalDateTime.now(),
                            updatedAt = LocalDateTime.now()
                        )
                    repository.createBooking(booking)
                }

                // Clear cart and refresh bookings
                clearCart()
                loadBookings()

                // Track analytics
                trackBookingAction("cart_processed", cartItems.size.toString())
            } catch (e: Exception) {
                Timber.e(e, "Error processing cart bookings")
            }
        }
    }

    private fun updateCurrentState() {
        val currentState = _bookingsState.value
        if (currentState is BookingsScreenState.Success) {
            _bookingsState.value =
                currentState.copy(
                    cartItems = _cartItems.value,
                    showCartMode = _showCartMode.value
                )
        }
    }

    fun createQuickBooking(car: Car, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        viewModelScope.launch(dispatcher) {
            try {
                val booking =
                    Booking(
                        carId = car.id,
                        car = car,
                        status = BookingStatus.PENDING,
                        startDate = LocalDateTime.now().plusDays(1), // Default: tomorrow
                        endDate =
                        LocalDateTime.now()
                            .plusDays(2), // Default: day after tomorrow
                        pickupLocation = "Main Office", // Default pickup location
                        returnLocation = "Main Office", // Default return location
                        totalCost = car.pricePerDay * 1, // 1 day rental
                        withDriver = false,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now(),
                        referenceNumber =
                        "RW${System.currentTimeMillis().toString().takeLast(6)}"
                    )

                val bookingId = repository.createBooking(booking)

                // Refresh bookings to show the new booking
                loadBookings()

                // Track analytics
                trackBookingAction("quick_booking_created", car.id)

                onSuccess()
            } catch (e: Exception) {
                Timber.e(e, "Error creating quick booking for car: ${car.id}")
                onError("Failed to create booking: ${e.message}")
            }
        }
    }
}
