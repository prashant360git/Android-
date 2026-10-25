package com.example.tradepulse.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
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
import com.example.tradepulse.data.model.DaySummary
import com.example.tradepulse.ui.theme.*

@Composable
fun CalendarViewGrid(
    daySummaries: List<DaySummary>,
    onSelectDay: (DaySummary) -> Unit,
    modifier: Modifier = Modifier
) {
    val dayMap = daySummaries.associateBy { it.date }

    // October 2026 calendar days: starts on Thursday (index 4 in Mon-Sun grid, or index 3 for 0-indexed Mon)
    // Days 1 through 31
    val weekdays = listOf("M", "T", "W", "T", "F", "S", "S")

    Box(
        modifier = modifier
            .testTag("calendar_view_card")
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
                Column {
                    Text(
                        text = "TRADING CALENDAR",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "October 2026",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Month Net P&L Summary
                val monthPnL = daySummaries.sumOf { it.netPnL }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (monthPnL >= 0) ProfitGreenSoft else LossRedSoft)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Month: ${TradeAnalyticsCalculator.formatCurrency(monthPnL)}",
                        color = if (monthPnL >= 0) ProfitGreenLight else LossRedLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Weekday header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekdays.forEach { dayLetter ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayLetter,
                            color = TextTertiary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 5 weeks grid for October 2026 (Oct 1 is Thursday -> 3 empty leading slots for Mon, Tue, Wed)
            val calendarDays = (1..3).map { null } + (1..31).map { dayNum ->
                val dateStr = "2026-10-${dayNum.toString().padStart(2, '0')}"
                val summary = dayMap[dateStr]
                Pair(dayNum, summary)
            }

            // Render calendar rows
            calendarDays.chunked(7).forEach { weekRow ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    weekRow.forEach { slot ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                        ) {
                            if (slot != null) {
                                val (dayNum, summary) = slot
                                val hasTrades = summary != null && summary.tradesCount > 0
                                val isProfitable = (summary?.netPnL ?: 0.0) >= 0.0

                                val bgColor = when {
                                    hasTrades && isProfitable -> ProfitGreenSoft
                                    hasTrades && !isProfitable -> LossRedSoft
                                    else -> Navy850
                                }

                                val borderColor = when {
                                    hasTrades && isProfitable -> ProfitGreen.copy(alpha = 0.5f)
                                    hasTrades && !isProfitable -> LossRed.copy(alpha = 0.5f)
                                    else -> Color.Transparent
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(bgColor)
                                        .border(1.dp, borderColor, RoundedCornerShape(6.dp))
                                        .clickable(enabled = hasTrades) {
                                            if (summary != null) onSelectDay(summary)
                                        }
                                        .padding(3.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = dayNum.toString(),
                                        color = if (hasTrades) TextPrimary else TextTertiary,
                                        fontSize = 10.sp,
                                        fontWeight = if (hasTrades) FontWeight.Bold else FontWeight.Normal
                                    )

                                    if (hasTrades && summary != null) {
                                        Text(
                                            text = if (summary.netPnL >= 0) "+$${summary.netPnL.toInt()}" else "-$${kotlin.math.abs(summary.netPnL).toInt()}",
                                            color = if (isProfitable) ProfitGreenLight else LossRedLight,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = "${summary.tradesCount}t",
                                            color = TextTertiary,
                                            fontSize = 8.sp
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(1.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
