package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.theme.Typography
import com.msdc.rentalwheels.uistates.BrowseFilters
import kotlin.math.roundToInt

@Composable
fun AdvancedFiltersSection(
    filters: BrowseFilters,
    onFiltersUpdate: (BrowseFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAdvancedFilters by remember { mutableStateOf(false) }

    Card(
        modifier =
        modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(20.dp),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    brush =
                    Brush.verticalGradient(
                        colors =
                        listOf(
                            MaterialTheme.colorScheme
                                .tertiary.copy(
                                    alpha = 0.05f
                                ),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Advanced Filters Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Advanced Filters",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Advanced Filters",
                            style = Typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(onClick = { showAdvancedFilters = !showAdvancedFilters }) {
                        Icon(
                            imageVector =
                            if (showAdvancedFilters) Icons.Default.ExpandLess
                            else Icons.Default.ExpandMore,
                            contentDescription =
                            if (showAdvancedFilters) "Collapse" else "Expand",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                AnimatedVisibility(visible = showAdvancedFilters) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        // Price Range Slider
                        PriceRangeSection(
                            minPrice = filters.minPrice,
                            maxPrice = filters.maxPrice,
                            onPriceRangeChange = { min, max ->
                                onFiltersUpdate(filters.copy(minPrice = min, maxPrice = max))
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Year Range Slider
                        YearRangeSection(
                            minYear = filters.minYear,
                            maxYear = filters.maxYear,
                            onYearRangeChange = { min, max ->
                                onFiltersUpdate(filters.copy(minYear = min, maxYear = max))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRangeSection(
    minPrice: Int,
    maxPrice: Int,
    onPriceRangeChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val priceRange = 0f..1000f
    var currentRange by
    remember(minPrice, maxPrice) { mutableStateOf(minPrice.toFloat()..maxPrice.toFloat()) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = "Price Range",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Price Range",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "$${minPrice} - $${maxPrice}",
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RangeSlider(
            value = currentRange,
            onValueChange = { range -> currentRange = range },
            onValueChangeFinished = {
                onPriceRangeChange(
                    currentRange.start.roundToInt(),
                    currentRange.endInclusive.roundToInt()
                )
            },
            valueRange = priceRange,
            colors =
            SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor =
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "$0",
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$1000+",
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun YearRangeSection(
    minYear: Int,
    maxYear: Int,
    onYearRangeChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val yearRange = 2010f..2024f
    var currentRange by
    remember(minYear, maxYear) { mutableStateOf(minYear.toFloat()..maxYear.toFloat()) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Year Range",
                style = Typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "$minYear - $maxYear",
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RangeSlider(
            value = currentRange,
            onValueChange = { range -> currentRange = range },
            onValueChangeFinished = {
                onYearRangeChange(
                    currentRange.start.roundToInt(),
                    currentRange.endInclusive.roundToInt()
                )
            },
            valueRange = yearRange,
            colors =
            SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor =
                MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "2010",
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "2024",
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
