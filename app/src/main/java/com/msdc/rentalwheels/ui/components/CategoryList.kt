package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay

/** Premium Category List with staggered animations and glassmorphism effects */
@Composable
fun CategoryList(
    categories: List<Category>,
    modifier: Modifier = Modifier,
    onCategoryClick: (Category) -> Unit = {},
    onSeeAllClick: () -> Unit = {}
) {
    // Entrance animation for the entire section
    val sectionAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        sectionAnimation.animateTo(
            targetValue = 1f,
            animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Surface(
        modifier =
        modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .graphicsLayer {
                // Entrance animation
                alpha = sectionAnimation.value
                translationY = (1 - sectionAnimation.value) * 30
            }
            .semantics {
                contentDescription =
                    "Car categories section with ${categories.size} categories"
                heading()
            },
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = RentalWheelsDesignSystem.Elevation.small,
        shadowElevation = RentalWheelsDesignSystem.Elevation.medium
    ) {
        // Subtle gradient background for premium feel
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
                                .surface,
                            MaterialTheme.colorScheme
                                .surface.copy(
                                    alpha = 0.95f
                                )
                        )
                    )
                )
        ) {
            Column(modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.extraLarge)) {
                // Enhanced Header with better spacing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Car Categories",
                            style = RentalWheelsDesignSystem.Typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Browse by vehicle type",
                            style = RentalWheelsDesignSystem.Typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier =
                            Modifier.padding(top = RentalWheelsDesignSystem.Spacing.XXS)
                        )
                    }

                    TextButton(onClick = onSeeAllClick, modifier = Modifier.padding(0.dp)) {
                        Text(
                            text = "See All",
                            style = RentalWheelsDesignSystem.Typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(
                            modifier =
                            Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "See All Categories",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                // Categories Row with staggered animations
                LazyRow(
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium),
                    contentPadding =
                    PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
                ) {
                    itemsIndexed(items = categories, key = { _, category -> category.id }) { index,
                                                                                             category ->
                        CategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category) },
                            animationDelay = index * 50L // Staggered animation
                        )
                    }
                }
            }
        }
    }
}

/** Premium Category Card with enhanced animations and interactions */
@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    animationDelay: Long = 0L
) {
    // Staggered entrance animation
    val cardAnimation = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation on press
    val scale by
    animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "category_card_scale"
    )

    LaunchedEffect(Unit) {
        delay(animationDelay)
        cardAnimation.animateTo(
            targetValue = 1f,
            animationSpec =
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    Card(
        modifier =
        modifier
            .size(
                width = 140.dp, // Design system:
                // ~RentalWheelsDesignSystem.Spacing.Huge * 2.2
                height = 120.dp // Design system:
                // ~RentalWheelsDesignSystem.Spacing.Huge * 1.875
            )
            .scale(scale)
            .graphicsLayer {
                alpha = cardAnimation.value
                translationY = (1 - cardAnimation.value) * 40
                rotationZ = (1 - cardAnimation.value) * -5f
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClickLabel = "Select ${category.name} category"
            ) { onClick() }
            .semantics {
                contentDescription = "${category.name} category"
                role = Role.Button
            },
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small,
            pressedElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        RentalWheelsDesignSystem.CornerRadius.large
                    )
                )
                .background(
                    brush =
                    Brush.verticalGradient(
                        colors =
                        listOf(
                            MaterialTheme.colorScheme
                                .primary.copy(
                                    alpha = 0.12f
                                ),
                            MaterialTheme.colorScheme
                                .primary.copy(
                                    alpha = 0.05f
                                ),
                            MaterialTheme.colorScheme
                                .surface.copy(
                                    alpha = 0.02f
                                )
                        )
                    )
                )
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(RentalWheelsDesignSystem.Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Premium Icon Container with glassmorphism
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    tonalElevation = RentalWheelsDesignSystem.Elevation.small,
                    shadowElevation = RentalWheelsDesignSystem.Elevation.extraSmall
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.XXL)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                // Category Name with proper typography
                Text(
                    text = category.name,
                    style = RentalWheelsDesignSystem.Typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    modifier =
                    Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
                )
            }
        }
    }
}
