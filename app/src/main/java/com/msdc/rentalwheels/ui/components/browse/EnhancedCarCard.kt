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
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.home.CarCard

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
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            // Base CarCard
            CarCard(car = car, onClick = onClick, modifier = Modifier.fillMaxWidth())

            // Enhanced features overlay
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                // Favorite Button
                FavoriteButton(isFavorite = isFavorite, onToggleFavorite = onToggleFavorite)

                Spacer(modifier = Modifier.width(4.dp))

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
