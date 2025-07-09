package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.browse.BrowseFiltersSection
import com.msdc.rentalwheels.ui.components.browse.BrowseSearchHeader
import com.msdc.rentalwheels.ui.components.browse.EnhancedCarCard
import com.msdc.rentalwheels.uistates.BrowseScreenState
import com.msdc.rentalwheels.viewmodel.BrowseViewModel

@Composable
fun BrowseScreen(
    onCarClick: (String) -> Unit,
    onViewComparison: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BrowseViewModel = hiltViewModel()
) {
    val browseState by viewModel.browseState.collectAsState()
    var showFilters by remember { mutableStateOf(false) }

    when (val state = browseState) {
        is BrowseScreenState.Loading -> {
            LoadingScreen()
        }

        is BrowseScreenState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { viewModel.onRefreshData() }
            )
        }

        is BrowseScreenState.Success -> {
            Column(modifier = modifier.fillMaxSize()) {
                // Fixed header - no scrolling
                BrowseSearchHeader(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = { query ->
                        viewModel.updateSearchQuery(query)
                    },
                    onFilterClick = { showFilters = !showFilters },
                    totalVehicles = state.totalVehicles,
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                )

                // Filters section - fixed, no scrolling
                AnimatedVisibility(
                    visible = showFilters,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        BrowseFiltersSection(
                            filters = state.filters,
                            availableCategories =
                            state.availableCategories,
                            availableFuelTypes =
                            state.availableFuelTypes,
                            availableTransmissions =
                            state.availableTransmissions,
                            onFiltersUpdate = { newFilters ->
                                viewModel.updateFilters(newFilters)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Active filters display - fixed, no scrolling
                if (state.filters != com.msdc.rentalwheels.uistates.BrowseFilters()
                ) {
                    ActiveFiltersDisplay(
                        filters = state.filters,
                        onRemoveFilter = { filterType ->
                            when (filterType) {
                                "category" ->
                                    viewModel.filterByCategory(
                                        "All"
                                    )

                                "fuelType" ->
                                    viewModel.filterByFuelType(
                                        "All"
                                    )

                                "transmission" ->
                                    viewModel
                                        .filterByTransmission(
                                            "All"
                                        )

                                else -> viewModel.clearFilters()
                            }
                        },
                        onClearAll = { viewModel.clearFilters() },
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }

                // Only scrollable content: Car Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Car Grid Items
                    items(
                        items = state.filteredCars,
                        key = { car -> car.id }
                    ) { car ->
                        EnhancedCarCard(
                            car = car,
                            onClick = { onCarClick(car.id) },
                            onToggleFavorite = { /* TODO: Implement favorite toggle */ },
                            onToggleComparison = { /* TODO: Implement comparison toggle */ },
                            isFavorite = false,
                            canAddToComparison = true,
                            isSelectedForComparison = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Loading indicator at bottom
                    if (state.isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                androidx.compose.material3
                                    .CircularProgressIndicator(
                                        modifier =
                                        Modifier.align(
                                            Alignment
                                                .Center
                                        )
                                    )
                            }
                        }
                    }
                }
            }
        }
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
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.padding(8.dp)
    )
}
