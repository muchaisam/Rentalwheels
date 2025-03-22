package com.msdc.rentalwheels.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.msdc.rentalwheels.ui.screens.DetailedCarScreen
import com.msdc.rentalwheels.viewmodel.CarDetailsViewModel

@Composable
fun CarDetailRoute(
    carId: String,
    onBackClick: () -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(carId) {
        viewModel.loadCar(carId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        uiState.error != null -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = uiState.error ?: "",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        uiState.car != null -> {
            DetailedCarScreen(
                car = uiState.car!!,
                onBackClick = onBackClick
            )
        }
    }
}