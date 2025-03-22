package com.msdc.rentalwheels.appnavigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.ThemeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    themeState: ThemeState
) {
    val themeMode by themeState.themeMode

    // Custom selected color FF6600
    val selectedColor = Color(0xFFFF6600)
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)

    var previousIndex by remember { mutableStateOf(0) }
    val currentIndex = remember(currentDestination) {
        when (currentDestination?.route) {
            Screen.Home.route -> 0
            Screen.Browse.route -> 1
            Screen.Bookings.route -> 2
            Screen.Settings.route -> 3
            else -> 0 // Default to Home tab
        }
    }

    // Ensure Home is selected by default if no route is specified
    LaunchedEffect(currentDestination) {
        if (currentDestination?.route == null) {
            onNavigate(Screen.Home)
        }
    }

    val direction = if (currentIndex > previousIndex)
        AnimatedContentTransitionScope.SlideDirection.Left
    else AnimatedContentTransitionScope.SlideDirection.Right

    LaunchedEffect(currentIndex) {
        previousIndex = currentIndex
    }

    val navigationItems = listOf(
        NavigationItem(
            icon = R.drawable.car_home,
            label = "Home",
            route = Screen.Home.route,
            contentDescription = "Navigate to Home screen"
        ),
        NavigationItem(
            icon = R.drawable.maps,
            label = "Browse",
            route = Screen.Browse.route,
            contentDescription = "Navigate to Browse screen"
        ),
        NavigationItem(
            icon = R.drawable.bookings,
            label = "Bookings",
            route = Screen.Bookings.route,
            contentDescription = "Navigate to Bookings screen"
        ),
        NavigationItem(
            icon = R.drawable.settings,
            label = "Settings",
            route = Screen.Settings.route,
            contentDescription = "Navigate to Settings screen"
        )
    )

    val dividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        shape = MaterialTheme.shapes.extraLarge.copy(
            bottomStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .drawBehind {
                drawLine(
                    color = dividerColor,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .semantics {
                role = Role.Tab
                contentDescription = "Bottom navigation with ${navigationItems.size} tabs"
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationItems.forEachIndexed { index, item ->
                // FIX 1: Check the current route directly against the item's route
                val selected = currentDestination?.route == item.route ||
                        (currentDestination?.route == null && index == 0) // Select Home by default
                val haptic = LocalHapticFeedback.current

                EnhancedNavigationItem(
                    icon = item.icon,
                    label = item.label,
                    selected = selected,
                    contentDesc = item.contentDescription,
                    selectedIconTint = selectedColor,
                    unselectedIconTint = unselectedColor,
                    onSelected = {
                        // FIX 2: Always allow navigation, even to the current tab
                        // This fixes the issue where you couldn't return to Home
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigate(
                            when (index) {
                                0 -> Screen.Home
                                1 -> Screen.Browse
                                2 -> Screen.Bookings
                                3 -> Screen.Settings
                                else -> Screen.Home
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EnhancedNavigationItem(
    icon: Int,
    label: String,
    selected: Boolean,
    contentDesc: String,
    selectedIconTint: Color,
    unselectedIconTint: Color,
    onSelected: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val animatedIconScale by animateFloatAsState(
        targetValue = if (selected) 1.2f else 1f,
        animationSpec = tween(300),
        label = "IconScale"
    )
    val animatedTextSize by animateFloatAsState(
        targetValue = if (selected) 14f else 12f,
        animationSpec = tween(300),
        label = "TextSize"
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (selected) 4.dp else 0.dp,
        animationSpec = tween(300),
        label = "Elevation"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .fillMaxHeight()
            .clip(MaterialTheme.shapes.medium)
            .selectable(
                selected = selected,
                onClick = onSelected,
                interactionSource = interactionSource,
                indication = LocalIndication.current
            )
            .semantics {
                this.contentDescription = if (selected) {
                    "Current tab: $contentDesc"
                } else {
                    contentDesc
                }
                this.selected = selected
                this.role = Role.Tab
            }
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        // Indicator light for selected state
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(width = 16.dp, height = 2.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(selectedIconTint)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Icon with animated scale and color
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(24.dp)
                .graphicsLayer {
                    scaleX = animatedIconScale * if (isPressed) 0.9f else 1f
                    scaleY = animatedIconScale * if (isPressed) 0.9f else 1f
                    translationY = if (selected) -2f else 0f
                }
        ) {
            // Create a crossfade between selected and unselected states
            Crossfade(
                targetState = selected,
                animationSpec = tween(300),
                label = "Icon Color Animation"
            ) { isSelected ->
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = if (isSelected) selectedIconTint else unselectedIconTint,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Label with animated weight and size
        Text(
            text = label,
            color = if (selected) selectedIconTint else unselectedIconTint,
            fontSize = animatedTextSize.sp,
            fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            maxLines = 1
        )

        // Animated indicator for selected state (bottom)
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(32.dp)
                    .height(3.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(selectedIconTint.copy(alpha = 0.7f))
                    .shadow(
                        elevation = animatedElevation,
                        shape = MaterialTheme.shapes.small,
                        spotColor = selectedIconTint
                    )
            )
        }
    }
}

// Navigation item data class for cleaner implementation
private data class NavigationItem(
    val icon: Int,
    val label: String,
    val route: String,
    val contentDescription: String
)