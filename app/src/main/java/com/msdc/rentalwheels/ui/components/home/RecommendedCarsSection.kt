package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.Typography
import kotlinx.coroutines.launch
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

    // Coroutine scope for animations
    val coroutineScope = rememberCoroutineScope()

    // Start entrance animation
    LaunchedEffect(Unit) {
        sectionAnimation.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    // LazyList state for scroll position tracking
    val lazyListState = rememberLazyListState()

    // Track if we can scroll in either direction
    val canScrollForward by remember {
        derivedStateOf { lazyListState.canScrollForward }
    }

    val canScrollBackward by remember {
        derivedStateOf { lazyListState.canScrollBackward }
    }

    // Snap fling behavior for scroll snapping
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState)

    // Surface with elevation for the entire section
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        shadowElevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer {
                // Entry animation
                alpha = sectionAnimation.value
                translationY = (1 - sectionAnimation.value) * 50
            }
            .semantics {
                contentDescription = "Recommended cars section with ${cars.size} cars"
                heading()
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            // Enhanced section header with animations
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Icon with primary color
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(8.dp)
                        )
                    }

                    // Title with primary color accent
                    Text(
                        text = "Recommended Cars",
                        style = Typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Enhanced "See All" button with improved interaction
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()

                Button(
                    onClick = onSeeAllClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .graphicsLayer {
                            val scale = if (isPressed) 0.95f else 1f
                            scaleX = scale
                            scaleY = scale
                        },
                    interactionSource = interactionSource
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "See All",
                            style = Typography.labelLarge
                        )
                        Icon(
                            imageVector = Icons.Rounded.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                // Left scroll indicator
                androidx.compose.animation.AnimatedVisibility(
                    visible = canScrollBackward,
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.Start),
                    exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.Start),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 4.dp)
                        .zIndex(10f)
                ) {
                    ScrollIndicator(isStart = true) {
                        coroutineScope.launch {
                            lazyListState.animateScrollToItem(0)
                        }
                    }
                }

                // Right scroll indicator
                androidx.compose.animation.AnimatedVisibility(
                    visible = canScrollForward,
                    enter = fadeIn() + expandHorizontally(expandFrom = Alignment.End),
                    exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .zIndex(10f)
                ) {
                    ScrollIndicator(isStart = false) {
                        coroutineScope.launch {
                            // Scroll to last visible item that's completely visible
                            val lastVisibleItem = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()
                            lastVisibleItem?.index?.let { index ->
                                lazyListState.animateScrollToItem(
                                    index.coerceAtMost(cars.size - 1)
                                )
                            }
                        }
                    }
                }

                // Gradient overlay for scroll indication at edges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Same as LazyRow height
                        .zIndex(5f)
                ) {
                    // Left fade effect
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .fillMaxHeight()
                            .alpha(if (canScrollBackward) 0.7f else 0f)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface,
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Right fade effect
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .fillMaxHeight()
                            .alpha(if (canScrollForward) 0.7f else 0f)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    )
                }

                if (isLoading) {
                    // Placeholder loading state
                    CarListShimmer()
                } else if (cars.isEmpty()) {
                    // Empty state
                    EmptyState()
                } else {
                    // Enhanced LazyRow with card animations and scroll snapping
                    LazyRow(
                        state = lazyListState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        flingBehavior = snapFlingBehavior,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Scroll to see more recommended cars"
                            }
                    ) {
                        itemsIndexed(
                            items = cars,
                            key = { _, car -> car.id }
                        ) { index, car ->
                            val density = LocalDensity.current

                            // Staggered animation for card entry
                            Box(
                                modifier = Modifier
                                    .graphicsLayer {
                                        val pageOffset = calculateCurrentItemOffset(
                                            lazyListState,
                                            index,
                                            280.dp + 16.dp
                                        )

                                        // Apply transformations with proper syntax
                                        val rotation = pageOffset * 5f
                                        val scale = 0.9f + (1f - pageOffset.absoluteValue) * 0.1f

                                        transformOrigin = TransformOrigin(0.5f, 0.5f)
                                        rotationY = rotation
                                        scaleX = scale
                                        scaleY = scale
                                        this.alpha = 0.5f + (1f - pageOffset.absoluteValue) * 0.5f
                                    }
                            ) {
                                // Enhanced Car Card with improved elevation and interaction
                                CarCard(
                                    car = car,
                                    onClick = { onCarClick(car.id) },
                                    modifier = Modifier.width(280.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Scroll position indicator dots
            if (cars.isNotEmpty() && !isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ScrollIndicatorDots(
                        itemCount = cars.size,
                        lazyListState = lazyListState,
                        itemWidthDp = 280.dp + 16.dp // Card width + spacing
                    )
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
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (isStart) Icons.Rounded.ChevronLeft else Icons.Rounded.ChevronRight,
                contentDescription = if (isStart) "Scroll to start" else "Scroll to end",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun ScrollIndicatorDots(
    itemCount: Int,
    lazyListState: LazyListState,
    itemWidthDp: Dp
) {
    val density = LocalDensity.current
    val itemWidthPx = with(density) { itemWidthDp.toPx() }

    // Calculate current position
    val scrollOffset = lazyListState.firstVisibleItemScrollOffset
    val firstVisibleItem = lazyListState.firstVisibleItemIndex

    // Calculate effective position including partial scrolling
    val position = firstVisibleItem + (scrollOffset / itemWidthPx)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until itemCount) {
            // Calculate how close we are to this dot's position
            val distanceFromCenter = (i - position).absoluteValue
            val scale = 1f - (distanceFromCenter * 0.25f).coerceIn(0f, 0.5f)
            val alpha = 0.3f + (1f - distanceFromCenter).coerceIn(0f, 0.7f)

            Box(
                modifier = Modifier
                    .size(8.dp * scale)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = alpha))
            )
        }
    }
}

@Composable
private fun CarListShimmer() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(3) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha),
                modifier = Modifier
                    .width(280.dp)
                    .height(200.dp)
                    .shadow(
                        elevation = 2.dp,
                        shape = MaterialTheme.shapes.medium,
                        spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
            ) {}
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp),
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
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "No recommended cars available",
                style = Typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Helper function to calculate current item offset for parallax effects
private fun calculateCurrentItemOffset(
    lazyListState: LazyListState,
    index: Int,
    itemWidthDp: Dp
): Float {
    val layoutInfo = lazyListState.layoutInfo
    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    val itemInfo = visibleItemsInfo.firstOrNull { it.index == index } ?: return 0f

    // Calculate the center position of the item
    val itemCenter = itemInfo.offset + (itemInfo.size / 2)
    val viewportCenter = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2

    // Convert offset to a value between -1 and 1
    return (itemCenter - viewportCenter) / itemWidthDp.value
}