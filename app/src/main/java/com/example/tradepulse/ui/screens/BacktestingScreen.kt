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
import com.example.tradepulse.data.model.BacktestTrade
import com.example.tradepulse.ui.components.StatCard
import com.example.tradepulse.ui.theme.*

@Composable
fun BacktestingScreen(
    backtestTrades: List<BacktestTrade>,
    onAddBacktestTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalBacktests = backtestTrades.size
    val closedTrades = backtestTrades.filter { it.status == "Closed" }
    val winTrades = closedTrades.filter { (it.realizedPnL ?: 0.0) > 0 }
    val winRate = if (closedTrades.isNotEmpty()) (winTrades.size.toDouble() / closedTrades.size * 100).toInt() else 0
    val totalPnL = closedTrades.sumOf { it.realizedPnL ?: 0.0 }
    val avgR = if (closedTrades.isNotEmpty()) closedTrades.mapNotNull { it.rMultiple }.average() else 0.0
    val avgAdherence = if (backtestTrades.isNotEmpty()) backtestTrades.map { it.ruleAdherencePercent }.average() else 100.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("backtesting_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "BACKTESTING ENGINE",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Forward & historical replay testing for playbook checklist validation",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                FilledTonalButton(
                    onClick = onAddBacktestTrade,
                    modifier = Modifier.testTag("add_backtest_trade_button"),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BrandIndigo,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Log Backtest", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Summary Stats Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard(
                    title = "Simulated P&L",
                    value = TradeAnalyticsCalculator.formatCurrency(totalPnL),
                    valueColor = if (totalPnL >= 0) ProfitGreen else LossRed,
                    subValue = "$totalBacktests Sessions",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Win Rate",
                    value = "$winRate%",
                    valueColor = if (winRate >= 50) ProfitGreen else TextPrimary,
                    subValue = "${winTrades.size}W - ${closedTrades.size - winTrades.size}L",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Rule Adherence",
                    value = "${avgAdherence.toInt()}%",
                    valueColor = ElectricCyan,
                    subValue = "Checklist Match",
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Text(
                text = "LOGGED REPLAY SESSIONS",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        if (backtestTrades.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Science,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "No Replay Sessions Recorded",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Forward test your strategy rules against historical market conditions to measure win rate and discipline obedience.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onAddBacktestTrade,
                            colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Start Replay Session", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(backtestTrades, key = { it.id }) { bt ->
                val pnl = bt.realizedPnL ?: 0.0
                val isWin = pnl > 0

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("backtest_card_${bt.id}")
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = bt.symbol,
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (bt.direction == "BUY") ProfitGreenSoft else LossRedSoft)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = bt.direction,
                                    color = if (bt.direction == "BUY") ProfitGreenLight else LossRedLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = bt.timeframe,
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = TradeAnalyticsCalculator.formatCurrency(pnl),
                                color = if (isWin) ProfitGreen else LossRed,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (bt.rMultiple != null) {
                                Text(
                                    text = TradeAnalyticsCalculator.formatRMultiple(bt.rMultiple),
                                    color = TextTertiary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    if (bt.playbookTitle != null) {
                        Text(
                            text = "Playbook: ${bt.playbookTitle}",
                            color = BrandIndigoLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Entry: $${bt.entry} | Stop: $${bt.stopLoss} | Target: $${bt.profitTarget}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (bt.ruleAdherencePercent >= 100.0) ProfitGreenSoft else ElectricCyanSoft)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Checklist: ${bt.checklistPassedCount}/${bt.checklistTotalCount} (${bt.ruleAdherencePercent.toInt()}%)",
                                color = if (bt.ruleAdherencePercent >= 100.0) ProfitGreenLight else ElectricCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = bt.createdAt.take(10),
                            color = TextTertiary,
                            fontSize = 10.sp
                        )
                    }

                    if (bt.notes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = bt.notes,
                            color = TextTertiary,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
}

