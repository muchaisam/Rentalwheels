package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.Typography

@Composable
fun FeaturedCar(car: Car) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
    ) {
        Column {
            ShimmerAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(car.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "${car.brand} ${car.model}",
                modifier = Modifier
                    .width(220.dp)
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Featured: ${car.brand} ${car.model}", style = Typography.titleLarge)
                Text(car.description, style = Typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(
                    "Ksh ${car.dailyRate}/Day",
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}