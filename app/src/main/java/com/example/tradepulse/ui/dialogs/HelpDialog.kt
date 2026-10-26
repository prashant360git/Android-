package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.ui.theme.*

@Composable
fun HelpDialog(
    onDismiss: () -> Unit
) {
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
                .testTag("help_dialog"),
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
                                .background(AccentAmber.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.HelpOutline, contentDescription = null, tint = AccentAmber, modifier = Modifier.size(22.dp))
                        }
                        Column {
                            Text("TradePulse Help & Guide", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text("Institutional protocols & feature walkthrough", color = TextSecondary, fontSize = 11.sp)
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("help_close_button")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 12.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Item 1: Logging Trades
                    item {
                        HelpSectionCard(
                            icon = Icons.Default.AddCircleOutline,
                            iconTint = ElectricCyan,
                            title = "1. Logging Trades & Emotional Mindset",
                            content = "Tap '+ Log' in the top bar or floating action button. Record your entry, exit, position size, and most importantly your pre-trade emotion (Calm, Patient, FOMO, Anxious). TradePulse calculates net P&L and R-Multiples automatically."
                        )
                    }

                    // Item 2: Google Drive Backup
                    item {
                        HelpSectionCard(
                            icon = Icons.Default.CloudSync,
                            iconTint = Color(0xFF4285F4),
                            title = "2. Google Drive Cloud Backup",
                            content = "Access 'Data Backup' in the upper-left cube menu. TradePulse saves your entire workspace (trades, accounts, playbooks, journals) directly to your personal Google Drive in the 'TradePulse_Backups' folder. Turn on Auto-Save for real-time cloud sync."
                        )
                    }

                    // Item 3: Broker Auto-Sync
                    item {
                        HelpSectionCard(
                            icon = Icons.Default.Autorenew,
                            iconTint = ProfitGreenLight,
                            title = "3. Broker Auto-Synchronize",
                            content = "Connect your brokerage (Interactive Brokers, Tradovate, Schwab, Alpaca, Binance). When Auto-Sync is turned ON, the engine polls for new filled executions and automatically settles them into your designated trading account."
                        )
                    }

                    // Item 4: 2-Loss Daily Stop Protocol
                    item {
                        HelpSectionCard(
                            icon = Icons.Default.Shield,
                            iconTint = LossRedLight,
                            title = "4. Institutional 2-Loss Rule",
                            content = "If you stop out on two consecutive trades in the same session, close your trading terminal for at least 45 minutes. This breaks the neurological revenge-trading impulse and protects capital."
                        )
                    }

                    // Item 5: Theme Personalization
                    item {
                        HelpSectionCard(
                            icon = Icons.Default.Palette,
                            iconTint = BrandIndigoLight,
                            title = "5. Themes & Special Glass Effect",
                            content = "Open Settings in the upper-left cube menu to switch themes anytime. Try the 'Frost Glass' theme for a translucent frosted glass aesthetic with sapphire blur, or 'Cyberpunk' for vibrant neon contrast."
                        )
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Navy850)
                ) {
                    Text("Got It", color = TextPrimary)
                }
            }
        }
    }
}

@Composable
private fun HelpSectionCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    content: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Navy850)
            .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = content, color = TextSecondary, fontSize = 12.sp, lineHeight = 17.sp)
            }
        }
    }
}
