package com.example.tradepulse.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val SlatePalette = TradeThemeColors(
    background = Color(0xFF090D16),
    surface = Color(0xFF111827),
    surfaceVariant = Color(0xFF131D33),
    cardBackground = Color(0xFF111827),
    cardBorder = Color(0xFF1E293B),
    primary = Color(0xFF6366F1),
    primaryLight = Color(0xFF818CF8),
    primaryDark = Color(0xFF4338CA),
    secondary = Color(0xFF06B6D4),
    secondaryLight = Color(0xFF164E63),
    profitGreen = Color(0xFF10B981),
    profitGreenSoft = Color(0xFF064E3B),
    profitGreenLight = Color(0xFF34D399),
    lossRed = Color(0xFFF43F5E),
    lossRedSoft = Color(0xFF4C0519),
    lossRedLight = Color(0xFFFB7185),
    accentAmber = Color(0xFFF59E0B),
    textPrimary = Color(0xFFF8FAFC),
    textSecondary = Color(0xFF94A3B8),
    textTertiary = Color(0xFF64748B),
    isGlass = false,
    backgroundBrush = null
)

val FrostGlassPalette = TradeThemeColors(
    background = Color(0xFF070B16),
    surface = Color(0x351F2942),
    surfaceVariant = Color(0x40253554),
    cardBackground = Color(0x24FFFFFF), // Translucent frosted glass
    cardBorder = Color(0x4DFFFFFF),     // Luminous glass rim
    primary = Color(0xFF38BDF8),        // Luminous ice cyan
    primaryLight = Color(0xFF7DD3FC),
    primaryDark = Color(0xFF0284C7),
    secondary = Color(0xFFC084FC),      // Neon violet accent
    secondaryLight = Color(0x40581C87),
    profitGreen = Color(0xFF10B981),
    profitGreenSoft = Color(0x40065F46),
    profitGreenLight = Color(0xFF4ADE80),
    lossRed = Color(0xFFFB7185),
    lossRedSoft = Color(0x40881337),
    lossRedLight = Color(0xFFFDA4AF),
    accentAmber = Color(0xFFFBBF24),
    textPrimary = Color(0xFFFFFFFF),
    textSecondary = Color(0xFFCBD5E1),
    textTertiary = Color(0xFF94A3B8),
    isGlass = true,
    backgroundBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF080D20),
            Color(0xFF140D2E),
            Color(0xFF08192A)
        )
    )
)

val CyberpunkPalette = TradeThemeColors(
    background = Color(0xFF05050A),
    surface = Color(0xFF0E0E1C),
    surfaceVariant = Color(0xFF18182E),
    cardBackground = Color(0xFF0E0E1C),
    cardBorder = Color(0xFF2E1C55),
    primary = Color(0xFFEC4899),        // Neon Pink
    primaryLight = Color(0xFFF472B6),
    primaryDark = Color(0xFF9D174D),
    secondary = Color(0xFF00F0FF),      // Electric Cyan
    secondaryLight = Color(0xFF083344),
    profitGreen = Color(0xFF00FF88),    // Toxic Lime
    profitGreenSoft = Color(0xFF05381E),
    profitGreenLight = Color(0xFF55FFAA),
    lossRed = Color(0xFFFF0055),        // Laser Red
    lossRedSoft = Color(0xFF450017),
    lossRedLight = Color(0xFFFF5588),
    accentAmber = Color(0xFFFFB703),
    textPrimary = Color(0xFFFDFDFD),
    textSecondary = Color(0xFFA0A0C0),
    textTertiary = Color(0xFF6B6B92),
    isGlass = false,
    backgroundBrush = null
)

