package com.msdc.rentalwheels.ui.components.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Text(
        text = title,
        style = RentalWheelsDesignSystem.Typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = RentalWheelsDesignSystem.Spacing.medium)
    )
    content()
    HorizontalDivider(
        modifier = Modifier.padding(vertical = RentalWheelsDesignSystem.Spacing.small)
    )
}
