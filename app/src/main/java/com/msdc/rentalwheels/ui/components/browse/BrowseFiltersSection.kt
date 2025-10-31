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
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
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
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
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
            Column(modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.medium)) {
                // Quick Filters Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Filters",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
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
                            shape =
                            RoundedCornerShape(
                                RentalWheelsDesignSystem.CornerRadius.medium
                            )
                        ) {
                            Row(
                                modifier =
                                Modifier.padding(
                                    horizontal =
                                    RentalWheelsDesignSystem.Spacing.small,
                                    vertical =
                                    RentalWheelsDesignSystem.Spacing.small
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = "Sort",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier =
                                    Modifier.size(
                                        RentalWheelsDesignSystem.Spacing.medium
                                    )
                                )
                                Spacer(
                                    modifier =
                                    Modifier.width(RentalWheelsDesignSystem.Spacing.XXS)
                                )
                                Text(
                                    text = "Sort",
                                    style = RentalWheelsDesignSystem.Typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector =
                                    if (showSortMenu) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier =
                                    Modifier.size(
                                        RentalWheelsDesignSystem.Spacing.medium
                                    )
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
                                                style =
                                                RentalWheelsDesignSystem.Typography
                                                    .bodyMedium
                                            )
                                            if (filters.sortBy == option) {
                                                Spacer(
                                                    modifier =
                                                    Modifier.width(
                                                        RentalWheelsDesignSystem
                                                            .Spacing.small
                                                    )
                                                )
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint =
                                                    MaterialTheme.colorScheme
                                                        .primary,
                                                    modifier =
                                                    Modifier.size(
                                                        RentalWheelsDesignSystem
                                                            .Spacing.medium
                                                    )
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

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                // Category Filters
                if (availableCategories.isNotEmpty()) {
                    Text(
                        text = "Categories",
                        style = RentalWheelsDesignSystem.Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier =
                        Modifier.padding(
                            bottom = RentalWheelsDesignSystem.Spacing.small
                        )
                    )

                    LazyRow(
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
                        contentPadding =
                        PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
                    ) {
                        items(listOf("All") + availableCategories) { category ->
                            FilterChip(
                                selected = filters.selectedCategory == category,
                                onClick = {
                                    onFiltersUpdate(filters.copy(selectedCategory = category))
                                },
                                label = {
                                    Text(
                                        text = category,
                                        style =
                                        RentalWheelsDesignSystem.Typography
                                            .labelMedium
                                    )
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

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
                }

                // Fuel Type Filters
                if (availableFuelTypes.isNotEmpty()) {
                    Text(
                        text = "Fuel Type",
                        style = RentalWheelsDesignSystem.Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier =
                        Modifier.padding(
                            bottom = RentalWheelsDesignSystem.Spacing.small
                        )
                    )

                    LazyRow(
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
                        contentPadding =
                        PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
                    ) {
                        items(listOf("All") + availableFuelTypes) { fuelType ->
                            FilterChip(
                                selected = filters.selectedFuelType == fuelType,
                                onClick = {
                                    onFiltersUpdate(filters.copy(selectedFuelType = fuelType))
                                },
                                label = {
                                    Text(
                                        text = fuelType,
                                        style =
                                        RentalWheelsDesignSystem.Typography
                                            .labelMedium
                                    )
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

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
                }

                // Transmission Filters
                if (availableTransmissions.isNotEmpty()) {
                    Text(
                        text = "Transmission",
                        style = RentalWheelsDesignSystem.Typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier =
                        Modifier.padding(
                            bottom = RentalWheelsDesignSystem.Spacing.small
                        )
                    )

                    LazyRow(
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
                        contentPadding =
                        PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
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
                                    Text(
                                        text = transmission,
                                        style =
                                        RentalWheelsDesignSystem.Typography
                                            .labelMedium
                                    )
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

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
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
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium)
                ) {
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(RentalWheelsDesignSystem.Spacing.medium),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "With Driver",
                                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Include cars with driver service",
                                style = RentalWheelsDesignSystem.Typography.bodySmall,
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
