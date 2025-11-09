package com.msdc.rentalwheels.ui.components.browse

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.home.CarCard
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun EnhancedCarCard(
        car: Car,
        onClick: () -> Unit,
        isFavorite: Boolean,
        onToggleFavorite: () -> Unit,
        isSelectedForComparison: Boolean,
        canAddToComparison: Boolean,
        onToggleComparison: () -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = RentalWheelsDesignSystem.Elevation.small
                    )
    ) {
        Box {
            // Base CarCard
            CarCard(car = car, onClick = onClick, modifier = Modifier.fillMaxWidth())

            // Enhanced features overlay
            Row(
                    modifier =
                            Modifier.align(Alignment.TopEnd)
                                    .padding(RentalWheelsDesignSystem.Spacing.small)
            ) {
                // Favorite Button
                FavoriteButton(isFavorite = isFavorite, onToggleFavorite = onToggleFavorite)

                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.XXS))

                // Comparison Button
                ComparisonButton(
                        isSelected = isSelectedForComparison,
                        canAdd = canAddToComparison,
                        onToggleComparison = onToggleComparison
                )
            }
        }
    }
}
