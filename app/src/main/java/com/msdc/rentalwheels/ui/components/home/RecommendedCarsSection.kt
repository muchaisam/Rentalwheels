package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.PremiumCarCard
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlin.math.absoluteValue

/** Premium Recommended Cars Section with Glassmorphic Design */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecommendedCarsSection(
        cars: List<Car>,
        onCarClick: (String) -> Unit,
        onSeeAllClick: () -> Unit = {},
        modifier: Modifier = Modifier,
        isLoading: Boolean = false
) {
    // Animation for section entrance
    val sectionAnimation = remember { Animatable(0f) }

    // Start entrance animation
    LaunchedEffect(Unit) {
        sectionAnimation.animateTo(
                targetValue = 1f,
                animationSpec =
                        spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                        )
        )
    }

    // LazyList state for scroll position tracking
    val lazyListState = rememberLazyListState()

    // Premium glassmorphic container
    Card(
            modifier =
                    modifier.fillMaxWidth()
                            .graphicsLayer {
                                // Entry animation
                                alpha = sectionAnimation.value
                                translationY = (1 - sectionAnimation.value) * 50
                            }
                            .semantics {
                                contentDescription =
                                        "Recommended cars section with ${cars.size} premium vehicles"
                                heading()
                            },
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
                    )
    ) {
        Box {
            // Gradient background overlay for premium feel
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .height(RentalWheelsDesignSystem.Spacing.Huge * 0.625f)
                                    .background(
                                            brush =
                                                    Brush.horizontalGradient(
                                                            colors =
                                                                    listOf(
                                                                            MaterialTheme
                                                                                    .colorScheme
                                                                                    .primary.copy(
                                                                                    alpha = 0.08f
                                                                            ),
                                                                            MaterialTheme
                                                                                    .colorScheme
                                                                                    .primaryContainer
                                                                                    .copy(
                                                                                            alpha =
                                                                                                    0.12f
                                                                                    )
                                                                    )
                                                    )
                                    )
            )

            Column(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .padding(RentalWheelsDesignSystem.Spacing.extraLarge)
            ) {
                // Enhanced Premium Header
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =
                                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
                    ) {
                        // Premium icon with gradient background
                        Box(
                                modifier =
                                        Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 0.75f)
                                                .clip(CircleShape)
                                                .background(
                                                        brush =
                                                                Brush.linearGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        MaterialTheme
                                                                                                .colorScheme
                                                                                                .primary,
                                                                                        MaterialTheme
                                                                                                .colorScheme
                                                                                                .primaryContainer
                                                                                )
                                                                )
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                    imageVector = Icons.Rounded.ThumbUp,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                            )
                        }

                        Column {
                            Text(
                                    text = "Recommended Cars",
                                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                    text = "${cars.size} top picks for you",
                                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Premium See All Button
                    Surface(
                            onClick = onSeeAllClick,
                            shape =
                                    RoundedCornerShape(
                                            RentalWheelsDesignSystem.CornerRadius.medium
                                    ),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            tonalElevation = RentalWheelsDesignSystem.Elevation.small,
                            modifier = Modifier.semantics { contentDescription = "View all cars" }
                    ) {
                        Row(
                                modifier =
                                        Modifier.padding(
                                                horizontal =
                                                        RentalWheelsDesignSystem.Spacing.medium,
                                                vertical = RentalWheelsDesignSystem.Spacing.small
                                        ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement =
                                        Arrangement.spacedBy(
                                                RentalWheelsDesignSystem.Spacing.extraSmall
                                        )
                        ) {
                            Text(
                                    text = "See All",
                                    style = RentalWheelsDesignSystem.Typography.labelLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Icon(
                                    imageVector = Icons.Rounded.ArrowForward,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier =
                                            Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                // Content Section
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (isLoading) {
                        CarListShimmer()
                    } else if (cars.isEmpty()) {
                        EmptyState()
                    } else {
                        // Enhanced Premium Car Carousel
                        LazyRow(
                                state = lazyListState,
                                contentPadding =
                                        PaddingValues(
                                                horizontal = RentalWheelsDesignSystem.Spacing.XXS
                                        ),
                                horizontalArrangement =
                                        Arrangement.spacedBy(
                                                RentalWheelsDesignSystem.Spacing.medium
                                        ),
                                modifier =
                                        Modifier.fillMaxWidth().semantics {
                                            contentDescription =
                                                    "Scroll to see more recommended cars"
                                        }
                        ) {
                            itemsIndexed(items = cars, key = { _, car -> car.id }) { index, car ->
                                // Premium Car Card with parallax effect
                                PremiumCarCard(
                                        car = car,
                                        onClick = { onCarClick(car.id) },
                                        modifier =
                                                Modifier.width(
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .Huge * 4.375f
                                                        )
                                                        .graphicsLayer {
                                                            // Subtle parallax effect based on
                                                            // scroll
                                                            val scrollOffset =
                                                                    lazyListState
                                                                            .firstVisibleItemScrollOffset
                                                                            .toFloat()
                                                            val itemIndex =
                                                                    lazyListState
                                                                            .firstVisibleItemIndex
                                                            if (index == itemIndex) {
                                                                translationX = scrollOffset * 0.1f
                                                            }
                                                        }
                                )
                            }
                        }
                    }
                }

                // Premium scroll indicators with enhanced styling
                if (cars.isNotEmpty() && !isLoading) {
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Navigation buttons
                        Row(
                                horizontalArrangement =
                                        Arrangement.spacedBy(
                                                RentalWheelsDesignSystem.Spacing.extraSmall
                                        )
                        ) {
                            // Previous button
                            IconButton(
                                    onClick = {
                                        // Scroll to previous item (implement scroll logic)
                                    },
                                    enabled = lazyListState.firstVisibleItemIndex > 0
                            ) {
                                Surface(
                                        shape = CircleShape,
                                        color =
                                                if (lazyListState.firstVisibleItemIndex > 0)
                                                        MaterialTheme.colorScheme.primaryContainer
                                                else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier =
                                                Modifier.size(RentalWheelsDesignSystem.Spacing.XXL)
                                ) {
                                    Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                                imageVector = Icons.Rounded.ChevronLeft,
                                                contentDescription = "Previous",
                                                tint =
                                                        if (lazyListState.firstVisibleItemIndex > 0)
                                                                MaterialTheme.colorScheme
                                                                        .onPrimaryContainer
                                                        else
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant.copy(
                                                                        alpha = 0.4f
                                                                ),
                                                modifier =
                                                        Modifier.size(
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .large
                                                        )
                                        )
                                    }
                                }
                            }

                            // Next button
                            IconButton(
                                    onClick = {
                                        // Scroll to next item (implement scroll logic)
                                    },
                                    enabled = lazyListState.firstVisibleItemIndex < cars.size - 1
                            ) {
                                Surface(
                                        shape = CircleShape,
                                        color =
                                                if (lazyListState.firstVisibleItemIndex <
                                                                cars.size - 1
                                                )
                                                        MaterialTheme.colorScheme.primaryContainer
                                                else MaterialTheme.colorScheme.surfaceVariant,
                                        modifier =
                                                Modifier.size(RentalWheelsDesignSystem.Spacing.XXL)
                                ) {
                                    Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                                imageVector = Icons.Rounded.ChevronRight,
                                                contentDescription = "Next",
                                                tint =
                                                        if (lazyListState.firstVisibleItemIndex <
                                                                        cars.size - 1
                                                        )
                                                                MaterialTheme.colorScheme
                                                                        .onPrimaryContainer
                                                        else
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant.copy(
                                                                        alpha = 0.4f
                                                                ),
                                                modifier =
                                                        Modifier.size(
                                                                RentalWheelsDesignSystem.Spacing
                                                                        .large
                                                        )
                                        )
                                    }
                                }
                            }
                        }

                        // Enhanced position indicators
                        Row(
                                horizontalArrangement =
                                        Arrangement.spacedBy(
                                                RentalWheelsDesignSystem.Spacing.extraSmall
                                        ),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            val firstVisibleIndex = lazyListState.firstVisibleItemIndex
                            val maxIndicators = 5 // Show max 5 dots
                            val totalItems = cars.size

                            if (totalItems <= maxIndicators) {
                                // Show all dots if items <= maxIndicators
                                for (i in 0 until totalItems) {
                                    IndicatorDot(isActive = i == firstVisibleIndex)
                                }
                            } else {
                                // Show condensed view with numbers
                                Text(
                                        text = "${firstVisibleIndex + 1}",
                                        style = RentalWheelsDesignSystem.Typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                        text = "/",
                                        style = RentalWheelsDesignSystem.Typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                        text = "$totalItems",
                                        style = RentalWheelsDesignSystem.Typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Premium indicator dot with animation */
@Composable
private fun IndicatorDot(isActive: Boolean) {
    Box(
            modifier =
                    Modifier.size(
                                    if (isActive) RentalWheelsDesignSystem.Spacing.small
                                    else RentalWheelsDesignSystem.Spacing.extraSmall
                            )
                            .clip(CircleShape)
                            .background(
                                    if (isActive) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
    )
}

@Composable
private fun ScrollIndicator(isStart: Boolean, onClick: () -> Unit) {
    Surface(
            onClick = onClick,
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = RentalWheelsDesignSystem.Elevation.small,
            shadowElevation = RentalWheelsDesignSystem.Elevation.medium,
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.XXXL)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                    imageVector =
                            if (isStart) Icons.Rounded.ChevronLeft else Icons.Rounded.ChevronRight,
                    contentDescription = if (isStart) "Scroll to start" else "Scroll to end",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge)
            )
        }
    }
}

@Composable
private fun ScrollIndicatorDots(itemCount: Int, lazyListState: LazyListState, itemWidthDp: Dp) {
    val density = LocalDensity.current
    val itemWidthPx = with(density) { itemWidthDp.toPx() }

    // Calculate current position
    val scrollOffset = lazyListState.firstVisibleItemScrollOffset
    val firstVisibleItem = lazyListState.firstVisibleItemIndex

    // Calculate effective position including partial scrolling
    val position = firstVisibleItem + (scrollOffset / itemWidthPx)

    Row(
            horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.extraSmall),
            verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until itemCount) {
            // Calculate how close we are to this dot's position
            val distanceFromCenter = (i.toFloat() - position).absoluteValue
            val scale = 1f - (distanceFromCenter * 0.25f).coerceIn(0f, 0.5f)
            val alpha = 0.3f + (1f - distanceFromCenter).coerceIn(0f, 0.7f)

            Box(
                    modifier =
                            Modifier.size(RentalWheelsDesignSystem.Spacing.extraSmall * scale)
                                    .clip(CircleShape)
                                    .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                                    )
            )
        }
    }
}

