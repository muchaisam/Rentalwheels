package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.ui.theme.Typography
import com.msdc.rentalwheels.utils.CurrencyUtils

@Composable
fun CarCard(car: Car, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var isBookmarked by remember { mutableStateOf(false) }

    Card(
        onClick = onClick,
        modifier =
        modifier
            .width(280.dp)
            .height(360.dp)
            .animateContentSize(
                animationSpec =
                spring(
                    dampingRatio =
                    Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(24.dp),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Image Section with Gradient Overlay
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 24.dp,
                                topEnd = 24.dp
                            )
                        )
                ) {
                    AsyncImage(
                        model = car.imageUrl,
                        contentDescription = "${car.brand} ${car.model}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay for better text visibility
                    Box(
                        modifier =
                        Modifier
                            .fillMaxSize()
                            .background(
                                brush =
                                Brush.verticalGradient(
                                    colors =
                                    listOf(
                                        Color.Transparent,
                                        Color.Black
                                            .copy(
                                                alpha =
                                                0.3f
                                            )
                                    )
                                )
                            )
                    )

                    // Car brand badge
                    Card(
                        modifier =
                        Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        colors =
                        CardDefaults.cardColors(
                            containerColor =
                            MaterialTheme.colorScheme
                                .primary.copy(
                                    alpha = 0.9f
                                )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = car.brand,
                            modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            style = Typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Bookmark Button
                    Card(
                        modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                            .size(44.dp),
                        onClick = { isBookmarked = !isBookmarked },
                        colors =
                        CardDefaults.cardColors(
                            containerColor =
                            MaterialTheme.colorScheme
                                .surface.copy(
                                    alpha = 0.9f
                                )
                        ),
                        shape = CircleShape,
                        elevation =
                        CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector =
                                if (isBookmarked)
                                    Icons.Filled
                                        .Bookmark
                                else
                                    Icons.Outlined
                                        .BookmarkBorder,
                                contentDescription =
                                if (isBookmarked)
                                    "Remove from bookmarks"
                                else "Add to bookmarks",
                                tint =
                                if (isBookmarked)
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                else
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Content Section
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)) {
                    // Car Name and Year
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = car.model,
                                style = Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color =
                                MaterialTheme.colorScheme
                                    .onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = car.year.toString(),
                                style = Typography.bodyMedium,
                                color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Features Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FeatureChip(
                            icon = Icons.Outlined.LocalGasStation,
                            text = car.fuelType,
                            modifier = Modifier.weight(1f)
                        )
                        FeatureChip(
                            icon = Icons.Outlined.Speed,
                            text = car.transmission,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Price Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Daily Rate",
                                style = Typography.labelSmall,
                                color =
                                MaterialTheme.colorScheme
                                    .onSurfaceVariant
                            )
                            Text(
                                text =
                                CurrencyUtils
                                    .formatDailyRate(
                                        car.dailyRate
                                    ),
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color =
                                MaterialTheme.colorScheme
                                    .primary
                            )
                        }

                        Card(
                            colors =
                            CardDefaults.cardColors(
                                containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Book Now",
                                modifier =
                                Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                style = Typography.labelMedium,
                                color =
                                MaterialTheme.colorScheme
                                    .onPrimaryContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                style = Typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CarCardPreview() {
    val previewCar =
        Car(
            make = "Tesla",
            model = "Model 3",
            year = 2023,
            price = 45000,
            dailyRate = 150,
            fuelType = "Electric",
            transmission = "Auto",
            imageUrl = ""
        )
    CarCard(car = previewCar, onClick = {})
}
