package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun HomeScreen(
    viewModel: CarViewModel,
    onCarClick: (String) -> Unit
) {
    val homeState by viewModel.homeState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh = {
        viewModel.loadInitialData()
    })

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = homeState) {
            is HomeScreenState.Loading -> LoadingScreen()
            is HomeScreenState.Error -> ErrorScreen(message = state.message)
            is HomeScreenState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    // Deals Section
                    if (state.deals.isNotEmpty()) {
                        item { PromotionBanner(deals = state.deals) }
                    }

                    // Categories Section
                    if (state.categories.isNotEmpty()) {
                        item { CategoryList(categories = state.categories) }
                    }

                    // Recommended Cars Section
                    if (state.recommendedCars.isNotEmpty()) {
                        item {
                            RecommendedCarsSection(
                                cars = state.recommendedCars,
                                onCarClick = onCarClick
                            )
                        }
                    }

                    // Cars by Fuel Type Section
                    item {
                        FilterableCarList(
                            carsByFilter = state.carsByFuelType,
                            filterName = "Fuel Type",
                            onCarClick = onCarClick
                        )
                    }

                    // Cars by Year Section
                    item {
                        FilterableCarList(
                            carsByFilter = state.carsByYear,
                            filterName = "Year",
                            onCarClick = onCarClick
                        )
                    }
                }
            }
        }
    }
}