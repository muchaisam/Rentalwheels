package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

enum class PremiumButtonSize {
    Small,
    Medium,
    Large
}

enum class PremiumButtonVariant {
    Primary, // Filled button with primary color
    Secondary, // Outlined button
    Tertiary, // Text button
    Tonal // Filled tonal button
}

/** Professional Button System Consistent styling with micro-animations */
@Composable
fun PremiumButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    variant: PremiumButtonVariant = PremiumButtonVariant.Primary,
    size: PremiumButtonSize = PremiumButtonSize.Medium,
    leadingIcon: ImageVector? = null,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by
    animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(RentalWheelsDesignSystem.Animation.Fast),
        label = "button_scale"
    )

    val (height, paddingValues) =
        when (size) {
            PremiumButtonSize.Small ->
                Pair(
                    RentalWheelsDesignSystem.Spacing.XXL,
                    PaddingValues(
                        horizontal = RentalWheelsDesignSystem.Spacing.small,
                        vertical = RentalWheelsDesignSystem.Spacing.XXS
                    )
                )

            PremiumButtonSize.Medium ->
                Pair(
                    RentalWheelsDesignSystem.Spacing.XXXL,
                    PaddingValues(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium,
                        vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                    )
                )

            PremiumButtonSize.Large ->
                Pair(
                    RentalWheelsDesignSystem.Spacing.XXXXL,
                    PaddingValues(
                        horizontal = RentalWheelsDesignSystem.Spacing.extraLarge,
                        vertical = RentalWheelsDesignSystem.Spacing.small
                    )
                )
        }

    when (variant) {
        PremiumButtonVariant.Primary -> {
            Button(
                onClick = onClick,
                modifier = modifier
                    .height(height)
                    .scale(scale),
                enabled = enabled,
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Button),
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor =
                    MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = paddingValues,
                interactionSource = interactionSource,
                elevation =
                ButtonDefaults.buttonElevation(
                    defaultElevation = RentalWheelsDesignSystem.Elevation.Button,
                    pressedElevation =
                    RentalWheelsDesignSystem.Elevation.extraSmall,
                    disabledElevation = RentalWheelsDesignSystem.Elevation.None
                )
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                        Modifier.size(
                            when (size) {
                                PremiumButtonSize.Small ->
                                    RentalWheelsDesignSystem.Spacing.medium

                                PremiumButtonSize.Medium ->
                                    RentalWheelsDesignSystem.Spacing.medium *
                                            1.125f

                                PremiumButtonSize.Large ->
                                    RentalWheelsDesignSystem.Spacing.large
                            }
                        )
                    )
                }
                content()
            }
        }

        PremiumButtonVariant.Secondary -> {
            OutlinedButton(
                onClick = onClick,
                modifier = modifier
                    .height(height)
                    .scale(scale),
                enabled = enabled,
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Button),
                colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border =
                BorderStroke(
                    width = RentalWheelsDesignSystem.Elevation.extraSmall,
                    color =
                    if (enabled) MaterialTheme.colorScheme.outline
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                contentPadding = paddingValues,
                interactionSource = interactionSource
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                        Modifier.size(
                            when (size) {
                                PremiumButtonSize.Small ->
                                    RentalWheelsDesignSystem.Spacing.medium

                                PremiumButtonSize.Medium ->
                                    RentalWheelsDesignSystem.Spacing.medium *
                                            1.125f

                                PremiumButtonSize.Large ->
                                    RentalWheelsDesignSystem.Spacing.large
                            }
                        )
                    )
                }
                content()
            }
        }

        PremiumButtonVariant.Tertiary -> {
            TextButton(
                onClick = onClick,
                modifier = modifier
                    .height(height)
                    .scale(scale),
                enabled = enabled,
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Button),
                colors =
                ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = paddingValues,
                interactionSource = interactionSource
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                        Modifier.size(
                            when (size) {
                                PremiumButtonSize.Small ->
                                    RentalWheelsDesignSystem.Spacing.medium

                                PremiumButtonSize.Medium ->
                                    RentalWheelsDesignSystem.Spacing.medium *
                                            1.125f

                                PremiumButtonSize.Large ->
                                    RentalWheelsDesignSystem.Spacing.large
                            }
                        )
                    )
                }
                content()
            }
        }

        PremiumButtonVariant.Tonal -> {
            FilledTonalButton(
                onClick = onClick,
                modifier = modifier
                    .height(height)
                    .scale(scale),
                enabled = enabled,
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.Button),
                colors =
                ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor =
                    MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor =
                    MaterialTheme.colorScheme.onSurfaceVariant
                ),
                contentPadding = paddingValues,
                interactionSource = interactionSource
            ) {
                leadingIcon?.let { icon ->
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier =
                        Modifier.size(
                            when (size) {
                                PremiumButtonSize.Small ->
                                    RentalWheelsDesignSystem.Spacing.medium

                                PremiumButtonSize.Medium ->
                                    RentalWheelsDesignSystem.Spacing.medium *
                                            1.125f

                                PremiumButtonSize.Large ->
                                    RentalWheelsDesignSystem.Spacing.large
                            }
                        )
                    )
                }
                content()
            }
        }
    }
}

/** Convenience composables for common button types */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: PremiumButtonSize = PremiumButtonSize.Medium,
    leadingIcon: ImageVector? = null
) {
    PremiumButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = PremiumButtonVariant.Primary,
        size = size,
        leadingIcon = leadingIcon
    ) {
        Text(
            text = text,
            style =
            when (size) {
                PremiumButtonSize.Small -> MaterialTheme.typography.labelSmall
                PremiumButtonSize.Medium -> MaterialTheme.typography.labelMedium
                PremiumButtonSize.Large -> MaterialTheme.typography.labelLarge
            }.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: PremiumButtonSize = PremiumButtonSize.Medium,
    leadingIcon: ImageVector? = null
) {
    PremiumButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = PremiumButtonVariant.Secondary,
        size = size,
        leadingIcon = leadingIcon
    ) {
        Text(
            text = text,
            style =
            when (size) {
                PremiumButtonSize.Small -> MaterialTheme.typography.labelSmall
                PremiumButtonSize.Medium -> MaterialTheme.typography.labelMedium
                PremiumButtonSize.Large -> MaterialTheme.typography.labelLarge
            }.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun TonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: PremiumButtonSize = PremiumButtonSize.Medium,
    leadingIcon: ImageVector? = null
) {
    PremiumButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        variant = PremiumButtonVariant.Tonal,
        size = size,
        leadingIcon = leadingIcon
    ) {
        Text(
            text = text,
            style =
            when (size) {
                PremiumButtonSize.Small -> MaterialTheme.typography.labelSmall
                PremiumButtonSize.Medium -> MaterialTheme.typography.labelMedium
                PremiumButtonSize.Large -> MaterialTheme.typography.labelLarge
            }.copy(fontWeight = FontWeight.Medium)
        )
    }
}
