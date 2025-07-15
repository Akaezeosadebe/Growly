package com.growly.app.ui.theme

import androidx.compose.ui.graphics.Color

// Growly Color Palette - Soft, Calming, Pastel Colors
object GrowlyColors {
    // Primary Colors
    val SoftBeige = Color(0xFFF5F5DC)        // Background: Soft Beige (#F5F5DC)
    val SkyBlue = Color(0xFF87CEEB)          // Accents: Sky Blue (#87CEEB)
    val BlushPink = Color(0xFFFFB6C1)        // Highlights: Blush Pink (#FFB6C1)
    val LightMint = Color(0xFF98FF98)        // Positive Indicators: Light Mint (#98FF98)
    val DarkerGray = Color(0xFF555555)       // Body Text: Darker Gray (#555555)
    
    // Additional Supporting Colors
    val White = Color(0xFFFFFFFF)
    val LightGray = Color(0xFFF8F8F8)
    val MediumGray = Color(0xFF888888)
    val SoftShadow = Color(0x1A000000)       // For gentle shadows
    
    // Semantic Colors
    val Success = LightMint
    val Warning = Color(0xFFFFE4B5)          // Soft peach for warnings
    val Error = Color(0xFFFFCDD2)            // Soft red for errors
    val Info = SkyBlue
    
    // Card and Surface Colors
    val CardBackground = White
    val SurfaceVariant = Color(0xFFFAFAFA)
    
    // Text Colors
    val TextPrimary = DarkerGray
    val TextSecondary = MediumGray
    val TextOnAccent = White
}

// Light Theme Colors
val md_theme_light_primary = GrowlyColors.SkyBlue
val md_theme_light_onPrimary = GrowlyColors.White
val md_theme_light_primaryContainer = GrowlyColors.SkyBlue.copy(alpha = 0.1f)
val md_theme_light_onPrimaryContainer = GrowlyColors.DarkerGray
val md_theme_light_secondary = GrowlyColors.BlushPink
val md_theme_light_onSecondary = GrowlyColors.White
val md_theme_light_secondaryContainer = GrowlyColors.BlushPink.copy(alpha = 0.1f)
val md_theme_light_onSecondaryContainer = GrowlyColors.DarkerGray
val md_theme_light_tertiary = GrowlyColors.LightMint
val md_theme_light_onTertiary = GrowlyColors.DarkerGray
val md_theme_light_tertiaryContainer = GrowlyColors.LightMint.copy(alpha = 0.1f)
val md_theme_light_onTertiaryContainer = GrowlyColors.DarkerGray
val md_theme_light_error = GrowlyColors.Error
val md_theme_light_errorContainer = GrowlyColors.Error.copy(alpha = 0.1f)
val md_theme_light_onError = GrowlyColors.White
val md_theme_light_onErrorContainer = GrowlyColors.DarkerGray
val md_theme_light_background = GrowlyColors.SoftBeige
val md_theme_light_onBackground = GrowlyColors.TextPrimary
val md_theme_light_surface = GrowlyColors.CardBackground
val md_theme_light_onSurface = GrowlyColors.TextPrimary
val md_theme_light_surfaceVariant = GrowlyColors.SurfaceVariant
val md_theme_light_onSurfaceVariant = GrowlyColors.TextSecondary
val md_theme_light_outline = GrowlyColors.MediumGray.copy(alpha = 0.3f)
val md_theme_light_inverseOnSurface = GrowlyColors.White
val md_theme_light_inverseSurface = GrowlyColors.DarkerGray
val md_theme_light_inversePrimary = GrowlyColors.SkyBlue.copy(alpha = 0.8f)
val md_theme_light_shadow = GrowlyColors.SoftShadow
val md_theme_light_surfaceTint = GrowlyColors.SkyBlue
val md_theme_light_outlineVariant = GrowlyColors.MediumGray.copy(alpha = 0.1f)
val md_theme_light_scrim = Color(0x00000000)
