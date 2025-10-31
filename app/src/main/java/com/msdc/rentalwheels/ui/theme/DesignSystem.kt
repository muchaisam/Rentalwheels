package com.msdc.rentalwheels.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** RentalWheels Design System Professional design tokens for consistent branding and spacing */
object RentalWheelsDesignSystem {

    /** Brand Colors - Professional color palette */
    object Colors {
        // Primary Brand Colors
        val BrandPrimary = Color(0xFF2563EB) // Professional blue
        val BrandPrimaryVariant = Color(0xFF1D4ED8) // Darker blue
        val BrandPrimaryLight = Color(0xFF3B82F6) // Lighter blue

        // Secondary Brand Colors
        val BrandSecondary = Color(0xFF059669) // Success green
        val BrandSecondaryVariant = Color(0xFF047857) // Darker green
        val BrandSecondaryLight = Color(0xFF10B981) // Lighter green

        // Accent Colors
        val BrandAccent = Color(0xFFDC2626) // Attention red
        val BrandAccentVariant = Color(0xFFB91C1C) // Darker red
        val BrandWarning = Color(0xFFF59E0B) // Warning amber
        val BrandInfo = Color(0xFF0EA5E9) // Info blue

        // Neutral Colors - Light Theme
        val NeutralWhite = Color(0xFFFFFFFF) // Pure white
        val NeutralGray50 = Color(0xFFFAFAFA) // Lightest gray
        val NeutralGray100 = Color(0xFFF5F5F5) // Very light gray
        val NeutralGray200 = Color(0xFFE5E5E5) // Light gray
        val NeutralGray300 = Color(0xFFD4D4D4) // Medium light gray
        val NeutralGray400 = Color(0xFFA3A3A3) // Medium gray
        val NeutralGray500 = Color(0xFF737373) // Medium dark gray
        val NeutralGray600 = Color(0xFF525252) // Dark gray
        val NeutralGray700 = Color(0xFF404040) // Very dark gray
        val NeutralGray800 = Color(0xFF262626) // Almost black
        val NeutralGray900 = Color(0xFF171717) // Near black
        val NeutralBlack = Color(0xFF000000) // Pure black

        // Surface Colors - Light Theme
        val Surface01 = NeutralGray50 // Primary surface
        val Surface02 = NeutralWhite // Card surfaces
        val Surface03 = NeutralGray100 // Elevated surfaces
        val Surface04 = NeutralGray200 // Dividers, borders

        // Surface Colors - Dark Theme
        val SurfaceDark01 = Color(0xFF121212) // Primary dark surface
        val SurfaceDark02 = Color(0xFF1E1E1E) // Card dark surfaces
        val SurfaceDark03 = Color(0xFF242424) // Elevated dark surfaces
        val SurfaceDark04 = Color(0xFF2A2A2A) // Dark dividers, borders

        // Text Colors - Light Theme
        val TextPrimary = NeutralGray900 // High contrast text
        val TextSecondary = NeutralGray600 // Medium emphasis text
        val TextTertiary = NeutralGray400 // Low emphasis text
        val TextDisabled = NeutralGray300 // Disabled text
        val TextOnBrand = NeutralWhite // Text on brand colors

        // Text Colors - Dark Theme
        val TextPrimaryDark = NeutralGray100 // High contrast dark text
        val TextSecondaryDark = NeutralGray300 // Medium emphasis dark text
        val TextTertiaryDark = NeutralGray500 // Low emphasis dark text
        val TextDisabledDark = NeutralGray600 // Disabled dark text

        // State Colors
        val Success = BrandSecondary
        val SuccessLight = Color(0xFFD1FAE5)
        val Error = BrandAccent
        val ErrorLight = Color(0xFFFEE2E2)
        val Warning = BrandWarning
        val WarningLight = Color(0xFFFEF3C7)
        val Info = BrandInfo
        val InfoLight = Color(0xFFE0F2FE)
    }

    /** Typography Scale - Consistent text styling */
    object Typography {
        // Display Styles - Large headlines
        val displayLarge =
            TextStyle(
                fontSize = 57.sp,
                lineHeight = 64.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.25).sp
            )

        val displayMedium =
            TextStyle(
                fontSize = 45.sp,
                lineHeight = 52.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )

