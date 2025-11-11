package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.data.model.Deal
import com.msdc.rentalwheels.ui.components.CategoryItem
import com.msdc.rentalwheels.ui.components.CategoryList
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.ui.components.PromotionBanner
import com.msdc.rentalwheels.ui.components.RecommendedCarItem
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.viewmodel.CarViewModel

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
        is CarViewModel.UiState.Success ->
            SuccessScreen(
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
    val safeCategories = categories.orEmpty().filterNotNull()
    val safeRecommendedCars = recommendedCars.orEmpty().filterNotNull()
    val safeCars = cars.orEmpty().filterNotNull()
    val safeDeals = deals.orEmpty().filterNotNull()

    val pullRefreshState = rememberPullRefreshState(isRefreshing, onRefresh)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)) {
            LazyColumn(
                modifier =
                Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                contentPadding = PaddingValues(bottom = RentalWheelsDesignSystem.Spacing.medium)
            ) {
                // Deals section
                if (safeDeals.isNotEmpty()) {
                    item(key = "deals") { PromotionBanner(deals = safeDeals) }
                }

                // Categories section
                if (safeCategories.isNotEmpty()) {
                    item(key = "categories") { CategoryList(categories = safeCategories) }
                }

                // Recommended cars section
                if (safeRecommendedCars.isNotEmpty()) {
                    item(key = "recommendedCarsHeader") {
                        Text(
                            "Recommended Cars",
                            modifier =
                            Modifier.padding(
                                start = RentalWheelsDesignSystem.Spacing.medium,
                                top = RentalWheelsDesignSystem.Spacing.medium,
                                bottom = RentalWheelsDesignSystem.Spacing.small
                            ),
                            style = RentalWheelsDesignSystem.Typography.bodyMedium
                        )
                    }

                    item(key = "recommendedCarsList") {
                        LazyRow(
                            contentPadding =
                            PaddingValues(
                                horizontal = RentalWheelsDesignSystem.Spacing.medium
                            ),
                            horizontalArrangement =
                            Arrangement.spacedBy(
                                RentalWheelsDesignSystem.Spacing.small
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            items(items = safeRecommendedCars, key = { car -> car.id }) { car ->
                                RecommendedCarItem(car = car, onClick = { onCarClick(car.id) })
                            }
                        }
                    }
                }

                // Add spacer inside an item block
                item(key = "spacer") {
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))
                }

                if (categories.isNotEmpty()) {
                    item(key = "categoriesScroll") {
                        LazyRow(
                            contentPadding =
                            PaddingValues(
                                horizontal = RentalWheelsDesignSystem.Spacing.medium
                            ),
                            horizontalArrangement =
                            Arrangement.spacedBy(
                                RentalWheelsDesignSystem.Spacing.medium
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                        ) {
                            items(
                                items = categories,
                                key = { category -> category.id } // Add proper key if possible
                            ) { category -> CategoryItem(category) }
                        }
                    }
                }
            }
        }

        if (isLoadingCarDetails) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
    }
}

@Composable
private fun ErrorFallback(
    message: String,
    height: androidx.compose.ui.unit.Dp = 80.dp,
    width: Float = 1f
) {
    Box(
        modifier =
        Modifier
            .height(height)
            .fillMaxWidth(width)
            .padding(RentalWheelsDesignSystem.Spacing.extraSmall)
            .background(
                MaterialTheme.colorScheme.errorContainer,
                shape =
                androidx.compose.foundation.shape.RoundedCornerShape(
                    RentalWheelsDesignSystem.CornerRadius.small
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.small),
            color = MaterialTheme.colorScheme.onErrorContainer,
            style = RentalWheelsDesignSystem.Typography.bodyMedium
        )
    }
}
