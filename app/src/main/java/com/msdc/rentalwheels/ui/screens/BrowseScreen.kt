package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.browse.BrowseFiltersSection
import com.msdc.rentalwheels.ui.components.browse.BrowseSearchHeader
import com.msdc.rentalwheels.ui.components.comparison.ComparisonBottomSheet
import com.msdc.rentalwheels.ui.components.comparison.ComparisonFloatingButton
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.uistates.BrowseScreenState
import com.msdc.rentalwheels.viewmodel.BrowseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
        onCarClick: (String) -> Unit,
        onViewComparison: () -> Unit = {},
        modifier: Modifier = Modifier,
        viewModel: BrowseViewModel = hiltViewModel()
) {
    val browseState by viewModel.browseState.collectAsState()
    val comparisonState by viewModel.comparisonState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }
    var showComparisonSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (browseState is BrowseScreenState.Loading) {
            viewModel.loadCars()
        }
    }

    when (val state = browseState) {
        is BrowseScreenState.Loading -> {
            LoadingScreen()
        }
        is BrowseScreenState.Error -> {
            ErrorScreen(message = state.message, onRetry = { viewModel.onRefreshData() })
        }
        is BrowseScreenState.Success -> {
            Box(modifier = modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Fixed header - no scrolling
                    BrowseSearchHeader(
                            searchQuery = state.searchQuery,
                            onSearchQueryChange = { query -> viewModel.updateSearchQuery(query) },
                            onFilterClick = { showFilters = !showFilters },
                            totalVehicles = state.totalVehicles,
                            modifier =
                                    Modifier.fillMaxWidth()
                                            .padding(
                                                    horizontal =
                                                            RentalWheelsDesignSystem.Spacing.medium,
                                                    vertical =
                                                            RentalWheelsDesignSystem.Spacing.small
                                            )
                    )

                    // Filters section - fixed, no scrolling
                    AnimatedVisibility(
                            visible = showFilters,
                            enter = slideInVertically() + fadeIn(),
                            exit = slideOutVertically() + fadeOut()
                    ) {
                        Column(
                                modifier =
                                        Modifier.padding(
                                                horizontal = RentalWheelsDesignSystem.Spacing.medium
                                        )
                        ) {
                            BrowseFiltersSection(
                                    filters = state.filters,
                                    availableCategories = state.availableCategories,
                                    availableFuelTypes = state.availableFuelTypes,
                                    availableTransmissions = state.availableTransmissions,
                                    onFiltersUpdate = { newFilters ->
                                        viewModel.updateFilters(newFilters)
                                    }
                            )
                            Spacer(
                                    modifier =
                                            Modifier.height(RentalWheelsDesignSystem.Spacing.small)
                            )
                        }
                    }

                    // Active filters display - fixed, no scrolling
                    if (state.filters != com.msdc.rentalwheels.uistates.BrowseFilters()) {
                        ActiveFiltersDisplay(
                                filters = state.filters,
                                onRemoveFilter = { filterType ->
                                    when (filterType) {
                                        "category" -> viewModel.filterByCategory("All")
                                        "fuelType" -> viewModel.filterByFuelType("All")
                                        "transmission" -> viewModel.filterByTransmission("All")
                                        else -> viewModel.clearFilters()
                                    }
                                },
                                onClearAll = { viewModel.clearFilters() },
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .padding(
                                                        horizontal =
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .medium
                                                )
                        )
                    }

                    // Only scrollable content: Car List (Single Column)
                    LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding =
                                    PaddingValues(
                                            start = RentalWheelsDesignSystem.Spacing.medium,
                                            end = RentalWheelsDesignSystem.Spacing.medium,
                                            top = RentalWheelsDesignSystem.Spacing.medium,
                                            bottom =
                                                    RentalWheelsDesignSystem.Spacing.Huge +
                                                            RentalWheelsDesignSystem.Spacing.XXL
                                    ),
                            verticalArrangement =
                                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.large)
                    ) {
                        // Car List Items with comparison support
                        items(items = state.filteredCars, key = { car -> car.id }) { car ->
                            val isSelected = viewModel.isInComparison(car.id)
                            val canSelect = viewModel.canAddToComparison(car.id)

                            com.msdc.rentalwheels.ui.components.home.CarCard(
                                    car = car,
                                    onClick = { onCarClick(car.id) },
                                    modifier = Modifier.fillMaxWidth(),
                                    isSelectable =
                                            comparisonState.selectedCars.isNotEmpty() || isSelected,
                                    isSelected = isSelected,
                                    onSelectionToggle = {
                                        if (canSelect || isSelected) {
                                            viewModel.toggleCarComparison(car)
                                        }
                                    }
                            )
                        }

                        // Loading indicator at bottom
                        if (state.isLoadingMore) {
                            item {
                                Box(
                                        modifier =
                                                Modifier.fillMaxWidth()
                                                        .padding(
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .large
                                                        )
                                ) {
                                    androidx.compose.material3.CircularProgressIndicator(
                                            modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }

                // Comparison Floating Button
                ComparisonFloatingButton(
                        selectedCount = comparisonState.selectedCars.size,
                        onClick = { showComparisonSheet = true },
                        modifier =
                                Modifier.align(Alignment.BottomEnd)
                                        .padding(
                                                end = RentalWheelsDesignSystem.Spacing.extraLarge,
                                                bottom =
                                                        RentalWheelsDesignSystem.Spacing.Huge +
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .large
                                        )
                )
            }
        }
    }

    // Comparison Bottom Sheet
    if (showComparisonSheet && comparisonState.selectedCars.isNotEmpty()) {
        ComparisonBottomSheet(
                cars = comparisonState.selectedCars,
                onDismiss = {
                    coroutineScope.launch {
                        sheetState.hide()
                        showComparisonSheet = false
                    }
                },
                onSelectCar = { car ->
                    onCarClick(car.id)
                    coroutineScope.launch {
                        sheetState.hide()
                        showComparisonSheet = false
                    }
                },
                onRemoveCar = { carId ->
                    viewModel.removeFromComparison(carId)
                    if (comparisonState.selectedCars.size <= 1) {
                        coroutineScope.launch {
                            sheetState.hide()
                            showComparisonSheet = false
                        }
                    }
                },
                sheetState = sheetState
        )
    }

    // Load initial data
    LaunchedEffect(Unit) {
        if (browseState is BrowseScreenState.Loading) {
            viewModel.loadCars()
        }
    }
}

@Composable
private fun ActiveFiltersDisplay(
        filters: com.msdc.rentalwheels.uistates.BrowseFilters,
        onRemoveFilter: (String) -> Unit,
        onClearAll: () -> Unit,
        modifier: Modifier = Modifier
) {
    // Simple implementation for now - can be enhanced later
    androidx.compose.material3.Text(
            text = "Active Filters Applied",
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            modifier = modifier.padding(RentalWheelsDesignSystem.Spacing.small)
    )
}
