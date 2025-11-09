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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.PremiumButton
import com.msdc.rentalwheels.ui.components.PremiumButtonVariant
import com.msdc.rentalwheels.ui.components.PremiumCarCard
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlin.math.absoluteValue

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


    // Snap fling behavior for scroll snappingection
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = RentalWheelsDesignSystem.Elevation.extraSmall,
        shadowElevation = RentalWheelsDesignSystem.Elevation.small,
        shape = MaterialTheme.shapes.medium,
        modifier =
        modifier
            .fillMaxWidth()
            .padding(RentalWheelsDesignSystem.Spacing.extraSmall)
            .graphicsLayer {
                // Entry animation
                alpha = sectionAnimation.value
                translationY = (1 - sectionAnimation.value) * 50
            }
            .semantics {
                contentDescription =
                    "Recommended cars section with ${cars.size} cars"
                heading()
            }
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = RentalWheelsDesignSystem.Spacing.medium)
        ) {
            // Enhanced section header with animations
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium,
                        vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.extraSmall)
                ) {
                    // Icon with primary color
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.XXL)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier =
                            Modifier
                                .size(RentalWheelsDesignSystem.Spacing.medium)
                                .padding(
                                    RentalWheelsDesignSystem.Spacing.extraSmall
                                )
                        )
                    }

                    // Title with primary color accent
                    Text(
                        text = "Recommended Cars",
                        style = RentalWheelsDesignSystem.Typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Premium "See All" button
                PremiumButton(onClick = onSeeAllClick, variant = PremiumButtonVariant.Secondary) {
                    Text("See All")
                }
            }

            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = RentalWheelsDesignSystem.Spacing.extraSmall)
            ) {
                if (isLoading) {
                    // Placeholder loading state
                    CarListShimmer()
                } else if (cars.isEmpty()) {
                    // Empty state
                    EmptyState()
                } else {
                    // Enhanced LazyRow with card animations
                    LazyRow(
                        state = lazyListState,
                        contentPadding =
                        PaddingValues(
                            horizontal = RentalWheelsDesignSystem.Spacing.medium
                        ),
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium),
                        modifier =
                        Modifier
                            .height(RentalWheelsDesignSystem.Spacing.Huge * 3.125f)
                            .fillMaxWidth()
                            .semantics {
                                contentDescription =
                                    "Scroll to see more recommended cars"
                            }
                    ) {
                        itemsIndexed(items = cars, key = { _, car -> car.id }) { index, car ->
                            // Premium Car Card with enhanced interaction
                            PremiumCarCard(
                                car = car,
                                onClick = { onCarClick(car.id) },
                                modifier =
                                Modifier.width(
                                    RentalWheelsDesignSystem.Spacing.Huge * 4.375f
                                )
                            )
                        }
                    }
                }
            }

            // Simple scroll position indicator dots
            if (cars.isNotEmpty() && !isLoading) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = RentalWheelsDesignSystem.Spacing.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement =
                        Arrangement.spacedBy(
                            RentalWheelsDesignSystem.Spacing.extraSmall
                        ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val firstVisibleIndex = lazyListState.firstVisibleItemIndex
                        for (i in 0 until cars.size) {
                            val isActive = i == firstVisibleIndex
                            Box(
                                modifier =
                                Modifier
                                    .size(
                                        if (isActive)
                                            RentalWheelsDesignSystem.Spacing
                                                .small
                                        else
                                            RentalWheelsDesignSystem.Spacing
                                                .extraSmall
                                    )
                                    .clip(CircleShape)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(
                                            alpha =
                                            if (isActive) 1f
                                            else 0.3f
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
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

@Composable
private fun CarListShimmer() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.medium),
        horizontalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        items(3) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier =
                Modifier
                    .width(RentalWheelsDesignSystem.Spacing.Huge * 4.375f)
                    .height(RentalWheelsDesignSystem.Spacing.Huge * 3.125f)
                    .shadow(
                        elevation = RentalWheelsDesignSystem.Elevation.small,
                        shape = MaterialTheme.shapes.medium,
                        spotColor =
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.1f
                        )
                    )
            ) {}
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(RentalWheelsDesignSystem.Spacing.Huge * 3.125f)
            .padding(horizontal = RentalWheelsDesignSystem.Spacing.medium),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.XXXL)
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

            Text(
                text = "No recommended cars available",
                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
