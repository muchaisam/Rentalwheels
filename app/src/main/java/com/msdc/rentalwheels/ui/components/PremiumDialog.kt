package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

/** Premium Dialog Types for different use cases */
enum class DialogType(val icon: ImageVector, val iconColor: @Composable () -> Color) {
    Success(icon = Icons.Default.CheckCircle, iconColor = { Color(0xFF10B981) }),
    Error(icon = Icons.Default.Error, iconColor = { Color(0xFFEF4444) }),
    Warning(icon = Icons.Default.Warning, iconColor = { Color(0xFFF59E0B) }),
    Info(icon = Icons.Default.Info, iconColor = { MaterialTheme.colorScheme.primary }),
    Confirmation(icon = Icons.Default.Info, iconColor = { MaterialTheme.colorScheme.primary })
}

/**
 * Premium Dialog with Glassmorphism Design Features professional styling, smooth animations, and
 * haptic feedback
 */
@Composable
fun PremiumDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    type: DialogType = DialogType.Info,
    confirmText: String = "OK",
    dismissText: String? = null,
    onConfirm: (() -> Unit)? = null,
    onDismissAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    AnimatedVisibility(
        visible = visible,
        enter =
        fadeIn(animationSpec = tween(300)) +
                scaleIn(
                    initialScale = 0.8f,
                    animationSpec =
                    spring(
                        dampingRatio =
                        Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
        exit =
        fadeOut(animationSpec = tween(200)) +
                scaleOut(targetScale = 0.8f, animationSpec = tween(200))
    ) {
        Dialog(
            onDismissRequest = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onDismiss()
            },
            properties =
            DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Glassmorphism Background Overlay
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors =
                                listOf(
                                    Color.Black
                                        .copy(
                                            alpha =
                                            0.4f
                                        ),
                                    Color.Black
                                        .copy(
                                            alpha =
                                            0.6f
                                        )
                                ),
                                radius = 800f
                            )
                        )
                        .blur(
                            RentalWheelsDesignSystem.Elevation
                                .medium
                        )
                        .clickable(
                            interactionSource =
                            remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            haptic.performHapticFeedback(
                                HapticFeedbackType.LongPress
                            )
                            onDismiss()
                        }
                )

                // Dialog Content Card
                Card(
                    modifier =
                    modifier
                        .widthIn(
                            min =
                            RentalWheelsDesignSystem
                                .Spacing.Huge *
                                    4.375f,
                            max =
                            RentalWheelsDesignSystem
                                .Spacing.Huge *
                                    6.25f
                        )
                        .padding(
                            RentalWheelsDesignSystem.Spacing
                                .large
                        )
                        .zIndex(1f),
                    shape =
                    RoundedCornerShape(
                        RentalWheelsDesignSystem.CornerRadius.large
                    ),
                    colors =
                    CardDefaults.cardColors(
                        containerColor =
                        MaterialTheme.colorScheme.surface
                            .copy(alpha = 0.95f)
                    ),
                    elevation =
                    CardDefaults.cardElevation(
                        defaultElevation =
                        RentalWheelsDesignSystem.Elevation
                            .large
                    )
                ) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                RentalWheelsDesignSystem
                                    .Spacing.large
                            )
                            .verticalScroll(
                                rememberScrollState()
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Close Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType
                                            .LongPress
                                    )
                                    onDismiss()
                                }
                            ) {
                                Icon(
                                    imageVector =
                                    Icons.Default.Close,
                                    contentDescription =
                                    "Close dialog",
                                    tint =
                                    MaterialTheme
                                        .colorScheme
                                        .onSurfaceVariant
                                )
                            }
                        }

                        Spacer(
                            modifier =
                            Modifier.height(
                                RentalWheelsDesignSystem
                                    .Spacing.small
                            )
                        )

                        // Icon
                        Surface(
                            modifier =
                            Modifier.size(
                                RentalWheelsDesignSystem
                                    .Spacing.Huge
                            ),
                            shape =
                            RoundedCornerShape(
                                RentalWheelsDesignSystem
                                    .CornerRadius.large
                            ),
                            color = type.iconColor().copy(alpha = 0.1f)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = type.icon,
                                    contentDescription = null,
                                    modifier =
                                    Modifier.size(
                                        RentalWheelsDesignSystem
                                            .Spacing
                                            .XXL
                                    ),
                                    tint = type.iconColor()
                                )
                            }
                        }

                        Spacer(
                            modifier =
                            Modifier.height(
                                RentalWheelsDesignSystem
                                    .Spacing.large
                            )
                        )

                        // Title
                        Text(
                            text = title,
                            style =
                            RentalWheelsDesignSystem.Typography
                                .headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(
                            modifier =
                            Modifier.height(
                                RentalWheelsDesignSystem
                                    .Spacing.medium
                            )
                        )

                        // Message
                        Text(
                            text = message,
                            style =
                            RentalWheelsDesignSystem.Typography
                                .bodyMedium,
                            color =
                            MaterialTheme.colorScheme
                                .onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight =
                            RentalWheelsDesignSystem.Typography
                                .bodyMedium
                                .lineHeight
                        )

                        Spacer(
                            modifier =
                            Modifier.height(
                                RentalWheelsDesignSystem
                                    .Spacing.extraLarge
                            )
                        )

                        // Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            if (dismissText != null) {
                                Arrangement.spacedBy(
                                    RentalWheelsDesignSystem
                                        .Spacing
                                        .medium
                                )
                            } else {
                                Arrangement.Center
                            }
                        ) {
                            // Dismiss Button (optional)
                            dismissText?.let {
                                PremiumButton(
                                    onClick = {
                                        haptic.performHapticFeedback(
                                            HapticFeedbackType
                                                .LongPress
                                        )
                                        onDismissAction
                                            ?.invoke()
                                            ?: onDismiss()
                                    },
                                    variant =
                                    PremiumButtonVariant
                                        .Secondary,
                                    modifier =
                                    if (dismissText !=
                                        null
                                    )
                                        Modifier.weight(
                                            1f
                                        )
                                    else Modifier
                                ) { Text(it) }
                            }

                            // Confirm Button
                            PremiumButton(
                                onClick = {
                                    haptic.performHapticFeedback(
                                        HapticFeedbackType
                                            .LongPress
                                    )
                                    onConfirm?.invoke()
                                        ?: onDismiss()
                                },
                                variant =
                                when (type) {
                                    DialogType.Error ->
                                        PremiumButtonVariant
                                            .Primary // Keep primary for errors
                                    // too
                                    DialogType
                                        .Success ->
                                        PremiumButtonVariant
                                            .Primary

                                    DialogType
                                        .Warning ->
                                        PremiumButtonVariant
                                            .Primary

                                    else ->
                                        PremiumButtonVariant
                                            .Primary
                                },
                                modifier =
                                if (dismissText != null)
                                    Modifier.weight(1f)
                                else
                                    Modifier.widthIn(
                                        min =
                                        RentalWheelsDesignSystem
                                            .Spacing
                                            .Huge *
                                                1.875f
                                    )
                            ) { Text(confirmText) }
                        }
                    }
                }
            }
        }
    }
}

/** Simplified Success Dialog */
@Composable
fun SuccessDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Great!"
) {
    PremiumDialog(
        visible = visible,
        onDismiss = onDismiss,
        title = title,
        message = message,
        type = DialogType.Success,
        confirmText = confirmText
    )
}

/** Simplified Error Dialog */
@Composable
fun ErrorDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String = "Error",
    message: String,
    confirmText: String = "OK"
) {
    PremiumDialog(
        visible = visible,
        onDismiss = onDismiss,
        title = title,
        message = message,
        type = DialogType.Error,
        confirmText = confirmText
    )
}

/** Confirmation Dialog with Two Actions */
@Composable
fun ConfirmationDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit
) {
    PremiumDialog(
        visible = visible,
        onDismiss = onDismiss,
        title = title,
        message = message,
        type = DialogType.Confirmation,
        confirmText = confirmText,
        dismissText = dismissText,
        onConfirm = onConfirm
    )
}
