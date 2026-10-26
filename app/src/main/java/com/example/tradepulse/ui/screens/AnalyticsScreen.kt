package com.example.tradepulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.*
import com.example.tradepulse.ui.components.StatCard
import com.example.tradepulse.ui.theme.*

@Composable
fun AnalyticsScreen(
    stats: AccountStats,
    emotionalBreakdown: List<EmotionMetric>,
    setupBreakdown: List<SetupMetric>,
    mistakesBreakdown: List<MistakeMetric>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Section 1: Detailed Account Health Stats
        item {
            Text(
                text = "EXECUTION METRICS & EXPECTANCY",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Gross Profit",
                    value = TradeAnalyticsCalculator.formatCurrency(stats.grossProfit),
                    valueColor = ProfitGreen,
                    subValue = "Winning Trades",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Gross Loss",
                    value = "-${TradeAnalyticsCalculator.formatCurrency(stats.grossLoss)}",
                    valueColor = LossRed,
                    subValue = "Losing Trades",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Avg Win",
                    value = TradeAnalyticsCalculator.formatCurrency(stats.avgWin),
                    valueColor = ProfitGreenLight,
                    subValue = "Per Winning Trade",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Avg Loss",
                    value = "-${TradeAnalyticsCalculator.formatCurrency(stats.avgLoss)}",
                    valueColor = LossRedLight,
                    subValue = "Per Losing Trade",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Win/Loss Ratio",
                    value = "${stats.winLossRatio}:1",
                    valueColor = ElectricCyan,
                    subValue = "Payoff Ratio",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section 2: Emotional Sentiment & Psychological Leak Breakdown
        item {
            Column {
                Text(
                    text = "PSYCHOLOGICAL & EMOTIONAL LEAK ANALYSIS",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Tracking how your pre-trade emotional state directly impacts profitability and win rate.",
                    color = TextTertiary,
                    fontSize = 11.sp
                )
            }
        }

        if (emotionalBreakdown.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No emotional data recorded yet. Tag mindset when logging trades.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            items(emotionalBreakdown, key = { it.emotion.name }) { item ->
                val isAlpha = item.netPnL > 0
                val isLeak = item.netPnL < 0

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("emotion_card_${item.emotion.name}")
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(
                            1.dp,
                            if (isAlpha) ProfitGreen.copy(alpha = 0.3f) else if (isLeak) LossRed.copy(alpha = 0.3f) else CardBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.emotion.name,
                                    color = TextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isAlpha) ProfitGreenSoft else if (isLeak) LossRedSoft else Navy850)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isAlpha) "Edge State" else if (isLeak) "Capital Leak" else "Neutral",
                                        color = if (isAlpha) ProfitGreenLight else if (isLeak) LossRedLight else TextSecondary,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${item.count} Trades | Win Rate: ${item.winRate}% | Avg: ${TradeAnalyticsCalculator.formatRMultiple(item.avgRMultiple)}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = TradeAnalyticsCalculator.formatCurrency(item.netPnL),
                                color = if (item.netPnL >= 0) ProfitGreen else LossRed,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Avg: ${TradeAnalyticsCalculator.formatCurrency(item.avgPnL)}",
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 3: Strategy & Playbook Setup Breakdown
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "STRATEGY PLAYBOOK PERFORMANCE",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        if (setupBreakdown.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No playbook setup data logged yet. Assign setups when logging executions.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        } else {
            items(setupBreakdown, key = { it.setup }) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("setup_card_${item.setup}")
                        .clip(RoundedCornerShape(10.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = item.setup,
                                color = TextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${item.count} Trades • ${item.winRate}% Win Rate • PF: ${item.profitFactor}",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = TradeAnalyticsCalculator.formatCurrency(item.netPnL),
                            color = if (item.netPnL >= 0) ProfitGreen else LossRed,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Section 4: Behavioral Mistakes Audit
        if (mistakesBreakdown.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "RECURRING BEHAVIORAL MISTAKES",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            items(mistakesBreakdown, key = { it.mistake }) { item ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LossRedSoft.copy(alpha = 0.4f))
                        .border(1.dp, LossRed.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = item.mistake,
                                color = LossRedLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Occurred ${item.count} times",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Text(
                            text = "-${TradeAnalyticsCalculator.formatCurrency(item.totalLostPnL)}",
                            color = LossRed,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
