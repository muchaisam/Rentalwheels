package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.msdc.rentalwheels.uistates.SortOption

@Composable
fun BrowseFiltersSection(
    filters: BrowseFilters,
    availableCategories: List<String>,
    availableFuelTypes: List<String>,
    availableTransmissions: List<String>,
    onFiltersUpdate: (BrowseFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                                .secondary.copy(
                                    alpha = 0.05f
                                ),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Quick Filters Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Filters",
                        style = Typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Sort Dropdown
                    Box {
                        Card(
                            onClick = { showSortMenu = !showSortMenu },
                            colors =
                            CardDefaults.cardColors(
                                containerColor =
                                MaterialTheme.colorScheme.primaryContainer
                                    .copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier =
                                Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sort,
                                    contentDescription = "Sort",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Sort",
                                    style = Typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector =
                                    if (showSortMenu) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showSortMenu,
                            onDismissRequest = { showSortMenu = false }
                        ) {
                            SortOption.values().forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = option.displayName,
                                                style = Typography.bodyMedium
                                            )
                                            if (filters.sortBy == option) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint =
                                                    MaterialTheme.colorScheme
                                                        .primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        onFiltersUpdate(filters.copy(sortBy = option))
                                        showSortMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Category Filters
                if (availableCategories.isNotEmpty()) {
                    Text(
                        text = "Categories",
                        style = Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(listOf("All") + availableCategories) { category ->
                            FilterChip(
                                selected = filters.selectedCategory == category,
                                onClick = {
                                    onFiltersUpdate(filters.copy(selectedCategory = category))
                                },
                                label = {
                                    Text(text = category, style = Typography.labelMedium)
                                },
                                colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor =
                                    MaterialTheme.colorScheme.primary,
                                    selectedLabelColor =
                                    MaterialTheme.colorScheme.onPrimary,
                                    containerColor =
                                    MaterialTheme.colorScheme.surfaceVariant
                                        .copy(alpha = 0.5f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Fuel Type Filters
                if (availableFuelTypes.isNotEmpty()) {
                    Text(
                        text = "Fuel Type",
                        style = Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(listOf("All") + availableFuelTypes) { fuelType ->
                            FilterChip(
                                selected = filters.selectedFuelType == fuelType,
                                onClick = {
                                    onFiltersUpdate(filters.copy(selectedFuelType = fuelType))
                                },
                                label = {
                                    Text(text = fuelType, style = Typography.labelMedium)
                                },
                                colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor =
                                    MaterialTheme.colorScheme.secondary,
                                    selectedLabelColor =
                                    MaterialTheme.colorScheme.onSecondary,
                                    containerColor =
                                    MaterialTheme.colorScheme.surfaceVariant
                                        .copy(alpha = 0.5f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Transmission Filters
                if (availableTransmissions.isNotEmpty()) {
                    Text(
                        text = "Transmission",
                        style = Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(listOf("All") + availableTransmissions) { transmission ->
                            FilterChip(
                                selected = filters.selectedTransmission == transmission,
                                onClick = {
                                    onFiltersUpdate(
                                        filters.copy(selectedTransmission = transmission)
                                    )
                                },
                                label = {
                                    Text(text = transmission, style = Typography.labelMedium)
                                },
                                colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor =
                                    MaterialTheme.colorScheme.tertiary,
                                    selectedLabelColor =
                                    MaterialTheme.colorScheme.onTertiary,
                                    containerColor =
                                    MaterialTheme.colorScheme.surfaceVariant
                                        .copy(alpha = 0.5f)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Driver Option
                Card(
                    colors =
                    CardDefaults.cardColors(
                        containerColor =
                        MaterialTheme.colorScheme.surfaceVariant.copy(
                            alpha = 0.3f
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "With Driver",
                                style = Typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Include cars with driver service",
                                style = Typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Switch(
                            checked = filters.withDriver,
                            onCheckedChange = { isChecked ->
                                onFiltersUpdate(filters.copy(withDriver = isChecked))
                            },
                            colors =
                            SwitchDefaults.colors(
                                checkedThumbColor =
                                MaterialTheme.colorScheme.primary,
                                checkedTrackColor =
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            }
        }
    }
}
