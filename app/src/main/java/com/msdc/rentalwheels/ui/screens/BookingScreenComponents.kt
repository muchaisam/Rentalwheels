import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
private fun SpecificationItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
            )
            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))
            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = value,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FeatureChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = RentalWheelsDesignSystem.Spacing.small,
                    vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.XXS)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
            Text(
                text = text,
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BookingDatesCard(startDate: LocalDate, endDate: LocalDate, totalDays: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rental Period",
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "$totalDays days",
                        style = RentalWheelsDesignSystem.Typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.SemiBold,
                        modifier =
                        Modifier.padding(
                            horizontal = RentalWheelsDesignSystem.Spacing.medium,
                            vertical = RentalWheelsDesignSystem.Spacing.small
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                DateCard(
                    icon = Icons.Default.DateRange,
                    label = "Pick-up",
                    date = startDate,
                    time = "10:00 AM",
                    modifier = Modifier.weight(1f)
                )

                DateCard(
                    icon = Icons.Default.CalendarMonth,
                    label = "Drop-off",
                    date = endDate,
                    time = "10:00 AM",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                )
                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                Text(
                    text = "Nairobi City Center - Same pick-up and drop-off location",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun DateCard(
    icon: ImageVector,
    label: String,
    date: LocalDate,
    time: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM dd")),
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = time,
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PricingBreakdownCard(
    dailyRate: Int,
    totalDays: Int,
    subtotal: Int,
    insurance: Int,
    taxes: Int,
    total: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Text(
                text = "Price Breakdown",
                style = RentalWheelsDesignSystem.Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            PriceRow(
                label = "Daily Rate",
                value = CurrencyUtils.formatDailyRate(dailyRate),
                description = "$totalDays days"
            )

            PriceRow(label = "Subtotal", value = CurrencyUtils.formatDailyRate(subtotal))

            PriceRow(
                label = "Insurance",
                value = CurrencyUtils.formatDailyRate(insurance),
                description = "15% coverage"
            )

            PriceRow(
                label = "Taxes & Fees",
                value = CurrencyUtils.formatDailyRate(taxes),
                description = "8% VAT"
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            androidx.compose.material3.HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                thickness = RentalWheelsDesignSystem.Elevation.extraSmall
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = CurrencyUtils.formatDailyRate(total),
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, description: String? = null) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            description?.let {
                Text(
                    text = it,
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = value,
            style = RentalWheelsDesignSystem.Typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TermsConditionsCard(car: Car) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                )
                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                Text(
                    text = "Important Information",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            TermsItem(
                icon = Icons.Default.Security,
                title = "Insurance Coverage",
                description =
                if (car.insuranceIncluded) "Comprehensive insurance included"
                else "Insurance not included - additional coverage available"
            )

            TermsItem(
                icon = Icons.Default.AccessTime,
                title = "Cancellation Policy",
                description = car.cancellationPolicy
            )

            TermsItem(
                icon = Icons.Default.LocationOn,
                title = "Fuel Policy",
                description = "Return vehicle with same fuel level as pickup"
            )

            if (car.depositRequired > 0) {
                TermsItem(
                    icon = Icons.Default.Security,
                    title = "Security Deposit",
                    description =
                    "Refundable deposit of ${CurrencyUtils.formatDailyRate(car.depositRequired.toInt())} required"
                )
            }
        }
    }
}

@Composable
private fun TermsItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.small),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                )
            }
        }

        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = RentalWheelsDesignSystem.Typography.bodyMedium.lineHeight
            )
        }
    }
}
