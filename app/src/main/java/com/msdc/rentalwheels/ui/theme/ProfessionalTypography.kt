//package com.msdc.rentalwheels.ui.theme
//
//import androidx.compose.material3.Typography
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import com.msdc.rentalwheels.R
//
//object AppFont {
//    val inter =
//        FontFamily(
//            Font(R.font.iregular, FontWeight.Normal),
//        )
//}
//
///**
// * Professional Typography System for RentalWheels Using Design System values with Inter font family
// */
//val RentalWheelsTypography =
//    Typography(
//        // Display styles for large headlines
//        displayLarge =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 57.sp,
//            lineHeight = 64.sp,
//            fontWeight = FontWeight.Bold,
//            letterSpacing = (-0.25).sp
//        ),
//        displayMedium =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 45.sp,
//            lineHeight = 52.sp,
//            fontWeight = FontWeight.Bold,
//            letterSpacing = 0.sp
//        ),
//        displaySmall =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 36.sp,
//            lineHeight = 44.sp,
//            fontWeight = FontWeight.Bold,
//            letterSpacing = 0.sp
//        ),
//
//        // Headline styles for section headers
//        headlineLarge =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 32.sp,
//            lineHeight = 40.sp,
//            fontWeight = FontWeight.Bold,
//            letterSpacing = 0.sp
//        ),
//        headlineMedium =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 28.sp,
//            lineHeight = 36.sp,
//            fontWeight = FontWeight.SemiBold,
//            letterSpacing = 0.sp
//        ),
//        headlineSmall =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 24.sp,
//            lineHeight = 32.sp,
//            fontWeight = FontWeight.SemiBold,
//            letterSpacing = 0.sp
//        ),
//
//        // Title styles for component headers
//        titleLarge =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 22.sp,
//            lineHeight = 28.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.sp
//        ),
//        titleMedium =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 18.sp,
//            lineHeight = 24.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.15.sp
//        ),
//        titleSmall =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 16.sp,
//            lineHeight = 20.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.1.sp
//        ),
//
//        // Body styles for regular content
//        bodyLarge =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 16.sp,
//            lineHeight = 24.sp,
//            fontWeight = FontWeight.Normal,
//            letterSpacing = 0.5.sp
//        ),
//        bodyMedium =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 14.sp,
//            lineHeight = 20.sp,
//            fontWeight = FontWeight.Normal,
//            letterSpacing = 0.25.sp
//        ),
//        bodySmall =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 12.sp,
//            lineHeight = 16.sp,
//            fontWeight = FontWeight.Normal,
//            letterSpacing = 0.4.sp
//        ),
//
//        // Label styles for UI labels and buttons
//        labelLarge =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 14.sp,
//            lineHeight = 20.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.1.sp
//        ),
//        labelMedium =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 12.sp,
//            lineHeight = 16.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.5.sp
//        ),
//        labelSmall =
//        TextStyle(
//            fontFamily = AppFont.inter,
//            fontSize = 10.sp,
//            lineHeight = 14.sp,
//            fontWeight = FontWeight.Medium,
//            letterSpacing = 0.5.sp
//        )
//    )
//
//// Keep backward compatibility
//val Typography = RentalWheelsTypography
