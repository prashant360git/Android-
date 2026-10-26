package com.example.tradepulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.AccountStats
import com.example.tradepulse.data.model.DaySummary
import com.example.tradepulse.data.model.Trade
import com.example.tradepulse.ui.components.*
import com.example.tradepulse.ui.theme.*

@Composable
fun DashboardScreen(
    stats: AccountStats,
    trades: List<Trade>,
    daySummaries: List<DaySummary>,
    onSelectDay: (DaySummary) -> Unit,
    onEditTrade: (Trade) -> Unit,
    onDeleteTrade: (String) -> Unit,
    onRemoveAllTrades: (() -> Unit)? = null,
    onViewAllTrades: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // KPI Stat Cards Grid (2 rows x 3 columns)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "Net P&L",
                        value = TradeAnalyticsCalculator.formatCurrency(stats.netPnL),
                        valueColor = if (stats.netPnL >= 0) ProfitGreen else LossRed,
                        subValue = "Total Profit",
                        icon = Icons.Default.MonetizationOn,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_net_pnl"
                    )
                    StatCard(
                        title = "Win Rate",
                        value = "${stats.winRate}%",
                        valueColor = if (stats.winRate >= 50) ProfitGreen else LossRed,
                        subValue = "${stats.winningTrades}W - ${stats.losingTrades}L",
                        icon = Icons.Default.EmojiEvents,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_win_rate"
                    )
                    StatCard(
                        title = "Profit Factor",
                        value = stats.profitFactor.toString(),
                        valueColor = if (stats.profitFactor >= 1.5) ProfitGreen else TextPrimary,
                        subValue = "Gross R:R",
                        icon = Icons.Default.Timeline,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_profit_factor"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        title = "Expectancy",
                        value = TradeAnalyticsCalculator.formatCurrency(stats.expectancy),
                        valueColor = if (stats.expectancy >= 0) ProfitGreen else LossRed,
                        subValue = "Per Trade",
                        icon = Icons.Default.Speed,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_expectancy"
                    )
                    StatCard(
                        title = "Max Drawdown",
                        value = "-${TradeAnalyticsCalculator.formatCurrency(stats.maxDrawdown)}",
                        valueColor = LossRedLight,
                        subValue = "Peak to Trough",
                        icon = Icons.Default.TrendingDown,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_drawdown"
                    )
                    StatCard(
                        title = "Discipline",
                        value = "${stats.disciplineAvg}/5",
                        valueColor = AccentAmber,
                        subValue = "${stats.followedPlanRate}% Followed",
                        icon = Icons.Default.Verified,
                        modifier = Modifier.weight(1f),
                        testTag = "kpi_discipline"
                    )
                }
            }
        }

        // Equity Curve Chart
        item {
            EquityCurveChart(
                trades = trades,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Execution Calendar Grid
        item {
            CalendarViewGrid(
                daySummaries = daySummaries,
                onSelectDay = onSelectDay,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Recent Trades Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "RECENT EXECUTIONS",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (trades.isNotEmpty() && onRemoveAllTrades != null) {
                        TextButton(
                            onClick = onRemoveAllTrades,
                            modifier = Modifier.testTag("dashboard_clear_all_button")
                        ) {
                            Text(
                                text = "Clear All",
                                color = LossRedLight,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    TextButton(onClick = onViewAllTrades) {
                        Text(
                            text = "View All (${trades.size})",
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Display up to 4 most recent trades
        val recentTrades = trades.sortedByDescending { it.entryDate }.take(4)
        if (recentTrades.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = TextTertiary,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "Workspace is clean. No executions logged yet.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        } else {
            items(recentTrades, key = { it.id }) { trade ->
                TradeItemCard(
                    trade = trade,
                    onEdit = { onEditTrade(trade) },
                    onDelete = { onDeleteTrade(trade.id) }
                )
            }
        }
    }
}
