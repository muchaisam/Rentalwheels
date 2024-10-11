package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.msdc.rentalwheels.ui.components.RecommendedCars
import com.msdc.rentalwheels.viewmodel.CarViewModel


@Composable
fun CarRentalApp(
    uiState: CarViewModel.UiState,
    carDetailState: CarViewModel.CarDetailState,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onBackClick: () -> Unit
) {
    when (carDetailState) {
        is CarViewModel.CarDetailState.Initial,
        is CarViewModel.CarDetailState.Loading -> {
            // Show the main screen with a loading indicator for car details if needed
            MainScreen(
                uiState = uiState,
                onCarClick = onCarClick,
                onLoadMore = onLoadMore,
                isLoadingCarDetails = carDetailState is CarViewModel.CarDetailState.Loading
            )
        }
        is CarViewModel.CarDetailState.Error -> ErrorScreen(message = carDetailState.message)
        is CarViewModel.CarDetailState.Success -> DetailedCarScreen(
            car = carDetailState.car,
            onBackClick = onBackClick
        )
    }
}
@Composable
fun MainScreen(
    uiState: CarViewModel.UiState,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingCarDetails: Boolean
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
            isLoadingCarDetails = isLoadingCarDetails
        )
    }
}



@Composable
fun SuccessScreen(
    categories: List<Category>,
    recommendedCars: List<Car>,
    cars: List<Car>,
    deals: List<Deal>,
    loadMoreError: String?,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingCarDetails: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.height(1000.dp)) {
            item {
                if (deals.isNotEmpty()) {
                    PromotionBanner(deals)
                } else {
                    Text(
                        "No active deals at the moment",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                if (categories.isNotEmpty()) {
                    CategoryList(categories)
                } else {
                    Text(
                        "No categories available",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            item {
                if (recommendedCars.isNotEmpty()) {
                    RecommendedCars(recommendedCars)
                } else {
                    Text(
                        "No recommended cars at the moment",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            items(cars) { car ->
                CarItem(car, onCarClick)
            }
            item {
                if (cars.isNotEmpty()) {
                    Button(
                        onClick = onLoadMore,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Load More")
                    }
                } else {
                    Text(
                        "No cars available",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(16.dp)
                    )
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