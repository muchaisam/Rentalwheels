package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.persistence.UserPreferencesManager
import com.msdc.rentalwheels.data.repository.CarRepository
import com.msdc.rentalwheels.modules.IoDispatcher
import com.msdc.rentalwheels.uistates.BrowseFilters
import com.msdc.rentalwheels.uistates.BrowseScreenState
import com.msdc.rentalwheels.uistates.ComparisonState
import com.msdc.rentalwheels.uistates.FavoritesState
import com.msdc.rentalwheels.uistates.RangeFilterState
import com.msdc.rentalwheels.uistates.RefreshState
import com.msdc.rentalwheels.uistates.SortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class BrowseViewModel
@Inject
constructor(
    private val repository: CarRepository,
    private val userPreferencesManager: UserPreferencesManager,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _browseState = MutableStateFlow<BrowseScreenState>(BrowseScreenState.Loading)
    val browseState: StateFlow<BrowseScreenState> = _browseState.asStateFlow()

    private val _allCars = MutableStateFlow<List<Car>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _filters = MutableStateFlow(BrowseFilters())
    private val _isLoadingMore = MutableStateFlow(false)

    // Enhanced state management for new features
    private val _favoritesState = MutableStateFlow(FavoritesState())
    val favoritesState: StateFlow<FavoritesState> = _favoritesState.asStateFlow()

    private val _comparisonState = MutableStateFlow(ComparisonState())
    val comparisonState: StateFlow<ComparisonState> = _comparisonState.asStateFlow()

    private val _rangeFilterState = MutableStateFlow(RangeFilterState())
    val rangeFilterState: StateFlow<RangeFilterState> = _rangeFilterState.asStateFlow()

    private val _refreshState = MutableStateFlow(RefreshState())
    val refreshState: StateFlow<RefreshState> = _refreshState.asStateFlow()

    // Computed state that reactively filters and sorts cars
    private val _filteredCars: StateFlow<List<Car>> =
        combine(_allCars, _searchQuery, _filters) { allCars, searchQuery, filters ->
            applySearchAndFilters(allCars, searchQuery, filters)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        loadCars()
    }

    fun loadCars() {
        viewModelScope.launch(dispatcher) {
            try {
                _browseState.value = BrowseScreenState.Loading

                // Load cars and categories concurrently
                combine(
                    repository.getCars(limit = 50), // Load more cars for browsing
                    repository.getCategories()
                ) { cars, categories -> Pair(cars, categories) }
                    .collect { (cars, categories) ->
                        _allCars.value = cars

                        // Extract unique values for filters
                        val availableCategories =
                            listOf("All") + categories.map { it.name }.distinct()
                        val availableFuelTypes =
                            listOf("All") +
                                    cars.map { it.fuelType }.distinct().filter {
                                        it.isNotEmpty()
                                    }
                        val availableTransmissions =
                            listOf("All") +
                                    cars.map { it.transmission }.distinct().filter {
                                        it.isNotEmpty()
                                    }

                        val filteredCars =
                            applySearchAndFilters(cars, _searchQuery.value, _filters.value)

                        _browseState.value =
                            BrowseScreenState.Success(
                                allCars = cars,
                                filteredCars = filteredCars,
                                availableCategories = availableCategories,
                                availableFuelTypes = availableFuelTypes,
                                availableTransmissions = availableTransmissions,
                                totalVehicles = filteredCars.size,
                                filters = _filters.value,
                                isLoadingMore = _isLoadingMore.value,
                                searchQuery = _searchQuery.value,
                                favoriteCarIds = _favoritesState.value.favoriteCarIds,
                                selectedCarsForComparison =
                                _comparisonState.value.selectedCars,
                                isRefreshing = _refreshState.value.isRefreshing
                            )
                    }
            } catch (e: Exception) {
                Timber.e(e, "Error loading cars for browse")
                _browseState.value =
                    BrowseScreenState.Error(
                        message = e.message ?: "Failed to load vehicles. Please try again."
                    )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        updateBrowseStateWithCurrentData()
    }

    fun updateFilters(newFilters: BrowseFilters) {
        _filters.value = newFilters
        updateBrowseStateWithCurrentData()
    }

    fun clearFilters() {
        _filters.value = BrowseFilters()
        updateBrowseStateWithCurrentData()
    }

    fun loadMoreCars() {
        viewModelScope.launch(dispatcher) {
            val currentState = _browseState.value
            if (currentState is BrowseScreenState.Success && !_isLoadingMore.value) {
                try {
                    _isLoadingMore.value = true
                    updateBrowseStateWithCurrentData()

                    // Load more cars using pagination
                    val lastCarId = _allCars.value.lastOrNull()?.id
                    repository.getCars(limit = 20, lastDocumentId = lastCarId).collect { newCars ->
                        if (newCars.isNotEmpty()) {
                            val updatedAllCars = _allCars.value + newCars
                            _allCars.value = updatedAllCars

                            updateBrowseStateWithCurrentData()
                        }
                        _isLoadingMore.value = false
                        updateBrowseStateWithCurrentData()
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Error loading more cars")
                    _isLoadingMore.value = false
                    updateBrowseStateWithCurrentData()
                }
            }
        }
    }

    // =========================
    // ENHANCED FEATURES METHODS
    // =========================

    // Favorites/Wishlist functionality
    fun toggleFavorite(carId: String) {
        viewModelScope.launch {
            val currentFavorites = _favoritesState.value.favoriteCarIds.toMutableSet()
            if (currentFavorites.contains(carId)) {
                currentFavorites.remove(carId)
            } else {
                currentFavorites.add(carId)
            }
            _favoritesState.value = _favoritesState.value.copy(favoriteCarIds = currentFavorites)
            // TODO: Persist to local storage or backend
        }
    }

    fun isFavorite(carId: String): Boolean {
        return _favoritesState.value.isFavorite(carId)
    }

    fun getFavoriteIds(): Set<String> {
        return _favoritesState.value.favoriteCarIds
    }

    // Comparison functionality
    fun toggleCarComparison(car: Car) {
        val currentState = _comparisonState.value
        val currentCars = currentState.selectedCars.toMutableList()

        val existingIndex = currentCars.indexOfFirst { it.id == car.id }
        if (existingIndex != -1) {
            // Remove car from comparison
            currentCars.removeAt(existingIndex)
        } else if (currentState.canAddMore) {
            // Add car to comparison
            currentCars.add(car)
        }

        _comparisonState.value =
            currentState.copy(
                selectedCars = currentCars,
                isComparing = currentCars.isNotEmpty()
            )
    }

    fun removeFromComparison(carId: String) {
        val currentState = _comparisonState.value
        val updatedCars = currentState.selectedCars.filterNot { it.id == carId }

        _comparisonState.value =
            currentState.copy(
                selectedCars = updatedCars,
                isComparing = updatedCars.isNotEmpty()
            )
    }

    fun clearComparison() {
        _comparisonState.value = ComparisonState()
    }

    fun isInComparison(carId: String): Boolean {
        return _comparisonState.value.selectedCars.any { it.id == carId }
    }

    fun canAddToComparison(carId: String): Boolean {
        return _comparisonState.value.canAddCar(carId)
    }

    // Pull-to-refresh functionality
    fun onPullToRefresh() {
        val currentRefreshState = _refreshState.value
        if (currentRefreshState.canRefresh) {
            _refreshState.value = currentRefreshState.copy(isRefreshing = true)

            // Refresh data
            refreshData()
        }
    }

    private fun finishRefresh() {
        _refreshState.value =
            _refreshState.value.copy(
                isRefreshing = false,
                lastRefreshTime = System.currentTimeMillis()
            )
    }

    fun onRefreshData() {
        viewModelScope.launch {
            try {
                _refreshState.value = _refreshState.value.copy(isRefreshing = true)

                // Clear current data and reload
                _allCars.value = emptyList()
                _searchQuery.value = ""
                _filters.value = BrowseFilters()
                _isLoadingMore.value = false

                loadCars()
            } finally {
                finishRefresh()
            }
        }
    }

    // Enhanced quick actions
    fun applyQuickFilterFavorites() {
        val favoriteIds = _favoritesState.value.favoriteCarIds
        // Filter to show only favorite cars
        val currentState = _browseState.value
        if (currentState is BrowseScreenState.Success) {
            val favoriteCars = currentState.allCars.filter { favoriteIds.contains(it.id) }
            _browseState.value =
                currentState.copy(
                    filteredCars = favoriteCars,
                    totalVehicles = favoriteCars.size
                )
        }
    }

    fun resetToAllCars() {
        updateBrowseStateWithCurrentData()
    }

    // Convenience methods for specific filter types
    fun filterByCategory(category: String) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(selectedCategory = category))
    }

    fun filterByFuelType(fuelType: String) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(selectedFuelType = fuelType))
    }

    fun filterByTransmission(transmission: String) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(selectedTransmission = transmission))
    }

    fun updatePriceRange(minPrice: Int, maxPrice: Int) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(minPrice = minPrice, maxPrice = maxPrice))
    }

    fun updateYearRange(minYear: Int, maxYear: Int) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(minYear = minYear, maxYear = maxYear))
    }

    fun toggleDriverOption() {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(withDriver = !currentFilters.withDriver))
    }

    fun updateSortOption(sortOption: SortOption) {
        val currentFilters = _filters.value
        updateFilters(currentFilters.copy(sortBy = sortOption))
    }

    // Quick filter presets
    fun applyQuickFilterElectric() {
        updateFilters(BrowseFilters(selectedFuelType = "Electric"))
    }

    fun applyQuickFilterLuxury() {
        updateFilters(
            BrowseFilters(selectedCategory = "Luxury", sortBy = SortOption.PRICE_HIGH_TO_LOW)
        )
    }

    fun applyQuickFilterEconomic() {
        updateFilters(BrowseFilters(sortBy = SortOption.PRICE_LOW_TO_HIGH, maxPrice = 100))
    }

    fun applyQuickFilterNewest() {
        updateFilters(BrowseFilters(sortBy = SortOption.YEAR_NEWEST, minYear = 2022))
    }

    // Missing helper functions
    private fun applySearchAndFilters(
        allCars: List<Car>,
        searchQuery: String,
        filters: BrowseFilters
    ): List<Car> {
        var filteredCars = allCars

        // Apply search query
        if (searchQuery.isNotBlank()) {
            filteredCars =
                filteredCars.filter { car ->
                    car.make.contains(searchQuery, ignoreCase = true) ||
                            car.model.contains(searchQuery, ignoreCase = true) ||
                            car.category.contains(searchQuery, ignoreCase = true) ||
                            car.location.contains(searchQuery, ignoreCase = true)
                }
        }

        // Apply filters
        filteredCars =
            filteredCars.filter { car ->
                val priceMatch =
                    car.pricePerDay >= filters.minPrice &&
                            car.pricePerDay <= filters.maxPrice
                val yearMatch = car.year >= filters.minYear && car.year <= filters.maxYear
                val categoryMatch =
                    filters.selectedCategory == "All" ||
                            car.category == filters.selectedCategory
                val fuelMatch =
                    filters.selectedFuelType == "All" ||
                            car.fuelType == filters.selectedFuelType
                val transmissionMatch =
                    filters.selectedTransmission == "All" ||
                            car.transmission == filters.selectedTransmission
                val availabilityMatch = car.isAvailable // Always show only available cars

                priceMatch &&
                        yearMatch &&
                        categoryMatch &&
                        fuelMatch &&
                        transmissionMatch &&
                        availabilityMatch
            }

        // Apply sorting
        return when (filters.sortBy) {
            SortOption.PRICE_LOW_TO_HIGH -> filteredCars.sortedBy { it.pricePerDay }
            SortOption.PRICE_HIGH_TO_LOW -> filteredCars.sortedByDescending { it.pricePerDay }
            SortOption.YEAR_NEWEST -> filteredCars.sortedByDescending { it.year }
            SortOption.YEAR_OLDEST -> filteredCars.sortedBy { it.year }
            SortOption.BRAND_A_TO_Z -> filteredCars.sortedBy { "${it.make} ${it.model}" }
            SortOption.BRAND_Z_TO_A -> filteredCars.sortedByDescending { "${it.make} ${it.model}" }
            SortOption.MOST_POPULAR -> filteredCars.sortedByDescending { it.reviewCount }
        }
    }

    private fun updateBrowseStateWithCurrentData() {
        viewModelScope.launch(dispatcher) {
            try {
                val allCars = _allCars.value
                val filteredCars = _filteredCars.value
                val favorites = _favoritesState.value.favoriteCarIds
                val comparisons = _comparisonState.value.selectedCars

                // Extract dynamic data from current cars
                val availableCategories = listOf("All") + allCars.map { it.category }.distinct()
                val availableFuelTypes =
                    listOf("All") +
                            allCars.map { it.fuelType }.distinct().filter { it.isNotEmpty() }
                val availableTransmissions =
                    listOf("All") +
                            allCars.map { it.transmission }.distinct().filter {
                                it.isNotEmpty()
                            }

                _browseState.value =
                    BrowseScreenState.Success(
                        allCars = allCars,
                        filteredCars = filteredCars,
                        availableCategories = availableCategories,
                        availableFuelTypes = availableFuelTypes,
                        availableTransmissions = availableTransmissions,
                        totalVehicles = filteredCars.size,
                        filters = _filters.value,
                        isLoadingMore = _isLoadingMore.value,
                        searchQuery = _searchQuery.value,
                        favoriteCarIds = favorites,
                        selectedCarsForComparison = comparisons,
                        isRefreshing = _refreshState.value.isRefreshing
                    )
            } catch (e: Exception) {
                Timber.e(e, "Error updating browse state")
                _browseState.value =
                    BrowseScreenState.Error(message = e.message ?: "Failed to update data")
            }
        }
    }

    fun refreshData() {
        onRefreshData()
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