/** Premium shimmer loading state */
@Composable
private fun CarListShimmer() {
    LazyRow(
            contentPadding = PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.XXS),
            horizontalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        items(3) {
            Card(
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
                    colors =
                            CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                    modifier =
                            Modifier.width(RentalWheelsDesignSystem.Spacing.Huge * 4.375f)
                                    .height(RentalWheelsDesignSystem.Spacing.Huge * 5.625f)
            ) {
                // Shimmer animation effect here
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .background(
                                                brush =
                                                        Brush.linearGradient(
                                                                colors =
                                                                        listOf(
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .surfaceVariant,
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .surfaceVariant
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.7f
                                                                                        ),
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .surfaceVariant
                                                                        )
                                                        )
                                        )
                )
            }
        }
    }
}

/** Premium empty state with enhanced styling */
@Composable
private fun EmptyState() {
    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .height(RentalWheelsDesignSystem.Spacing.Huge * 5f)
                            .padding(RentalWheelsDesignSystem.Spacing.large),
            contentAlignment = Alignment.Center
    ) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            // Icon with gradient background
            Box(
                    modifier =
                            Modifier.size(RentalWheelsDesignSystem.Spacing.Huge)
                                    .clip(CircleShape)
                                    .background(
                                            MaterialTheme.colorScheme.primaryContainer.copy(
                                                    alpha = 0.3f
                                            )
                                    ),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.XXL)
                )
            }

            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement =
                            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.extraSmall)
            ) {
                Text(
                        text = "No Recommendations Yet",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                        text = "Check back later for personalized car recommendations",
                        style = RentalWheelsDesignSystem.Typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
