package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun CarSpecs(car: Car, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        colors =
        CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SpecItem(icon = Icons.Rounded.Speed, label = "Transmission", value = car.transmission)
            VerticalDivider()
            SpecItem(
                icon = Icons.Rounded.LocalGasStation,
                label = "Fuel Type",
                value = car.fuelType
            )
            VerticalDivider()
        }
    }
}

@Composable
private fun SpecItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val scale by
    animateFloatAsState(
        targetValue = if (isPressed.value) 0.95f else 1f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "specItemScale"
    )

    Column(
        modifier =
        modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.XXS))
        Text(
            text = label,
            style = RentalWheelsDesignSystem.Typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(text = value, style = RentalWheelsDesignSystem.Typography.titleMedium)
    }
}

@Composable
fun CarDescription(description: String, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "About this car",
            style = RentalWheelsDesignSystem.Typography.titleLarge,
            modifier = Modifier.padding(bottom = RentalWheelsDesignSystem.Spacing.small)
        )

        Text(
            text = description,
            style = RentalWheelsDesignSystem.Typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable { expanded = !expanded }
        )

        if (!expanded && description.length > 150) {
            TextButton(onClick = { expanded = true }) { Text("Read more") }
        }
    }
}
