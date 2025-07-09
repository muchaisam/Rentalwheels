package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.repository.BookingRepository
import com.msdc.rentalwheels.modules.IoDispatcher
import com.msdc.rentalwheels.uistates.Booking
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BookingDetailViewModel
@Inject
constructor(
    private val repository: BookingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _bookingState = MutableStateFlow<BookingDetailState>(BookingDetailState.Loading)
    val bookingState: StateFlow<BookingDetailState> = _bookingState.asStateFlow()

    sealed interface BookingDetailState {
        data object Loading : BookingDetailState
        data class Success(val booking: Booking) : BookingDetailState
        data class Error(val message: String) : BookingDetailState
    }

    fun loadBooking(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                _bookingState.value = BookingDetailState.Loading

                val booking = repository.getBookingById(bookingId)
                if (booking != null) {
                    _bookingState.value = BookingDetailState.Success(booking)
                } else {
                    _bookingState.value = BookingDetailState.Error("Booking not found")
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading booking: $bookingId")
                _bookingState.value =
                    BookingDetailState.Error(e.message ?: "Failed to load booking details")
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.cancelBooking(bookingId)
                // Reload booking to reflect changes
                loadBooking(bookingId)
            } catch (e: Exception) {
                Timber.e(e, "Error cancelling booking: $bookingId")
            }
        }
    }

    fun extendBooking(bookingId: String) {
        viewModelScope.launch(dispatcher) {
            try {
                repository.extendBooking(bookingId)
                // Reload booking to reflect changes
                loadBooking(bookingId)
            } catch (e: Exception) {
                Timber.e(e, "Error extending booking: $bookingId")
            }
        }
    }
}
