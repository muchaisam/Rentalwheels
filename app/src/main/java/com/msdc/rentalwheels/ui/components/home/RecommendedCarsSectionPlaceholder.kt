package com.msdc.rentalwheels.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

// Loading state for RecommendedCarsSection
@Composable
fun RecommendedCarsSectionPlaceholder(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.medium)
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = RentalWheelsDesignSystem.Spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier =
                Modifier
                    .width(RentalWheelsDesignSystem.Spacing.Huge * 2.5f)
                    .height(RentalWheelsDesignSystem.Spacing.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

        LazyRow(
            contentPadding =
            PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.medium),
            horizontalArrangement =
            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            items(3) {
                CarCardPlaceholder(
                    modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.Huge * 4.375f)
                )
            }
        }
    }
}
