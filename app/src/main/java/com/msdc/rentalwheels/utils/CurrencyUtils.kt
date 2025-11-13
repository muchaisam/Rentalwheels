package com.msdc.rentalwheels.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    /**
     * Formats a price value to KES currency (values are already in KES from Firestore)
     * @param amount The amount to format (already in KES)
     * @param showDecimals Whether to show decimal places (default: false for KES)
     * @return Formatted string with KES currency symbol
     */
    fun formatToKSH(amount: Double, showDecimals: Boolean = false): String {
        return if (showDecimals) {
            "KES ${NumberFormat.getNumberInstance(Locale.US).format(amount)}"
        } else {
            "KES ${NumberFormat.getNumberInstance(Locale.US).format(amount.toInt())}"
        }
    }

    /**
     * Formats a price value to KES currency for integer amounts (already in KES)
     * @param amount The amount to format (already in KES)
     * @return Formatted string with KES currency symbol
     */
    fun formatToKSH(amount: Int): String {
        return "KES ${NumberFormat.getNumberInstance(Locale.US).format(amount)}"
    }

    /**
     * Formats daily rate to KES with "per day" suffix
     * @param dailyRate The daily rate (already in KES)
     * @return Formatted string with KES currency symbol and "per day"
     */
    fun formatDailyRate(dailyRate: Int): String {
        return "${formatToKSH(dailyRate)}/day"
    }

    /**
     * Formats daily rate to KES with "per day" suffix for double values
     * @param dailyRate The daily rate (already in KES)
     * @return Formatted string with KES currency symbol and "per day"
     */
    fun formatDailyRate(dailyRate: Double): String {
        return "${formatToKSH(dailyRate)}/day"
    }
}
