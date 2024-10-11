package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import kotlinx.coroutines.delay

@Composable
fun AutoScrollingCarousel(cars: List<Car>) {
    var currentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000) // Scroll every 3 seconds
            currentIndex = (currentIndex + 1) % cars.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cars[currentIndex].imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "${cars[currentIndex].brand} ${cars[currentIndex].model}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(16.dp)
        ) {
            Text(
                "${cars[currentIndex].brand} ${cars[currentIndex].model}",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                "Ksh ${cars[currentIndex].dailyRate}/Day",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}