package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car

@Composable
fun RecommendedCars(cars: List<Car>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(8.dp)
    ) {
        Text("Recommended Cars", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Carousel
        AutoScrollingCarousel(cars)

        Spacer(modifier = Modifier.height(16.dp))

        // Featured Car
        cars.firstOrNull()?.let { featuredCar ->
            FeaturedCar(featuredCar)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid Layout
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(400.dp) // Set a fixed height for the grid
        ) {
            items(cars) { car ->
                RecommendedCarItem(car)
            }
        }
    }
}