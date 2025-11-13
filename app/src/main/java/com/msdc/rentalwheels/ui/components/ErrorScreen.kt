package com.msdc.rentalwheels.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun ErrorScreen(message: String, modifier: Modifier = Modifier, onRetry: (() -> Unit)? = null) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(), contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = RentalWheelsDesignSystem.Typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier =
                Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.medium)
            )

            onRetry?.let { retryAction ->
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))
                Button(
                    onClick = retryAction,
                    modifier =
                    Modifier.padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium
                    )
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
                    Text("Retry")
                }
            }
        }
    }
}
