package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.uistates.RangeFilterState

@Composable
fun EnhancedRangeFilters(
    rangeState: RangeFilterState,
    onPriceRangeChange: (Pair<Float, Float>) -> Unit,
    onYearRangeChange: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Range Filters",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            PriceRangeSlider(
                currentRange = rangeState.priceRange,
                minValue = rangeState.minPrice,
                maxValue = rangeState.maxPrice,
                onRangeChange = onPriceRangeChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            YearRangeSlider(
                currentRange = rangeState.yearRange,
                minValue = rangeState.minYear,
                maxValue = rangeState.maxYear,
                onRangeChange = onYearRangeChange
            )
        }
    }
}

@Composable
private fun PriceRangeSlider(
    currentRange: Pair<Float, Float>,
    minValue: Float,
    maxValue: Float,
    onRangeChange: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by
    remember(currentRange) { mutableStateOf(currentRange.first..currentRange.second) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Price Range",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$${currentRange.first.toInt()} - $${currentRange.second.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                onRangeChange(Pair(range.start, range.endInclusive))
            },
            valueRange = minValue..maxValue,
            steps = 20,
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
                text = "$${minValue.toInt()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$${maxValue.toInt()}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun YearRangeSlider(
    currentRange: Pair<Float, Float>,
    minValue: Float,
    maxValue: Float,
    onRangeChange: (Pair<Float, Float>) -> Unit,
    modifier: Modifier = Modifier
) {
    var sliderPosition by
    remember(currentRange) { mutableStateOf(currentRange.first..currentRange.second) }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Year Range",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${currentRange.first.toInt()} - ${currentRange.second.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range ->
                sliderPosition = range
                onRangeChange(Pair(range.start, range.endInclusive))
            },
            valueRange = minValue..maxValue,
            steps = (maxValue - minValue).toInt() - 1,
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
                text = minValue.toInt().toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = maxValue.toInt().toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
