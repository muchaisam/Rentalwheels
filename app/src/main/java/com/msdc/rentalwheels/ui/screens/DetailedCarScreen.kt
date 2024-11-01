package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.BookingButton
import com.msdc.rentalwheels.ui.components.CarDescription
import com.msdc.rentalwheels.ui.components.CarFeatures
import com.msdc.rentalwheels.ui.components.CarImage
import com.msdc.rentalwheels.ui.components.CarSpecs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedCarScreen(car: Car, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopAppBar(
            title = { Text(car.brand + " " + car.model) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
        ) {
            CarImage(car.imageUrl)
            CarSpecs(car)
            CarDescription(car.description)
            CarFeatures(car.features)
            BookingButton(car.price)
        }
    }
}