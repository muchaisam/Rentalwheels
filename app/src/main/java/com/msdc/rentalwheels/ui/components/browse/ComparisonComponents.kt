package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.uistates.ComparisonState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonButton(
    isSelected: Boolean,
    canAdd: Boolean,
    onToggleComparison: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor =
        when {
            isSelected -> MaterialTheme.colorScheme.primary
            canAdd -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        }

    val contentColor =
        when {
            isSelected -> MaterialTheme.colorScheme.onPrimary
            canAdd -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> MaterialTheme.colorScheme.outline
        }

    IconButton(
        onClick = onToggleComparison,
        enabled = canAdd || isSelected,
        modifier =
        modifier
            .size(40.dp)
            .background(color = backgroundColor, shape = CircleShape)
    ) {
        Icon(
            imageVector = if (isSelected) Icons.Default.Close else Icons.Default.Add,
            contentDescription =
            if (isSelected) "Remove from comparison" else "Add to comparison",
            tint = contentColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ComparisonFloatingButton(
    comparisonState: ComparisonState,
    onViewComparison: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = comparisonState.hasSelections,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier
    ) {
        ExtendedFloatingActionButton(
            onClick = onViewComparison,
            icon = {
                Box {
                    Icon(
                        imageVector = Icons.Default.Compare,
                        contentDescription = "Compare cars"
                    )
                    if (comparisonState.selectedCars.isNotEmpty()) {
                        Badge(modifier = Modifier.align(Alignment.TopEnd)) {
                            Text(
                                comparisonState.selectedCars.size
                                    .toString()
                            )
                        }
                    }
                }
            },
            text = { Text("Compare (${comparisonState.selectedCars.size})") },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ComparisonPreview(
    comparisonState: ComparisonState,
    onRemoveCar: (String) -> Unit,
    onViewComparison: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = comparisonState.hasSelections,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
            CardDefaults.cardColors(
                containerColor =
                MaterialTheme.colorScheme.secondaryContainer.copy(
                    alpha = 0.7f
                )
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text =
                        "Compare Cars (${comparisonState.selectedCars.size}/${comparisonState.maxComparisons})",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color =
                        MaterialTheme.colorScheme
                            .onSecondaryContainer
                    )

                    ExtendedFloatingActionButton(
                        onClick = onViewComparison,
                        text = { Text("Compare") },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Compare,
                                contentDescription =
                                "View comparison"
                            )
                        },
                        modifier = Modifier.height(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(comparisonState.selectedCars) { car ->
                        ComparisonCarItem(
                            car = car,
                            onRemove = { onRemoveCar(car.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonCarItem(car: Car, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.width(120.dp),
        colors =
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(8.dp)) {
                AsyncImage(
                    model =
                    car.imageUrl.ifEmpty {
                        "https://via.placeholder.com/150x100?text=${car.brand}"
                    },
                    contentDescription = "${car.brand} ${car.model}",
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${car.brand} ${car.model}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${car.dailyRate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = onRemove,
                modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .size(20.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove from comparison",
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
