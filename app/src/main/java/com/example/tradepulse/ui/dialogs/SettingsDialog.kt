package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.ui.theme.*

@Composable
fun SettingsDialog(
    currentTheme: AppThemeMode,
    isGlobalAutoSync: Boolean,
    onSelectTheme: (AppThemeMode) -> Unit,
    onToggleGlobalAutoSync: (Boolean) -> Unit,
    onCleanDataClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val themeOptions = listOf(
        ThemeOption(
            mode = AppThemeMode.FROST_GLASS,
            title = "Frost Glass",
            badge = "SPECIAL GLASS EFFECT",
            description = "Translucent frosted acrylic with deep sapphire blur & ambient purple glow",
            previewBrush = Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF1E1B4B))),
            accentColor = Color(0xFF38BDF8)
        ),
        ThemeOption(
            mode = AppThemeMode.SLATE,
            title = "Obsidian Slate",
            badge = "DEFAULT",
            description = "Clean institutional dark palette with high-contrast electric cyan highlights",
            previewBrush = Brush.linearGradient(listOf(Color(0xFF0B101D), Color(0xFF131D31))),
            accentColor = ElectricCyan
        ),
        ThemeOption(
            mode = AppThemeMode.CYBERPUNK,
            title = "Cyberpunk Neon",
            badge = "HIGH CONTRAST",
            description = "Vibrant synthwave aesthetic with neon cyan and intense electric pink accents",
            previewBrush = Brush.linearGradient(listOf(Color(0xFF090818), Color(0xFF1B0B2E))),
            accentColor = Color(0xFFFF2A85)
        ),
        ThemeOption(
            mode = AppThemeMode.EMERALD,
            title = "Emerald Bull",
            badge = "BULL RUN",
            description = "Deep dark forest green palette with luminous mint profit highlights",
            previewBrush = Brush.linearGradient(listOf(Color(0xFF06140E), Color(0xFF0D281E))),
            accentColor = Color(0xFF10B981)
        ),
        ThemeOption(
            mode = AppThemeMode.OBSIDIAN_GOLD,
            title = "Obsidian Gold",
            badge = "LUXURY FINISH",
            description = "Warm institutional dark brass with glowing metallic gold indicators",
            previewBrush = Brush.linearGradient(listOf(Color(0xFF120F08), Color(0xFF231C0E))),
            accentColor = Color(0xFFFBBF24)
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                .testTag("settings_dialog"),
            color = CardBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("Settings & Preferences", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("App appearance, themes & execution options", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("settings_close_button")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Section 1: Themes ("in setting option add theam")
                    item {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                                    Text("THEME OPTIONS", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                                }
                                Text("Live Switch", color = ElectricCyan, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Select your trading workspace theme style including the special glass effect:",
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    items(themeOptions, key = { it.mode.name }) { theme ->
                        val isSelected = currentTheme == theme.mode

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(theme.previewBrush)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) theme.accentColor else CardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onSelectTheme(theme.mode) }
                                .padding(12.dp)
                                .testTag("theme_option_${theme.mode.name}")
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = theme.title,
                                            color = TextPrimary,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(theme.accentColor.copy(alpha = 0.2f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = theme.badge,
                                                color = theme.accentColor,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = theme.description,
                                        color = TextSecondary,
                                        fontSize = 11.sp,
                                        lineHeight = 15.sp
                                    )
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(theme.accentColor),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Section 2: Execution & Automation
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.Autorenew, contentDescription = null, tint = ProfitGreenLight, modifier = Modifier.size(16.dp))
                            Text("BROKER AUTO-SYNC", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Navy850)
                                .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Global Auto-Sync Engine", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Automatically pull order fills from active brokers in background", color = TextSecondary, fontSize = 11.sp)
                                }
                                Switch(
                                    checked = isGlobalAutoSync,
                                    onCheckedChange = { onToggleGlobalAutoSync(it) },
                                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = ProfitGreen)
                                )
                            }
                        }
                    }

                    // Section 3: Clean Workspace
                    item {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, tint = LossRedLight, modifier = Modifier.size(16.dp))
                            Text("DATA RESET", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                        }
                    }

                    item {
                        Surface(
                            onClick = {
                                onDismiss()
                                onCleanDataClick()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = LossRedSoft,
                            border = androidx.compose.foundation.BorderStroke(1.dp, LossRed.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Clean Workspace Data", color = LossRedLight, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text("Wipe dummy or test data and reset to fresh slate", color = TextSecondary, fontSize = 11.sp)
                                }
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, tint = LossRedLight, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy850)
                ) {
                    Text("Done", color = TextPrimary)
                }
            }
        }
    }
}

private data class ThemeOption(
    val mode: AppThemeMode,
    val title: String,
    val badge: String,
    val description: String,
    val previewBrush: Brush,
    val accentColor: Color
)
