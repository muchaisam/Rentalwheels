package com.msdc.rentalwheels.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car

@Composable
fun FilterableCarList(
    carsByFilter: Map<String, List<Car>>,
    filterName: String,
    onCarClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val displayedCars by remember(carsByFilter, selectedFilter) {
        derivedStateOf {
            when (selectedFilter) {
                null -> carsByFilter.values.flatten()
                else -> carsByFilter[selectedFilter] ?: emptyList()
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Cars by $filterName",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            carsByFilter.forEach { (filter, cars) ->
                item(key = "filter_$filter") {
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = if (selectedFilter == filter) null else filter },
                        label = { Text("$filter (${cars.size})") },
                        leadingIcon = if (selectedFilter == filter) {
                            { Icon(Icons.Rounded.Check, null) }
                        } else null
                    )
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item(key = "spacer") {
                Spacer(modifier = Modifier.width(0.dp))
            }

            items(
                items = displayedCars,
                key = { it.id }
            ) { car ->
                CarCard(
                    car = car,
                    onClick = { onCarClick(car.id) }
                )
            }
        }
    }
}
