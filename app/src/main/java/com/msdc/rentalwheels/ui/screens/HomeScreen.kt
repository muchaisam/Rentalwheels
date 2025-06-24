package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.components.AnimatedBackground
import com.msdc.rentalwheels.ui.components.CategoryList
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.PromotionBanner
import com.msdc.rentalwheels.ui.components.home.FilterableCarList
import com.msdc.rentalwheels.ui.components.home.RecommendedCarsSection
import com.msdc.rentalwheels.uistates.HomeScreenState
import com.msdc.rentalwheels.viewmodel.CarViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(viewModel: CarViewModel, onCarClick: (String) -> Unit) {
    val homeState by viewModel.homeState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    val pullRefreshState =
            rememberPullRefreshState(
                    refreshing = isRefreshing,
                    onRefresh = {
                        isRefreshing = true
                        viewModel.loadInitialData()
                        isRefreshing = false
                    }
            )

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated Background
        AnimatedBackground()

        // Pull-to-refresh wrapper
        Box(modifier = Modifier.fillMaxSize().pullRefresh(pullRefreshState)) {
            when (val state = homeState) {
                is HomeScreenState.Loading -> {
                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        LoadingScreen()
                    }
                }
                is HomeScreenState.Error -> {
                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        ErrorScreen(message = state.message)
                    }
                }
                is HomeScreenState.Success -> {
                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding =
                                        PaddingValues(
                                                start = 16.dp,
                                                end = 16.dp,
                                                top = 8.dp,
                                                bottom = 100.dp // Extra space for bottom navigation
                                        )
                        ) {
                            // Deals Section
                            if (state.deals.isNotEmpty()) {
                                item {
                                    PromotionBanner(
                                            deals = state.deals,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            // Categories Section
                            if (state.categories.isNotEmpty()) {
                                item {
                                    CategoryList(
                                            categories = state.categories,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            // Recommended Cars Section
                            if (state.recommendedCars.isNotEmpty()) {
                                item {
                                    RecommendedCarsSection(
                                            cars = state.recommendedCars,
                                            onCarClick = onCarClick,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }
                            }

                            // Cars by Fuel Type Section
                            item {
                                FilterableCarList(
                                        carsByFilter = state.carsByFuelType,
                                        filterName = "Fuel Type",
                                        onCarClick = onCarClick,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            // Cars by Year Section
                            item {
                                FilterableCarList(
                                        carsByFilter = state.carsByYear,
                                        filterName = "Year",
                                        onCarClick = onCarClick,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            // Bottom spacer for better UX
                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }

        // Pull refresh indicator
        PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
        )
    }
}
