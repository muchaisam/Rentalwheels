package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.msdc.rentalwheels.ui.theme.Typography
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car

@Composable
fun CarItem(car: Car, onCarClick: (String) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()  // Added wrapContentHeight
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                isExpanded = !isExpanded
                onCarClick(car.id)
            }
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(car.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${car.brand} ${car.model}",
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("${car.brand} ${car.model}", style = Typography.titleMedium)
                    Text("${car.year} • ${car.category}", style = Typography.bodyMedium)
                    Text(
                        "Ksh ${car.dailyRate}/Day",
                        style = Typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(car.description, style = Typography.bodyMedium)
            }
        }
    }
}