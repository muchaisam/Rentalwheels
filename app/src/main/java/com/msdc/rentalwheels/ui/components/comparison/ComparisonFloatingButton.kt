package com.msdc.rentalwheels.ui.components.comparison

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Compare
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

/**
 * Premium Floating Action Button for car comparison Shows when 1-2 cars are selected with
 * glassmorphic design
 */
@Composable
fun ComparisonFloatingButton(
        selectedCount: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
) {
    AnimatedVisibility(
            visible = selectedCount > 0,
            enter =
                    expandVertically(
                            animationSpec =
                                    spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessMedium
                                    )
                    ) + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
            modifier = modifier
    ) {
        FloatingActionButton(
                onClick = onClick,
                modifier = Modifier,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large)
        ) {
            Row(
                    modifier =
                            Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.large),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement =
                            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                BadgedBox(
                        badge = {
                            if (selectedCount > 0) {
                                Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                ) {
                                    Text(
                                            text = selectedCount.toString(),
                                            style = RentalWheelsDesignSystem.Typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                ) {
                    Icon(
                            imageVector = Icons.Outlined.Compare,
                            contentDescription = "Compare cars",
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large),
                            tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Text(
                        text = "Compare",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
