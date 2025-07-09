package com.msdc.rentalwheels.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {

    /**
     * Formats a price value to KSH currency (values are already in KSH from Firestore)
     * @param amount The amount to format (already in KSH)
     * @param showDecimals Whether to show decimal places (default: false for KSH)
     * @return Formatted string with KSH currency symbol
     */
    fun formatToKSH(amount: Double, showDecimals: Boolean = false): String {
        return if (showDecimals) {
            "KSH ${NumberFormat.getNumberInstance(Locale.US).format(amount)}"
        } else {
            "KSH ${NumberFormat.getNumberInstance(Locale.US).format(amount.toInt())}"
        }
    }

    /**
     * Formats a price value to KSH currency for integer amounts (already in KSH)
     * @param amount The amount to format (already in KSH)
     * @return Formatted string with KSH currency symbol
     */
    fun formatToKSH(amount: Int): String {
        return "KSH ${NumberFormat.getNumberInstance(Locale.US).format(amount)}"
    }

    /**
     * Formats daily rate to KSH with "per day" suffix
     * @param dailyRate The daily rate (already in KSH)
     * @return Formatted string with KSH currency symbol and "per day"
     */
    fun formatDailyRate(dailyRate: Int): String {
        return "${formatToKSH(dailyRate)}/day"
    }

    /**
     * Formats daily rate to KSH with "per day" suffix for double values
     * @param dailyRate The daily rate (already in KSH)
     * @return Formatted string with KSH currency symbol and "per day"
     */
    fun formatDailyRate(dailyRate: Double): String {
        return "${formatToKSH(dailyRate)}/day"
    }
}
