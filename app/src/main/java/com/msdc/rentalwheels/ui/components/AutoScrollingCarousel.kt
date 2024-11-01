package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import kotlinx.coroutines.delay
import com.msdc.rentalwheels.ui.theme.Typography


@Composable
fun AutoScrollingCarousel(cars: List<Car>) {
    var currentIndex by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
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
            modifier = Modifier
                .width(200.dp)
                .height(120.dp),
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
                style = Typography.titleMedium,
                color = Color.White
            )
            Text(
                "Ksh ${cars[currentIndex].dailyRate}/Day",
                style = Typography.bodyMedium,
                color = Color.White
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            cars.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (index == currentIndex) Color.White else Color.White.copy(alpha = 0.5f))
                )
            }
        }
    }
}
