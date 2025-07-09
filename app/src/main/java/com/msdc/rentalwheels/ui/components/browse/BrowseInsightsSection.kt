package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.Typography
import com.msdc.rentalwheels.utils.CurrencyUtils
import kotlinx.coroutines.delay

data class BrowseInsight(
    val title: String,
    val value: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun BrowseInsightsSection(cars: List<Car>, modifier: Modifier = Modifier) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(cars) {
        delay(300) // Small delay for better animation
        isVisible = true
    }

    if (cars.isNotEmpty()) {
        val insights = generateInsights(cars)

        AnimatedVisibility(
            visible = isVisible,
            enter =
            slideInVertically(
                initialOffsetY = { it },
                animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(animationSpec = tween(600)),
            modifier = modifier
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors =
                CardDefaults.cardColors(
                    containerColor =
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.3f
                    )
                ),
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
                                    MaterialTheme
                                        .colorScheme
                                        .primary
                                        .copy(
                                            alpha =
                                            0.1f
                                        ),
                                    Color.Transparent
                                )
                            )
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = "Insights",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Browse Insights",
                                style = Typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            itemsIndexed(insights) { index, insight ->
                                AnimatedVisibility(
                                    visible = isVisible,
                                    enter =
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec =
                                        spring(
                                            dampingRatio =
                                            Spring.DampingRatioMediumBouncy,
                                            stiffness =
                                            Spring.StiffnessLow,
                                        )
                                    ) +
                                            fadeIn(
                                                animationSpec =
                                                tween(
                                                    durationMillis =
                                                    600,
                                                    delayMillis =
                                                    index * 100
                                                )
                                            )
                                ) { InsightCard(insight = insight) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightCard(insight: BrowseInsight, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.width(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = insight.icon,
                contentDescription = insight.title,
                tint = insight.color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = insight.value,
                style = Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = insight.title,
                style = Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = insight.description,
                style = Typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

private fun generateInsights(cars: List<Car>): List<BrowseInsight> {
    val insights = mutableListOf<BrowseInsight>()

    if (cars.isNotEmpty()) {
        // Average price insight
        val avgPrice = cars.map { it.dailyRate }.average().toInt()
        insights.add(
            BrowseInsight(
                title = "Avg Price",
                value = CurrencyUtils.formatToKSH(avgPrice),
                description = "per day",
                icon = Icons.Default.TrendingUp,
                color = Color(0xFF4CAF50)
            )
        )

        // Electric cars percentage
        val electricCount = cars.count { it.fuelType.equals("Electric", ignoreCase = true) }
        val electricPercentage = if (cars.isNotEmpty()) (electricCount * 100) / cars.size else 0
        insights.add(
            BrowseInsight(
                title = "Electric",
                value = "$electricPercentage%",
                description = "eco-friendly",
                icon = Icons.Default.ElectricCar,
                color = Color(0xFF2196F3)
            )
        )

        // Most popular fuel type
        val fuelTypeGroups = cars.groupBy { it.fuelType }
        val mostPopularFuel = fuelTypeGroups.maxByOrNull { it.value.size }?.key ?: "Mixed"
        insights.add(
            BrowseInsight(
                title = "Popular",
                value = mostPopularFuel,
                description = "fuel type",
                icon = Icons.Default.LocalGasStation,
                color = Color(0xFFFF9800)
            )
        )

        // Newest cars
        val newestYear = cars.maxOfOrNull { it.year } ?: 2024
        val newestCount = cars.count { it.year >= newestYear - 1 }
        insights.add(
            BrowseInsight(
                title = "Latest",
                value = "$newestCount",
                description = "recent models",
                icon = Icons.Default.Speed,
                color = Color(0xFF9C27B0)
            )
        )

        // Recommended cars
        val recommendedCount = cars.count { it.recommended }
        if (recommendedCount > 0) {
            insights.add(
                BrowseInsight(
                    title = "Top Rated",
                    value = "$recommendedCount",
                    description = "recommended",
                    icon = Icons.Default.Star,
                    color = Color(0xFFFFC107)
                )
            )
        }
    }

    return insights
}
