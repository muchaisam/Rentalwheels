package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.msdc.rentalwheels.ui.theme.Typography
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Category

@Composable
fun CategoryItem(category: Category) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(category.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = category.name,
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            onError = {
                // Log error or show placeholder
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(category.name, style = Typography.bodyMedium)
    }
}