        val displaySmall =
            TextStyle(
                fontSize = 36.sp,
                lineHeight = 44.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )

        // Headline Styles - Section headers
        val headlineLarge =
            TextStyle(
                fontSize = 32.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.sp
            )

        val headlineMedium =
            TextStyle(
                fontSize = 28.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp
            )

        val headlineSmall =
            TextStyle(
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.sp
            )

        // Title Styles - Component headers
        val titleLarge =
            TextStyle(
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.sp
            )

        val titleMedium =
            TextStyle(
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.15.sp
            )

        val titleSmall =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.1.sp
            )

        // Body Styles - Regular content
        val bodyLarge =
            TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.5.sp
            )

        val bodyMedium =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.25.sp
            )

        val bodySmall =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.4.sp
            )

        // Label Styles - UI labels and buttons
        val labelLarge =
            TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.1.sp
            )

        val labelMedium =
            TextStyle(
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )

        val labelSmall =
            TextStyle(
                fontSize = 10.sp,
                lineHeight = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
    }

    /** Spacing Scale - Consistent spacing system */
    object Spacing {
        // Base spacing unit
        private const val BaseUnit = 4

        // Spacing scale based on 4dp grid system
        val XXS = (BaseUnit * 1).dp // 4dp
        val extraSmall = (BaseUnit * 2).dp // 8dp
        val small = (BaseUnit * 3).dp // 12dp
        val medium = (BaseUnit * 4).dp // 16dp
        val large = (BaseUnit * 5).dp // 20dp
        val extraLarge = (BaseUnit * 6).dp // 24dp
        val XXL = (BaseUnit * 8).dp // 32dp
        val XXXL = (BaseUnit * 10).dp // 40dp
        val XXXXL = (BaseUnit * 12).dp // 48dp
        val Huge = (BaseUnit * 16).dp // 64dp

        // Component-specific spacing
        val ButtonPadding = medium // 16dp
        val CardPadding = large // 20dp
        val ScreenPadding = extraLarge // 24dp
        val SectionSpacing = XXL // 32dp
        val ItemSpacing = extraSmall // 8dp
        val ContentSpacing = small // 12dp
    }

    /** Corner Radius Scale - Consistent border radius */
    object CornerRadius {
        val extraSmall = 4.dp
        val small = 8.dp
        val medium = 12.dp
        val large = 16.dp
        val extraLarge = 20.dp
        val XXL = 24.dp
        val Round = 50.dp // For circular elements

        // Component-specific radius
        val Button = medium // 12dp
        val Card = large // 16dp
        val Sheet = extraLarge // 20dp
        val Dialog = large // 16dp
    }

    /** Elevation Scale - Consistent shadow depths */
    object Elevation {
        val None = 0.dp
        val extraSmall = 1.dp
        val small = 2.dp
        val medium = 4.dp
        val large = 8.dp
        val extraLarge = 12.dp
        val XXL = 16.dp
        val XXXL = 24.dp

        // Component-specific elevation
        val Card = medium // 4dp
        val Button = small // 2dp
        val Sheet = extraLarge // 12dp
        val Dialog = XXL // 16dp
        val FloatingButton = large // 8dp
    }

    /** Animation Durations - Consistent timing */
    object Animation {
        const val Fast = 150
        const val Medium = 300
        const val Slow = 500
        const val Slower = 800

        // Component-specific durations
        const val ButtonPress = Fast // 150ms
        const val CardHover = Medium // 300ms
        const val ScreenTransition = Slow // 500ms
        const val Loading = Slower // 800ms
    }

    /** Breakpoints - Responsive design */
    object Breakpoints {
        val Compact = 600.dp // Phones
        val Medium = 840.dp // Tablets
        val Expanded = 1200.dp // Desktop
    }
}

/** Convenient access to design system values */
object DS {
    val colors = RentalWheelsDesignSystem.Colors
    val typography = RentalWheelsDesignSystem.Typography
    val spacing = RentalWheelsDesignSystem.Spacing
    val radius = RentalWheelsDesignSystem.CornerRadius
    val elevation = RentalWheelsDesignSystem.Elevation
}

/** Theme-aware design system access */
@Composable
@ReadOnlyComposable
fun designSystem(): RentalWheelsDesignSystem = RentalWheelsDesignSystem
