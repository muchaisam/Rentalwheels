package com.msdc.rentalwheels.data.persistence

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesManager
@Inject
constructor(@ApplicationContext private val context: Context, private val gson: Gson) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    private val _favoriteCarIds = MutableStateFlow(getFavoriteCarIds())
    val favoriteCarIds: Flow<Set<String>> = _favoriteCarIds.asStateFlow()

    private val _searchHistory = MutableStateFlow(getSearchHistory())
    val searchHistory: Flow<List<String>> = _searchHistory.asStateFlow()

    private val _recentFilters = MutableStateFlow(getRecentFilters())
    val recentFilters: Flow<Map<String, Any>> = _recentFilters.asStateFlow()

    private val _favoriteBookingIds = MutableStateFlow(getFavoriteBookingIds())
    val favoriteBookingIds: Flow<Set<String>> = _favoriteBookingIds.asStateFlow()

    companion object {
        private const val KEY_FAVORITE_CAR_IDS = "favorite_car_ids"
        private const val KEY_FAVORITE_BOOKING_IDS = "favorite_booking_ids"
        private const val KEY_SEARCH_HISTORY = "search_history"
        private const val KEY_RECENT_FILTERS = "recent_filters"
        private const val KEY_BOOKING_PREFERENCES = "booking_preferences"
        private const val KEY_USER_ANALYTICS = "user_analytics"
        private const val KEY_USER_ACTIONS = "user_actions"
        private const val MAX_SEARCH_HISTORY = 10
    }

    // Favorite Cars Management
    fun addToFavorites(carId: String) {
        val currentFavorites = getFavoriteCarIds().toMutableSet()
        currentFavorites.add(carId)
        saveFavoriteCarIds(currentFavorites)
        _favoriteCarIds.value = currentFavorites
    }

    fun removeFromFavorites(carId: String) {
        val currentFavorites = getFavoriteCarIds().toMutableSet()
        currentFavorites.remove(carId)
        saveFavoriteCarIds(currentFavorites)
        _favoriteCarIds.value = currentFavorites
    }

    fun isCarFavorite(carId: String): Boolean {
        return getFavoriteCarIds().contains(carId)
    }

    private fun getFavoriteCarIds(): Set<String> {
        val favoritesJson = prefs.getString(KEY_FAVORITE_CAR_IDS, null)
        return if (favoritesJson != null) {
            try {
                val type = object : TypeToken<Set<String>>() {}.type
                gson.fromJson(favoritesJson, type) ?: emptySet()
            } catch (e: Exception) {
                emptySet()
            }
        } else {
            emptySet()
        }
    }

    private fun saveFavoriteCarIds(favoriteIds: Set<String>) {
        val favoritesJson = gson.toJson(favoriteIds)
        prefs.edit { putString(KEY_FAVORITE_CAR_IDS, favoritesJson) }
    }

    // Search History Management
    fun addSearchQuery(query: String) {
        if (query.isBlank()) return

        val currentHistory = getSearchHistory().toMutableList()
        currentHistory.remove(query) // Remove if exists to avoid duplicates
        currentHistory.add(0, query) // Add to beginning

        // Keep only the most recent searches
        val trimmedHistory = currentHistory.take(MAX_SEARCH_HISTORY)
        saveSearchHistory(trimmedHistory)
        _searchHistory.value = trimmedHistory
    }

    fun clearSearchHistory() {
        prefs.edit { remove(KEY_SEARCH_HISTORY) }
        _searchHistory.value = emptyList()
    }

    private fun getSearchHistory(): List<String> {
        val historyJson = prefs.getString(KEY_SEARCH_HISTORY, null)
        return if (historyJson != null) {
            try {
                val type = object : TypeToken<List<String>>() {}.type
                gson.fromJson(historyJson, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun saveSearchHistory(history: List<String>) {
        val historyJson = gson.toJson(history)
        prefs.edit { putString(KEY_SEARCH_HISTORY, historyJson) }
    }

    // Filter Preferences Management
    fun saveRecentFilters(filters: Map<String, Any>) {
        val filtersJson = gson.toJson(filters)
        prefs.edit { putString(KEY_RECENT_FILTERS, filtersJson) }
        _recentFilters.value = filters
    }

    private fun getRecentFilters(): Map<String, Any> {
        val filtersJson = prefs.getString(KEY_RECENT_FILTERS, null)
        return if (filtersJson != null) {
            try {
                val type = object : TypeToken<Map<String, Any>>() {}.type
                gson.fromJson(filtersJson, type) ?: emptyMap()
            } catch (e: Exception) {
                emptyMap()
            }
        } else {
            emptyMap()
        }
    }

    // Booking Preferences
    fun saveBookingPreferences(preferences: BookingPreferences) {
        val preferencesJson = gson.toJson(preferences)
        prefs.edit { putString(KEY_BOOKING_PREFERENCES, preferencesJson) }
    }

    fun getBookingPreferences(): BookingPreferences {
        val preferencesJson = prefs.getString(KEY_BOOKING_PREFERENCES, null)
        return if (preferencesJson != null) {
            try {
                gson.fromJson(preferencesJson, BookingPreferences::class.java)
                    ?: BookingPreferences()
            } catch (e: Exception) {
                BookingPreferences()
            }
        } else {
            BookingPreferences()
        }
    }

    // Favorite Bookings Management
    fun addBookingToFavorites(bookingId: String) {
        val currentFavorites = getFavoriteBookingIds().toMutableSet()
        currentFavorites.add(bookingId)
        saveFavoriteBookingIds(currentFavorites)
        _favoriteBookingIds.value = currentFavorites
    }

    fun removeBookingFromFavorites(bookingId: String) {
        val currentFavorites = getFavoriteBookingIds().toMutableSet()
        currentFavorites.remove(bookingId)
        saveFavoriteBookingIds(currentFavorites)
        _favoriteBookingIds.value = currentFavorites
    }

    fun isFavoriteBooking(bookingId: String): Boolean {
        return getFavoriteBookingIds().contains(bookingId)
    }

    private fun getFavoriteBookingIds(): Set<String> {
        val favoritesJson = prefs.getString(KEY_FAVORITE_BOOKING_IDS, null)
        return if (favoritesJson != null) {
            try {
                val type = object : TypeToken<Set<String>>() {}.type
                gson.fromJson(favoritesJson, type) ?: emptySet()
            } catch (e: Exception) {
                emptySet()
            }
        } else {
            emptySet()
        }
    }

    private fun saveFavoriteBookingIds(favoriteIds: Set<String>) {
        val favoritesJson = gson.toJson(favoriteIds)
        prefs.edit { putString(KEY_FAVORITE_BOOKING_IDS, favoritesJson) }
    }

    // User Actions Tracking
    fun trackUserAction(action: String, data: Map<String, String>) {
        val actionData =
            mapOf(
                "action" to action,
                "timestamp" to System.currentTimeMillis().toString(),
                "data" to data
            )

        val currentActions = getUserActions().toMutableList()
        currentActions.add(0, actionData) // Add to beginning

        // Keep only the most recent 100 actions
        val trimmedActions = currentActions.take(100)
        saveUserActions(trimmedActions)
    }

    private fun getUserActions(): List<Map<String, Any>> {
        val actionsJson = prefs.getString(KEY_USER_ACTIONS, null)
        return if (actionsJson != null) {
            try {
                val type = object : TypeToken<List<Map<String, Any>>>() {}.type
                gson.fromJson(actionsJson, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun saveUserActions(actions: List<Map<String, Any>>) {
        val actionsJson = gson.toJson(actions)
        prefs.edit { putString(KEY_USER_ACTIONS, actionsJson) }
    }

    // User Analytics Tracking
    fun recordUserAction(action: UserAction) {
        val analytics = getUserAnalytics().toMutableList()
        analytics.add(action)

        // Keep only the last 100 actions
        val trimmedAnalytics = analytics.takeLast(100)
        saveUserAnalytics(trimmedAnalytics)
    }

    fun getUserAnalytics(): List<UserAction> {
        val analyticsJson = prefs.getString(KEY_USER_ANALYTICS, null)
        return if (analyticsJson != null) {
            try {
                val type = object : TypeToken<List<UserAction>>() {}.type
                gson.fromJson(analyticsJson, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    private fun saveUserAnalytics(analytics: List<UserAction>) {
        val analyticsJson = gson.toJson(analytics)
        prefs.edit { putString(KEY_USER_ANALYTICS, analyticsJson) }
    }

    // Clear all user data
    fun clearAllUserData() {
        prefs.edit { clear() }
        _favoriteCarIds.value = emptySet()
        _searchHistory.value = emptyList()
        _recentFilters.value = emptyMap()
        _favoriteBookingIds.value = emptySet()
    }
}

data class BookingPreferences(
    val preferredPickupLocation: String = "",
    val preferredCarType: String = "",
    val preferredFuelType: String = "",
    val withDriverByDefault: Boolean = false,
    val notificationPreferences: NotificationPreferences = NotificationPreferences()
)

data class NotificationPreferences(
    val bookingReminders: Boolean = true,
    val promotionalOffers: Boolean = true,
    val newCarAlerts: Boolean = false,
    val priceDropAlerts: Boolean = false
)

data class UserAction(
    val action: String,
    val targetId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: Map<String, String> = emptyMap()
)
