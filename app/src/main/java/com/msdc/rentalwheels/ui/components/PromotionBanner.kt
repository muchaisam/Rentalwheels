package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Deal
import com.msdc.rentalwheels.ui.theme.Typography
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PromotionBanner(
    deals: List<Deal>,
    onBannerClick: (Deal) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Handle empty deals list gracefully
    if (deals.isEmpty()) {
        EmptyPromotionBanner(modifier)
        return
    }

    // Deal rotation logic
    var currentDealIndex by remember { mutableStateOf(0) }
    var previousDealIndex by remember { mutableStateOf(0) }
    var isTransitioning by remember { mutableStateOf(false) }
    val currentDeal = deals[currentDealIndex]

    // Progress indicator animation
    val progress = remember { Animatable(0f) }

    // Handle deal rotation with animation
    LaunchedEffect(currentDealIndex) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(5000, easing = LinearEasing)
        )
        if (deals.size > 1) {
            previousDealIndex = currentDealIndex
            currentDealIndex = (currentDealIndex + 1) % deals.size
            isTransitioning = true
            delay(300) // Short delay for transition effect
            isTransitioning = false
        }
    }

    // Interaction source for press effects
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Card with elevation and scaling effects
    ElevatedCard(
        onClick = { onBannerClick(currentDeal) },
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .graphicsLayer {
                // Scale slightly when pressed for interactive feedback
                val scale = if (isPressed) 0.98f else 1f
                scaleX = scale
                scaleY = scale
            },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Animated transition between deals
            AnimatedContent(
                targetState = currentDealIndex,
                transitionSpec = {
                    fadeIn(animationSpec = tween(500)) with
                            fadeOut(animationSpec = tween(500))
                },
                label = "Deal Transition"
            ) { targetIndex ->
                // Image with loading state handling
                DealImage(
                    imageUrl = deals[targetIndex].imageUrl,
                    title = deals[targetIndex].title
                )
            }

            // Gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawBehind {
                            drawRect(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.7f),
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent
                                    ),
                                    start = Offset(0f, 0f),
                                    end = Offset(size.width, size.height * 0.6f)
                                )
                            )
                        }
                    }
            )

            // Deal content with improved layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Upper section with title and description
                Column {
                    // Title with animation
                    AnimatedContent(
                        targetState = currentDeal.title,
                        transitionSpec = {
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        },
                        label = "Title Animation"
                    ) { title ->
                        Text(
                            text = title,
                            style = Typography.headlineSmall,
                            color = Color.White,
                            modifier = Modifier.semantics {
                                contentDescription = "Promotion: $title"
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description with animation
                    AnimatedContent(
                        targetState = currentDeal.description,
                        transitionSpec = {
                            slideInVertically { height -> height } + fadeIn() with
                                    slideOutVertically { height -> -height } + fadeOut()
                        },
                        label = "Description Animation"
                    ) { description ->
                        Text(
                            text = description,
                            style = Typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Bottom row with discount badge and indicators
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Animated discount badge
                    AnimatedContent(
                        targetState = currentDeal.discountPercentage,
                        transitionSpec = {
                            scaleIn() + fadeIn() with scaleOut() + fadeOut()
                        },
                        label = "Discount Animation"
                    ) { discount ->
                        Surface(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.9f),
                            shape = MaterialTheme.shapes.medium,
                            shadowElevation = 4.dp
                        ) {
                            Text(
                                text = "$discount% off",
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                                color = MaterialTheme.colorScheme.onTertiary,
                                style = Typography.labelLarge
                            )
                        }
                    }

                    // Deal progress indicators
                    if (deals.size > 1) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            // Linear progress indicator for current deal time
                            LinearProgressIndicator(
                                progress = progress.value,
                                modifier = Modifier
                                    .width(48.dp)
                                    .height(4.dp),
                                color = MaterialTheme.colorScheme.inversePrimary,
                                trackColor = MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.3f)
                            )

                            // Page indicator dots
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                deals.forEachIndexed { index, _ ->
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(MaterialTheme.shapes.small)
                                            .background(
                                                color = if (index == currentDealIndex)
                                                    MaterialTheme.colorScheme.inversePrimary
                                                else
                                                    MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.5f)
                                            )
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

@Composable
private fun DealImage(
    imageUrl: String,
    title: String
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Promotion image for $title",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        loading = {
            Box(modifier = Modifier.fillMaxSize()) {
                // Shimmer effect while loading
                ShimmerEffect()
            }
        },
        error = {
            // Fallback for error state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Promotion",
                    style = Typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}



@Composable
private fun EmptyPromotionBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No active promotions",
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview
@Composable
private fun PromotionBannerPreview() {
    val sampleDeals = listOf(
        Deal(
            id = "1",
            title = "Summer Special",
            description = "Get 20% off on weekend rentals",
            discountPercentage = 20,
            imageUrl = ""
        ),
        Deal(
            id = "2",
            title = "Holiday Offer",
            description = "Enjoy 15% discount on long-term rentals",
            discountPercentage = 15,
            imageUrl = ""
        )
    )

    PromotionBanner(deals = sampleDeals)
}