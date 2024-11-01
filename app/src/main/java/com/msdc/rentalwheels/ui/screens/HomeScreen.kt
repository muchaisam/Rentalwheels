package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.viewmodel.CarViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: CarViewModel.UiState,
    carDetailState: CarViewModel.CarDetailState,
    onCarClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onSearch: (String) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var searchQuery by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {  // Changed from Scaffold to Column


        when (carDetailState) {
            is CarViewModel.CarDetailState.Initial,
            is CarViewModel.CarDetailState.Loading -> {
                HomeContent(
                    uiState = uiState,
                    onCarClick = onCarClick,
                    onLoadMore = onLoadMore,
                    isLoadingCarDetails = carDetailState is CarViewModel.CarDetailState.Loading,
                    onRefresh = {
                        coroutineScope.launch {
                            isRefreshing = true
                            onRefresh()
                            delay(1000)
                            isRefreshing = false
                        }
                    },
                    isRefreshing = isRefreshing,
                    scrollBehavior = scrollBehavior
                )
            }
            is CarViewModel.CarDetailState.Error -> ErrorScreen(message = carDetailState.message)
            is CarViewModel.CarDetailState.Success -> DetailedCarScreen(
                car = carDetailState.car,
                onBackClick = onBackClick
            )
        }
    }
}