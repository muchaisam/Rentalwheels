package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun FilterableCarList(
    carsByFilter: Map<String, List<Car>>,
    filterName: String,
    onCarClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: String? = null
) {
    // State management
    var selectedFilter by remember { mutableStateOf<String?>(null) }
    val displayedCars by
    remember(carsByFilter, selectedFilter) {
        derivedStateOf {
            selectedFilter?.let { filter -> carsByFilter[filter] ?: emptyList() }
                ?: carsByFilter.values.flatten()
        }
    }

    // Premium Card container with gradient overlay
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Subtle gradient background overlay
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        brush =
                        Brush.verticalGradient(
                            colors =
                            listOf(
                                MaterialTheme
                                    .colorScheme
                                    .primary.copy(
                                        alpha = 0.08f
                                    ),
                                MaterialTheme
                                    .colorScheme
                                    .primary.copy(
                                        alpha = 0.03f
                                    ),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = RentalWheelsDesignSystem.Spacing.extraLarge)
            ) {
                // Premium Header with gradient accent
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                            RentalWheelsDesignSystem.Spacing.extraLarge
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Filter by $filterName",
                            style = RentalWheelsDesignSystem.Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${displayedCars.size} vehicles available",
                            style = RentalWheelsDesignSystem.Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier =
                            Modifier.padding(top = RentalWheelsDesignSystem.Spacing.XXS)
                        )
                    }

                    // Glassmorphic icon badge
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                // Glassmorphic Filter Chips Container
                Card(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                            RentalWheelsDesignSystem.Spacing.extraLarge
                        ),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                    colors =
                    CardDefaults.cardColors(
                        containerColor =
                        MaterialTheme.colorScheme.surfaceContainer.copy(
                            alpha = 0.6f
                        )
                    ),
                    elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = RentalWheelsDesignSystem.Elevation.small
                    )
                ) {
                    LazyRow(
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
                        contentPadding =
                        PaddingValues(
                            vertical = RentalWheelsDesignSystem.Spacing.medium
                        ),
                    ) {
                        items(carsByFilter.keys.toList()) { filter ->
                            val isSelected = filter == selectedFilter
                            // Animate selection changes with scale effect
                            val scale by
                            animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1f,
                                animationSpec =
                                spring(
                                    dampingRatio =
                                    Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                                ),
                                label = "filter_chip_scale"
                            )

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = if (isSelected) null else filter },
                                label = {
                                    Text(
                                        text = filter,
                                        style =
                                        RentalWheelsDesignSystem.Typography
                                            .bodyMedium,
                                        fontWeight =
                                        if (isSelected) FontWeight.SemiBold
                                        else FontWeight.Normal
                                    )
                                },
                                leadingIcon =
                                if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Rounded.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                } else null,
                                colors =
                                FilterChipDefaults.filterChipColors(
                                    selectedContainerColor =
                                    MaterialTheme.colorScheme
                                        .primaryContainer,
                                    selectedLabelColor =
                                    MaterialTheme.colorScheme
                                        .onPrimaryContainer,
                                    containerColor =
                                    MaterialTheme.colorScheme.surface.copy(
                                        alpha = 0.8f
                                    )
                                ),
                                border =
                                FilterChipDefaults.filterChipBorder(
                                    borderColor =
                                    MaterialTheme.colorScheme.outline.copy(
                                        alpha = 0.3f
                                    ),
                                    selectedBorderColor =
                                    MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.8f
                                    ),
                                    enabled = true,
                                    selected = isSelected,
                                    borderWidth = 1.5.dp,
                                    selectedBorderWidth = 2.dp
                                ),
                                modifier =
                                Modifier.graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                // Content Section with improved states
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                            RentalWheelsDesignSystem.Spacing.extraLarge
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> {
                            LoadingState()
                        }

                        error != null -> {
                            ErrorState(error = error)
                        }

                        displayedCars.isEmpty() -> {
                            EmptyState()
                        }

                        else -> {
                            // Horizontal scrollable car list with snap behavior
                            val lazyListState = rememberLazyListState()
                            val snapFlingBehavior =
                                rememberSnapFlingBehavior(lazyListState = lazyListState)

                            LazyRow(
                                state = lazyListState,
                                flingBehavior = snapFlingBehavior,
                                horizontalArrangement =
                                Arrangement.spacedBy(
                                    RentalWheelsDesignSystem.Spacing.medium
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            ) {
                                items(items = displayedCars, key = { car -> car.id }) { car ->
                                    // Car card with fixed width for horizontal scroll (peek effect)
                                    CarCard(
                                        car = car,
                                        onClick = { onCarClick(car.id) },
                                        modifier =
                                        Modifier
                                            .width(320.dp)
                                            .clip(
                                                RoundedCornerShape(
                                                    RentalWheelsDesignSystem
                                                        .CornerRadius
                                                        .extraLarge
                                                )
                                            )
                                    )
                                }

                                // "View All" card at the end if there are more than 3 cars
                                if (displayedCars.size > 3) {
                                    item {
                                        ViewAllCard(
                                            count = displayedCars.size,
                                            filterName = selectedFilter ?: "All"
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp), contentAlignment = Alignment.Center) {
        // Pulsating animation for the loading indicator
        val infiniteTransition = rememberInfiniteTransition(label = "loading_pulse")
        val scale by
        infiniteTransition.animateFloat(
            initialValue = 0.8f,
            targetValue = 1.1f,
            animationSpec =
            infiniteRepeatable(
                animation = tween(800, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "loading_scale"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Glassmorphic loading container
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier =
                        Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

            Text(
                text = "Finding cars for you...",
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(RentalWheelsDesignSystem.Spacing.XXL)
            .heightIn(min = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Premium glassmorphic icon surface
        Card(
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
            colors =
            CardDefaults.cardColors(
                containerColor =
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            ),
            elevation =
            CardDefaults.cardElevation(
                defaultElevation = RentalWheelsDesignSystem.Elevation.small
            ),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                // Gradient background
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            brush =
                            Brush.radialGradient(
                                colors =
                                listOf(
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                        .copy(
                                            alpha =
                                            0.15f
                                        ),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Icon(
                    imageVector = Icons.Rounded.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

        Text(
            text = "No cars found",
            style = RentalWheelsDesignSystem.Typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

        Text(
            text = "Try adjusting your filters to see more options",
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ErrorState(error: String) {
    Column(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(RentalWheelsDesignSystem.Spacing.XXL)
            .heightIn(min = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Premium glassmorphic error icon
        Card(
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
            colors =
            CardDefaults.cardColors(
                containerColor =
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f)
            ),
            elevation =
            CardDefaults.cardElevation(
                defaultElevation = RentalWheelsDesignSystem.Elevation.small
            ),
            modifier = Modifier.size(80.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                // Gradient background
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            brush =
                            Brush.radialGradient(
                                colors =
                                listOf(
                                    MaterialTheme
                                        .colorScheme
                                        .error.copy(
                                            alpha = 0.2f
                                        ),
                                    Color.Transparent
                                )
                            )
                        )
                )
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

        Text(
            text = "Oops! Something went wrong",
            style = RentalWheelsDesignSystem.Typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

        Text(
            text = error,
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.errorContainer,
            tonalElevation = 3.dp,
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Oops! Something went wrong",
            style = RentalWheelsDesignSystem.Typography.titleMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Retry button for better UX
        Button(
            onClick = { /* Implement retry logic */ },
            colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Text("Retry")
            }
        }
    }
}

/** View All Card - Displayed at the end of horizontal scroll */
@Composable
private fun ViewAllCard(count: Int, filterName: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(320.dp)
            .height(380.dp),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        ),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                    Brush.radialGradient(
                        colors =
                        listOf(
                            MaterialTheme.colorScheme
                                .primary.copy(
                                    alpha = 0.2f
                                ),
                            MaterialTheme.colorScheme
                                .primary.copy(
                                    alpha = 0.05f
                                ),
                            Color.Transparent
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.extraLarge)
            ) {
                // Large circular icon background
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                Text(
                    text = "View All",
                    style = RentalWheelsDesignSystem.Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

                Text(
                    text = "$count $filterName Cars",
                    style = RentalWheelsDesignSystem.Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                // CTA Button
                Surface(
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
                    color = MaterialTheme.colorScheme.primary,
                    modifier =
                    Modifier.padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium
                    )
                ) {
                    Row(
                        modifier =
                        Modifier.padding(
                            horizontal = RentalWheelsDesignSystem.Spacing.large,
                            vertical = RentalWheelsDesignSystem.Spacing.small
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
                    ) {
                        Text(
                            text = "Browse All",
                            style = RentalWheelsDesignSystem.Typography.labelLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
