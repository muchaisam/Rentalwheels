package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CarFeatures(features: List<String>, modifier: Modifier = Modifier) {
    // Entrance animation
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = RentalWheelsDesignSystem.Animation.Medium)
        )
    }

    Column(
        modifier =
        modifier.graphicsLayer {
            this.alpha = alpha.value
            translationY = (1f - alpha.value) * 30f
        }
    ) {
        Text(
            text = "Features",
            style = RentalWheelsDesignSystem.Typography.titleLarge,
            modifier = Modifier.padding(bottom = RentalWheelsDesignSystem.Spacing.small)
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small),
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
        ) {
            features.forEachIndexed { index, feature ->
                FeatureChip(feature = feature, delay = index * 50L)
            }
        }
    }
}

@Composable
private fun FeatureChip(feature: String, delay: Long = 0L) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val scale by
    animateFloatAsState(
        targetValue = if (isPressed.value) 0.92f else 1f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "featureChipScale"
    )

    AssistChip(
        onClick = {},
        label = {
            Text(text = feature, style = RentalWheelsDesignSystem.Typography.bodyMedium)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
            )
        },
        modifier =
        Modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        interactionSource = interactionSource
    )
}
