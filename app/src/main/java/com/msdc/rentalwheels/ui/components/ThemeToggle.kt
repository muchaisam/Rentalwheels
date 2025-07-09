package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.theme.ThemeMode
import com.msdc.rentalwheels.ui.theme.ThemeState

@Composable
fun ThemeToggle(themeState: ThemeState, modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current
    val themeMode = themeState.themeMode.collectAsState()
    val isDarkTheme = themeMode.value == ThemeMode.Dark

    // Animated colors and properties
    val toggleColor by
    animateColorAsState(
        targetValue =
        if (isDarkTheme) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.tertiary
        },
        animationSpec = tween(300),
        label = "ToggleColor"
    )

    val backgroundColor by
    animateColorAsState(
        targetValue = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        animationSpec = tween(300),
        label = "BackgroundColor"
    )

    val iconScale by
    animateFloatAsState(
        targetValue = if (isDarkTheme) 1.1f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "IconScale"
    )

    Row(
        modifier =
        modifier
            .background(backgroundColor, RoundedCornerShape(24.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                RoundedCornerShape(24.dp)
            )
            .padding(4.dp)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                themeState.toggleTheme()
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Light mode icon
        Box(
            modifier =
            Modifier
                .size(40.dp)
                .background(
                    if (!isDarkTheme) toggleColor else Color.Transparent,
                    CircleShape
                )
                .scale(if (!isDarkTheme) iconScale else 1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Light mode",
                tint =
                if (!isDarkTheme) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(20.dp)
            )
        }

        // Dark mode icon
        Box(
            modifier =
            Modifier
                .size(40.dp)
                .background(
                    if (isDarkTheme) toggleColor else Color.Transparent,
                    CircleShape
                )
                .scale(if (isDarkTheme) iconScale else 1f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.DarkMode,
                contentDescription = "Dark mode",
                tint =
                if (isDarkTheme) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
