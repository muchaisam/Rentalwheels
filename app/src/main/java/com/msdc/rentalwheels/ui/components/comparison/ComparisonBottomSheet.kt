package com.msdc.rentalwheels.ui.components.comparison

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils

/**
 * Premium Modal Bottom Sheet for side-by-side car comparison Displays comprehensive comparison of 2
 * cars
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonBottomSheet(
    cars: List<Car>,
    onDismiss: () -> Unit,
    onSelectCar: (Car) -> Unit,
    onRemoveCar: (String) -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = RentalWheelsDesignSystem.Elevation.extraLarge
    ) {
        LazyColumn(
            modifier =
            Modifier
                    .fillMaxWidth()
                    .padding(
                            bottom = RentalWheelsDesignSystem.Spacing.extraLarge
                    )
        ) {
            // Header
            item {
                Row(
                    modifier =
                    Modifier
                            .fillMaxWidth()
                            .padding(
                                    horizontal =
                                    RentalWheelsDesignSystem
                                            .Spacing.extraLarge
                            ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Compare Cars",
                        style =
                        RentalWheelsDesignSystem.Typography
                            .headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close comparison",
                            tint =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )
                    }
                }

                Spacer(
                    modifier =
                    Modifier.height(
                        RentalWheelsDesignSystem.Spacing.medium
                    )
                )
            }

            // Car Images and Basic Info
            item {
                Row(
                    modifier =
                    Modifier
                            .fillMaxWidth()
                            .padding(
                                    horizontal =
                                    RentalWheelsDesignSystem
                                            .Spacing.extraLarge
                            ),
                    horizontalArrangement =
                    Arrangement.spacedBy(
                        RentalWheelsDesignSystem.Spacing.medium
                    )
                ) {
                    cars.forEach { car ->
                        CarImageCard(
                            car = car,
                            onRemove = { onRemoveCar(car.id) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Empty slot if only 1 car
                    if (cars.size == 1) {
                        EmptySlotCard(modifier = Modifier.weight(1f))
                    }
                }

                Spacer(
                    modifier =
                    Modifier.height(
                        RentalWheelsDesignSystem.Spacing.extraLarge
                    )
                )
            }

            // Comparison Rows
            item {
                ComparisonSection(
                    title = "Price",
                    values =
                    cars.map {
                        CurrencyUtils.formatDailyRate(it.dailyRate)
                    }
                )
            }

            item {
                ComparisonSection(
                    title = "Year",
                    values = cars.map { it.year.toString() }
                )
            }

            item {
                ComparisonSection(
                    title = "Fuel Type",
                    values = cars.map { it.fuelType }
                )
            }

            item {
                ComparisonSection(
                    title = "Transmission",
                    values = cars.map { it.transmission }
                )
            }

            item { ComparisonSection(title = "Brand", values = cars.map { it.brand }) }

            // Action Buttons
            item {
                Spacer(
                    modifier =
                    Modifier.height(
                        RentalWheelsDesignSystem.Spacing.large
                    )
                )

                Row(
                    modifier =
                    Modifier
                            .fillMaxWidth()
                            .padding(
                                    horizontal =
                                    RentalWheelsDesignSystem
                                            .Spacing.extraLarge
                            ),
                    horizontalArrangement =
                    Arrangement.spacedBy(
                        RentalWheelsDesignSystem.Spacing.medium
                    )
                ) {
                    cars.forEach { car ->
                        Button(
                            onClick = { onSelectCar(car) },
                            modifier = Modifier.weight(1f),
                            shape =
                            RoundedCornerShape(
                                RentalWheelsDesignSystem
                                    .CornerRadius.medium
                            ),
                            colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                MaterialTheme
                                    .colorScheme
                                    .primary
                            )
                        ) {
                            Text(
                                text = "Select",
                                style =
                                RentalWheelsDesignSystem
                                    .Typography
                                    .labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (cars.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun CarImageCard(car: Car, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box {
            Column {
                // Car Image
                Box(
                    modifier =
                    Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(
                                    RoundedCornerShape(
                                            topStart =
                                            RentalWheelsDesignSystem
                                                    .CornerRadius
                                                    .large,
                                            topEnd =
                                            RentalWheelsDesignSystem
                                                    .CornerRadius
                                                    .large
                                    )
                            )
                ) {
                    AsyncImage(
                        model = car.imageUrl,
                        contentDescription = "${car.brand} ${car.model}",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient overlay
                    Box(
                        modifier =
                        Modifier
                                .fillMaxWidth()
                                .height(120.dp)
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
                }

                // Car Name
                Column(
                    modifier =
                    Modifier
                            .fillMaxWidth()
                            .padding(
                                    RentalWheelsDesignSystem.Spacing
                                            .medium
                            )
                ) {
                    Text(
                        text = car.brand,
                        style =
                        RentalWheelsDesignSystem.Typography
                            .labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = car.model,
                        style =
                        RentalWheelsDesignSystem.Typography
                            .titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Remove button
            Surface(
                modifier =
                Modifier
                        .align(Alignment.TopEnd)
                        .padding(RentalWheelsDesignSystem.Spacing.small)
                        .size(32.dp),
                onClick = onRemove,
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                shape =
                RoundedCornerShape(
                    RentalWheelsDesignSystem.CornerRadius.small
                ),
                tonalElevation = RentalWheelsDesignSystem.Elevation.small
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Remove car",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptySlotCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceContainerHighest.copy(
                alpha = 0.5f
            )
        ),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
    ) {
        Box(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select another car\nto compare",
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ComparisonSection(title: String, values: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider(
            modifier =
            Modifier.padding(
                horizontal = RentalWheelsDesignSystem.Spacing.extraLarge
            ),
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

        Row(
            modifier =
            Modifier
                    .fillMaxWidth()
                    .padding(
                            horizontal =
                            RentalWheelsDesignSystem.Spacing.extraLarge
                    ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Title
            Text(
                text = title,
                style = RentalWheelsDesignSystem.Typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(0.8f)
            )

            // Values
            Row(
                modifier = Modifier.weight(2f),
                horizontalArrangement =
                Arrangement.spacedBy(
                    RentalWheelsDesignSystem.Spacing.medium
                )
            ) {
                values.forEach { value ->
                    Surface(
                        modifier = Modifier.weight(1f),
                        color =
                        MaterialTheme.colorScheme.primaryContainer
                            .copy(alpha = 0.5f),
                        shape =
                        RoundedCornerShape(
                            RentalWheelsDesignSystem
                                .CornerRadius.small
                        ),
                        tonalElevation =
                        RentalWheelsDesignSystem.Elevation
                            .extraSmall
                    ) {
                        Text(
                            text = value,
                            style =
                            RentalWheelsDesignSystem.Typography
                                .bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier =
                            Modifier.padding(
                                vertical =
                                RentalWheelsDesignSystem
                                    .Spacing
                                    .small,
                                horizontal =
                                RentalWheelsDesignSystem
                                    .Spacing
                                    .extraSmall
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Empty slot
                if (values.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))
    }
}
