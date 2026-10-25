package com.example.tradepulse.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val isGlass: Boolean = false
) {
    SLATE(
        id = "slate",
        displayName = "Midnight Slate",
        subtitle = "Institutional Dark Navy"
    ),
    FROST_GLASS(
        id = "frost_glass",
        displayName = "Frosted Glass",
        subtitle = "Special Translucent Glassmorphism",
        isGlass = true
    ),
    CYBERPUNK(
        id = "cyberpunk",
        displayName = "Cyberpunk Neon",
        subtitle = "High-Voltage Electric Neon"
    ),
    EMERALD(
        id = "emerald",
        displayName = "Wall Street Emerald",
        subtitle = "Prestige Forest & Money Green"
    ),
    OBSIDIAN_GOLD(
        id = "obsidian_gold",
        displayName = "Obsidian Gold",
        subtitle = "Luxury Terminal & Champagne Gold"
    )
}

data class TradeThemeColors(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val primary: Color,
    val primaryLight: Color,
    val primaryDark: Color,
    val secondary: Color,
    val secondaryLight: Color,
    val profitGreen: Color,
    val profitGreenSoft: Color,
    val profitGreenLight: Color,
    val lossRed: Color,
    val lossRedSoft: Color,
    val lossRedLight: Color,
    val accentAmber: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val isGlass: Boolean = false,
    val backgroundBrush: Brush? = null
)

// Dynamic theme color accessors that react to the active theme
val CardBackground: Color
    @Composable
    get() = TradePulseTheme.colors.cardBackground

val CardBorder: Color
    @Composable
    get() = TradePulseTheme.colors.cardBorder

val Navy950: Color
    @Composable
    get() = TradePulseTheme.colors.background

val Navy900: Color
    @Composable
    get() = TradePulseTheme.colors.surface

val Navy850: Color
    @Composable
    get() = TradePulseTheme.colors.surfaceVariant

val Navy800: Color
    @Composable
    get() = TradePulseTheme.colors.cardBorder

val Navy700: Color
    @Composable
    get() = TradePulseTheme.colors.surfaceVariant

val Navy600: Color
    @Composable
    get() = TradePulseTheme.colors.textTertiary

val BrandIndigo: Color
    @Composable
    get() = TradePulseTheme.colors.primary

val BrandIndigoLight: Color
    @Composable
    get() = TradePulseTheme.colors.primaryLight

val BrandIndigoDark: Color
    @Composable
    get() = TradePulseTheme.colors.primaryDark

val ElectricCyan: Color
    @Composable
    get() = TradePulseTheme.colors.secondary

val ElectricCyanSoft: Color
    @Composable
    get() = TradePulseTheme.colors.secondaryLight

val AccentAmber: Color
    @Composable
    get() = TradePulseTheme.colors.accentAmber

val AccentPurple: Color
    @Composable
    get() = Color(0xFFA855F7)

val ProfitGreen: Color
    @Composable
    get() = TradePulseTheme.colors.profitGreen

val ProfitGreenSoft: Color
    @Composable
    get() = TradePulseTheme.colors.profitGreenSoft

val ProfitGreenLight: Color
    @Composable
    get() = TradePulseTheme.colors.profitGreenLight

val LossRed: Color
    @Composable
    get() = TradePulseTheme.colors.lossRed

val LossRedSoft: Color
    @Composable
    get() = TradePulseTheme.colors.lossRedSoft

val LossRedLight: Color
    @Composable
    get() = TradePulseTheme.colors.lossRedLight

val TextPrimary: Color
    @Composable
    get() = TradePulseTheme.colors.textPrimary

val TextSecondary: Color
    @Composable
    get() = TradePulseTheme.colors.textSecondary

val TextTertiary: Color
    @Composable
    get() = TradePulseTheme.colors.textTertiary
