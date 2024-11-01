package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal
import com.msdc.rentalwheels.ui.components.CarItem
import com.msdc.rentalwheels.ui.components.CategoryList
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.PromotionBanner
import com.msdc.rentalwheels.viewmodel.CarViewModel
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.msdc.rentalwheels.ui.components.RecommendedCarItem
import com.msdc.rentalwheels.ui.theme.Typography
import com.msdc.rentalwheels.ui.utils.PullRefreshIndicator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: CarViewModel.UiState,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingCarDetails: Boolean,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    scrollBehavior: TopAppBarScrollBehavior
) {
    when (uiState) {
        is CarViewModel.UiState.Loading -> LoadingScreen()
        is CarViewModel.UiState.Error -> ErrorScreen(message = uiState.message)
        is CarViewModel.UiState.Success -> SuccessScreen(
            categories = uiState.categories,
            recommendedCars = uiState.recommendedCars,
            cars = uiState.cars,
            deals = uiState.deals,
            loadMoreError = uiState.loadMoreError,
            onCarClick = onCarClick,
            onLoadMore = onLoadMore,
            isLoadingCarDetails = isLoadingCarDetails,
            onRefresh = onRefresh,
            isRefreshing = isRefreshing,
            scrollBehavior = scrollBehavior
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SuccessScreen(
    categories: List<Category>,
    recommendedCars: List<Car>,
    cars: List<Car>,
    deals: List<Deal>,
    loadMoreError: String?,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingCarDetails: Boolean,
    onRefresh: () -> Unit,
    isRefreshing: Boolean,
    scrollBehavior: TopAppBarScrollBehavior
) {
    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .height(1600.dp)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            if (deals.isNotEmpty()) {
                item(key = "deals") {
                    PromotionBanner(deals = deals)
                }
            }

            if (categories.isNotEmpty()) {
                item(key = "categories") {
                    CategoryList(categories = categories)
                }
            }

            if (recommendedCars.isNotEmpty()) {
                item(key = "recommendedTitle") {
                    Text(
                        "Recommended Cars",
                        style = Typography.titleLarge,
                        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                }

                item(key = "recommendedCars") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(recommendedCars) { car ->
                            RecommendedCarItem(car)
                        }
                    }
                }
            }

            items(
                items = cars,
                key = { it.id }
            ) { car ->
                CarItem(car = car, onCarClick = onCarClick)
            }

            if (cars.isNotEmpty()) {
                item {
                    Button(
                        onClick = onLoadMore,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text("Load More")
                    }
                }
            }

            loadMoreError?.let {
                item {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        if (isLoadingCarDetails) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}