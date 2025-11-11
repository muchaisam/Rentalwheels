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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AirportShuttle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Category
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay

/** Helper to get category-specific icon based on category name */
private fun getCategoryIcon(categoryName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when {
        categoryName.contains("SUV", ignoreCase = true) ||
                categoryName.contains("Truck", ignoreCase = true) -> Icons.Default.LocalShipping
        categoryName.contains("Sedan", ignoreCase = true) ||
                categoryName.contains("Compact", ignoreCase = true) -> Icons.Default.DirectionsCar
        categoryName.contains("Electric", ignoreCase = true) ||
                categoryName.contains("Hybrid", ignoreCase = true) -> Icons.Default.ElectricCar
        categoryName.contains("Van", ignoreCase = true) ||
                categoryName.contains("Minivan", ignoreCase = true) -> Icons.Default.AirportShuttle
        categoryName.contains("Bike", ignoreCase = true) ||
                categoryName.contains("Motorcycle", ignoreCase = true) -> Icons.Default.TwoWheeler
        else -> Icons.Default.DirectionsCar
    }
}

/** Helper to get category-specific gradient colors */
private fun getCategoryGradientColors(
        categoryName: String,
        colorScheme: androidx.compose.material3.ColorScheme
): Pair<androidx.compose.ui.graphics.Color, androidx.compose.ui.graphics.Color> {
    return when {
        categoryName.contains("SUV", ignoreCase = true) ||
                categoryName.contains("Truck", ignoreCase = true) ->
                Pair(
                        RentalWheelsDesignSystem.Colors.BrandSecondary.copy(alpha = 0.12f),
                        RentalWheelsDesignSystem.Colors.BrandSecondaryLight.copy(alpha = 0.08f)
                )
        categoryName.contains("Electric", ignoreCase = true) ||
                categoryName.contains("Hybrid", ignoreCase = true) ->
                Pair(
                        RentalWheelsDesignSystem.Colors.BrandInfo.copy(alpha = 0.12f),
                        RentalWheelsDesignSystem.Colors.InfoLight.copy(alpha = 0.08f)
                )
        categoryName.contains("Luxury", ignoreCase = true) ||
                categoryName.contains("Premium", ignoreCase = true) ->
                Pair(
                        RentalWheelsDesignSystem.Colors.BrandWarning.copy(alpha = 0.12f),
                        RentalWheelsDesignSystem.Colors.WarningLight.copy(alpha = 0.08f)
                )
        else ->
                Pair(
                        colorScheme.primary.copy(alpha = 0.08f),
                        colorScheme.primaryContainer.copy(alpha = 0.12f)
                )
    }
}

/** Premium Category List with staggered animations and glassmorphism effects */
@Composable
fun CategoryList(
        categories: List<Category>,
        modifier: Modifier = Modifier,
        onCategoryClick: (Category) -> Unit = {},
        onSeeAllClick: () -> Unit = {},
        carCountsPerCategory: Map<String, Int> = emptyMap() // New parameter for vehicle counts
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
                    modifier.fillMaxWidth()
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
            tonalElevation = RentalWheelsDesignSystem.Elevation.extraSmall
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
                            modifier = Modifier.padding(top = RentalWheelsDesignSystem.Spacing.XXS)
                    )
                }

                TextButton(onClick = onSeeAllClick, modifier = Modifier.padding(0.dp)) {
                    Text(
                            text = "See All",
                            style = RentalWheelsDesignSystem.Typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall))
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
                itemsIndexed(items = categories, key = { _, category -> category.id }) {
                        index,
                        category ->
                    CategoryCard(
                            category = category,
                            onClick = { onCategoryClick(category) },
                            animationDelay = index * 50L, // Staggered animation
                            vehicleCount = carCountsPerCategory[category.name] ?: 0
                    )
                }
            }
        }
    }
}

