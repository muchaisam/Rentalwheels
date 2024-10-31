package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.data.model.Car

@Composable
fun CarSpecs(car: Car) {
    Row(
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SpecItem(title = "Engine", value = car.engine)
        SpecItem(title = "Transmission", value = car.transmission)
        SpecItem(title = "Fuel Type", value = car.fuelType)
    }
}

@Composable
fun SpecItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(
                id = when (title) {
                    "Engine" -> R.drawable.engine
                    "Transmission" -> R.drawable.ic_transmission
                    else -> R.drawable.ic_fuel
                }
            ),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Text(text = value, fontWeight = FontWeight.Bold)
        Text(text = title, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun CarDescription(description: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description)
    }
}