val EmeraldPalette = TradeThemeColors(
    background = Color(0xFF03140E),
    surface = Color(0xFF08261B),
    surfaceVariant = Color(0xFF0E382A),
    cardBackground = Color(0xFF08261B),
    cardBorder = Color(0xFF14533D),
    primary = Color(0xFF10B981),        // Emerald Green
    primaryLight = Color(0xFF34D399),
    primaryDark = Color(0xFF047857),
    secondary = Color(0xFFF59E0B),      // Wall Street Gold
    secondaryLight = Color(0xFF451A03),
    profitGreen = Color(0xFF10B981),
    profitGreenSoft = Color(0xFF044830),
    profitGreenLight = Color(0xFF6EE7B7),
    lossRed = Color(0xFFF43F5E),
    lossRedSoft = Color(0xFF4C0519),
    lossRedLight = Color(0xFFFB7185),
    accentAmber = Color(0xFFFBBF24),
    textPrimary = Color(0xFFECFDF5),
    textSecondary = Color(0xFF99F6E4),
    textTertiary = Color(0xFF4D8B75),
    isGlass = false,
    backgroundBrush = null
)

val ObsidianGoldPalette = TradeThemeColors(
    background = Color(0xFF08080A),
    surface = Color(0xFF121216),
    surfaceVariant = Color(0xFF1E1E24),
    cardBackground = Color(0xFF121216),
    cardBorder = Color(0xFF2C2C38),
    primary = Color(0xFFEAB308),        // Champagne Gold
    primaryLight = Color(0xFFFACC15),
    primaryDark = Color(0xFFA16207),
    secondary = Color(0xFF38BDF8),
    secondaryLight = Color(0xFF082F49),
    profitGreen = Color(0xFF10B981),
    profitGreenSoft = Color(0xFF073322),
    profitGreenLight = Color(0xFF34D399),
    lossRed = Color(0xFFF43F5E),
    lossRedSoft = Color(0xFF4C0519),
    lossRedLight = Color(0xFFFB7185),
    accentAmber = Color(0xFFEAB308),
    textPrimary = Color(0xFFF9FAFB),
    textSecondary = Color(0xFFA1A1AA),
    textTertiary = Color(0xFF71717A),
    isGlass = false,
    backgroundBrush = null
)

val LocalTradeThemeColors = staticCompositionLocalOf { SlatePalette }

object TradePulseTheme {
    val colors: TradeThemeColors
        @Composable
        @ReadOnlyComposable
        get() = LocalTradeThemeColors.current
}

@Composable
fun TradePulseTheme(
    theme: AppThemeMode = AppThemeMode.SLATE,
    content: @Composable () -> Unit
) {
    val activePalette = when (theme) {
        AppThemeMode.SLATE -> SlatePalette
        AppThemeMode.FROST_GLASS -> FrostGlassPalette
        AppThemeMode.CYBERPUNK -> CyberpunkPalette
        AppThemeMode.EMERALD -> EmeraldPalette
        AppThemeMode.OBSIDIAN_GOLD -> ObsidianGoldPalette
    }

    val colorScheme = darkColorScheme(
        primary = activePalette.primary,
        onPrimary = Color.White,
        primaryContainer = activePalette.primaryDark,
        onPrimaryContainer = Color.White,
        secondary = activePalette.secondary,
        onSecondary = Color.Black,
        secondaryContainer = activePalette.secondaryLight,
        onSecondaryContainer = Color.White,
        tertiary = activePalette.profitGreen,
        onTertiary = Color.Black,
        tertiaryContainer = activePalette.profitGreenSoft,
        onTertiaryContainer = Color.White,
        background = activePalette.background,
        onBackground = activePalette.textPrimary,
        surface = activePalette.surface,
        onSurface = activePalette.textPrimary,
        surfaceVariant = activePalette.surfaceVariant,
        onSurfaceVariant = activePalette.textSecondary,
        outline = activePalette.cardBorder,
        outlineVariant = activePalette.cardBorder.copy(alpha = 0.5f),
        error = activePalette.lossRed,
        onError = Color.White,
        errorContainer = activePalette.lossRedSoft,
        onErrorContainer = Color.White
    )

    CompositionLocalProvider(
        LocalTradeThemeColors provides activePalette
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
