package com.growly.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.growly.app.R

// Custom font family - using system fonts for now, can be replaced with custom fonts
val GrowlyFontFamily = FontFamily.Default

// Typography following Material 3 guidelines with Growly's soothing aesthetic
val GrowlyTypography = Typography(
    // Display styles - for large headings
    displayLarge = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
        color = GrowlyColors.TextPrimary
    ),
    displayMedium = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    displaySmall = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    
    // Headline styles - for section titles
    headlineLarge = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    headlineSmall = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    
    // Title styles - for card titles and important text
    titleLarge = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = GrowlyColors.TextPrimary
    ),
    titleMedium = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
        color = GrowlyColors.TextPrimary
    ),
    titleSmall = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color = GrowlyColors.TextPrimary
    ),
    
    // Body styles - for main content and reading
    bodyLarge = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = GrowlyColors.TextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
        color = GrowlyColors.TextPrimary
    ),
    bodySmall = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
        color = GrowlyColors.TextSecondary
    ),
    
    // Label styles - for buttons and small text
    labelLarge = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
        color = GrowlyColors.TextPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = GrowlyColors.TextPrimary
    ),
    labelSmall = TextStyle(
        fontFamily = GrowlyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = GrowlyColors.TextSecondary
    )
)
