package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.Typography

@Composable
fun RecommendedCars(cars: List<Car>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()  // Changed from fixed height
            .padding(vertical = 8.dp)
    ) {
        Text("Recommended Cars", style = Typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal scrolling carousel instead of AutoScrollingCarousel
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cars) { car ->
                RecommendedCarItem(car)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grid with fixed number of items instead of LazyVerticalGrid
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            cars.take(2).forEach { car ->
                Box(modifier = Modifier.weight(1f)) {
                    RecommendedCarItem(car)
                }
            }
        }
    }
}