/** Premium Glassmorphic Category Card with frosted glass effect and advanced interactions */
@Composable
private fun CategoryCard(
        category: Category,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        animationDelay: Long = 0L,
        vehicleCount: Int = 0
) {
    // Staggered entrance animation
    val cardAnimation = remember { Animatable(0f) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Shimmer idle animation
    val shimmerAnimation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Entrance animation first
        delay(animationDelay)
        cardAnimation.animateTo(
                targetValue = 1f,
                animationSpec =
                        spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                        )
        )

        // Then start continuous shimmer animation
        delay(500) // Wait a bit before starting shimmer
        while (true) {
            shimmerAnimation.animateTo(
                    targetValue = 1f,
                    animationSpec = androidx.compose.animation.core.tween(2000)
            )
            shimmerAnimation.animateTo(
                    targetValue = 0f,
                    animationSpec = androidx.compose.animation.core.tween(2000)
            )
        }
    }

    // Enhanced scale animation on press with stronger effect
    val scale by
            animateFloatAsState(
                    targetValue = if (isPressed) 0.88f else 1f,
                    animationSpec =
                            spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                            ),
                    label = "category_card_scale"
            )

    // Glow effect animation on press
    val glowAlpha by
            animateFloatAsState(
                    targetValue = if (isPressed) 0.6f else 0.3f,
                    animationSpec =
                            spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                            ),
                    label = "category_card_glow"
            )

    // Get category-specific styling
    val categoryIcon = remember(category.name) { getCategoryIcon(category.name) }
    val gradientColors = getCategoryGradientColors(category.name, MaterialTheme.colorScheme)

    // Glassmorphic container with layered effect
    Box(
            modifier =
                    modifier.size(width = 140.dp, height = 120.dp)
                            .scale(scale)
                            .graphicsLayer {
                                alpha = cardAnimation.value
                                translationY = (1 - cardAnimation.value) * 50
                                rotationZ = (1 - cardAnimation.value) * -8f
                            }
                            .semantics {
                                contentDescription =
                                        "${category.name} category, $vehicleCount vehicles available"
                                role = Role.Button
                            }
    ) {
        // Outer glow layer (simulated backdrop blur effect)
        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .padding(4.dp)
                                .background(
                                        brush =
                                                androidx.compose.ui.graphics.Brush.radialGradient(
                                                        colors =
                                                                listOf(
                                                                        MaterialTheme.colorScheme
                                                                                .primary.copy(
                                                                                alpha = glowAlpha
                                                                        ),
                                                                        MaterialTheme.colorScheme
                                                                                .primary.copy(
                                                                                alpha = 0f
                                                                        )
                                                                ),
                                                        radius = 200f
                                                ),
                                        shape =
                                                RoundedCornerShape(
                                                        RentalWheelsDesignSystem.CornerRadius
                                                                .extraLarge
                                                )
                                )
        )

        // Main glassmorphic card
        Card(
                modifier =
                        Modifier.fillMaxSize().clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        role = Role.Button,
                                        onClickLabel = "Select ${category.name} category"
                                ) { onClick() },
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.surfaceContainer.copy(
                                                alpha = 0.7f
                                        )
                        ),
                elevation =
                        CardDefaults.cardElevation(
                                defaultElevation = RentalWheelsDesignSystem.Elevation.large,
                                pressedElevation = RentalWheelsDesignSystem.Elevation.extraLarge,
                                hoveredElevation = RentalWheelsDesignSystem.Elevation.extraLarge
                        )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Gradient overlay for glassmorphic effect (category-specific colors)
                Box(
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(
                                                brush =
                                                        androidx.compose.ui.graphics.Brush
                                                                .verticalGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        gradientColors
                                                                                                .first,
                                                                                        gradientColors
                                                                                                .second
                                                                                )
                                                                )
                                        )
                )

                // Shimmer effect overlay
                Box(
                        modifier =
                                Modifier.fillMaxSize()
                                        .background(
                                                brush =
                                                        androidx.compose.ui.graphics.Brush
                                                                .linearGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Color
                                                                                                .Transparent,
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Color
                                                                                                .White
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0.1f *
                                                                                                                        shimmerAnimation
                                                                                                                                .value
                                                                                                ),
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Color
                                                                                                .Transparent
                                                                                ),
                                                                        start =
                                                                                androidx.compose.ui
                                                                                        .geometry
                                                                                        .Offset(
                                                                                                -200f +
                                                                                                        (shimmerAnimation
                                                                                                                .value *
                                                                                                                400f),
                                                                                                -200f +
                                                                                                        (shimmerAnimation
                                                                                                                .value *
                                                                                                                400f)
                                                                                        ),
                                                                        end =
                                                                                androidx.compose.ui
                                                                                        .geometry
                                                                                        .Offset(
                                                                                                200f +
                                                                                                        (shimmerAnimation
                                                                                                                .value *
                                                                                                                400f),
                                                                                                200f +
                                                                                                        (shimmerAnimation
                                                                                                                .value *
                                                                                                                400f)
                                                                                        )
                                                                )
                                        )
                )

                // Border gradient effect
                Box(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(1.dp)
                                        .background(
                                                brush =
                                                        androidx.compose.ui.graphics.Brush
                                                                .linearGradient(
                                                                        colors =
                                                                                listOf(
                                                                                        MaterialTheme
                                                                                                .colorScheme
                                                                                                .primary
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0.3f
                                                                                                ),
                                                                                        androidx.compose
                                                                                                .ui
                                                                                                .graphics
                                                                                                .Color
                                                                                                .Transparent,
                                                                                        MaterialTheme
                                                                                                .colorScheme
                                                                                                .primary
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0.2f
                                                                                                )
                                                                                )
                                                                ),
                                                shape =
                                                        RoundedCornerShape(
                                                                RentalWheelsDesignSystem
                                                                        .CornerRadius.extraLarge
                                                        )
                                        )
                )

                // Count badge in top-right corner
                if (vehicleCount > 0) {
                    BadgedBox(
                            badge = {
                                Badge(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(
                                            text = "$vehicleCount",
                                            style = RentalWheelsDesignSystem.Typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                    )
                                }
                            },
                            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    ) {}
                }

                // Content
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(RentalWheelsDesignSystem.Spacing.medium),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                ) {
                    // Floating icon with glassmorphic container
                    Box(modifier = Modifier.size(56.dp), contentAlignment = Alignment.Center) {
                        // Icon glow background
                        Surface(
                                modifier = Modifier.size(52.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                tonalElevation = RentalWheelsDesignSystem.Elevation.small
                        ) {}

                        // Icon container
                        Surface(
                                modifier = Modifier.size(48.dp),
                                shape = CircleShape,
                                color =
                                        MaterialTheme.colorScheme.primaryContainer.copy(
                                                alpha = 0.9f
                                        ),
                                tonalElevation = RentalWheelsDesignSystem.Elevation.medium
                        ) {
                            Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                        imageVector = categoryIcon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                    // Category Name with enhanced typography
                    Text(
                            text = category.name,
                            style = RentalWheelsDesignSystem.Typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}
