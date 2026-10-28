package com.example.tradepulse.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.DailyPnLPoint
import com.example.tradepulse.data.model.Trade
import com.example.tradepulse.ui.theme.*
import kotlin.math.abs
import kotlin.math.max

@Composable
fun DailyPnLBarChart(
    trades: List<Trade>,
    modifier: Modifier = Modifier
) {
    val dailyPoints = remember(trades) {
        TradeAnalyticsCalculator.getLast30DaysPnL(trades)
    }

    var selectedIndex by remember(dailyPoints) {
        val lastActiveIndex = dailyPoints.indexOfLast { it.tradesCount > 0 }
        mutableStateOf(if (lastActiveIndex >= 0) lastActiveIndex else null)
    }

    val activeDays = remember(dailyPoints) { dailyPoints.filter { it.tradesCount > 0 } }
    val greenDaysCount = remember(dailyPoints) { dailyPoints.count { it.netPnL > 0.01 } }
    val redDaysCount = remember(dailyPoints) { dailyPoints.count { it.netPnL < -0.01 } }
    val bestDay = remember(dailyPoints) { dailyPoints.maxByOrNull { it.netPnL }?.takeIf { it.netPnL > 0 } }
    val worstDay = remember(dailyPoints) { dailyPoints.minByOrNull { it.netPnL }?.takeIf { it.netPnL < 0 } }
    val thirtyDayNet = remember(dailyPoints) { dailyPoints.sumOf { it.netPnL } }

    val selectedPoint = selectedIndex?.let { dailyPoints.getOrNull(it) }

    // Capture @Composable theme colors before entering Canvas DrawScope
    val cardBorderColor = CardBorder
    val textTertiaryColor = TextTertiary
    val profitGreenColor = ProfitGreen
    val profitGreenLightColor = ProfitGreenLight
    val lossRedColor = LossRed
    val lossRedLightColor = LossRedLight
    val electricCyanColor = ElectricCyan
    val brandIndigoColor = BrandIndigo
    val accentAmberColor = AccentAmber

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .testTag("daily_pnl_bar_chart")
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandIndigo.copy(alpha = 0.2f))
                        .border(1.dp, BrandIndigo.copy(alpha = 0.45f), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Column {
                    Text(
                        text = "30-DAY DAILY P&L DISTRIBUTION",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Daily net realized profit & loss bars (tap bar to inspect)",
                        color = TextTertiary,
                        fontSize = 11.sp
                    )
                }
            }

            // 30D Net Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        when {
                            thirtyDayNet > 0.01 -> ProfitGreenSoft
                            thirtyDayNet < -0.01 -> LossRedSoft
                            else -> Navy850
                        }
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                val sign = if (thirtyDayNet > 0.01) "+" else ""
                Text(
                    text = "30D: $sign${TradeAnalyticsCalculator.formatCurrency(thirtyDayNet)}",
                    color = when {
                        thirtyDayNet > 0.01 -> ProfitGreenLight
                        thirtyDayNet < -0.01 -> LossRedLight
                        else -> TextSecondary
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Selected Day Inspection Banner
        if (selectedPoint != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Navy850)
                    .border(
                        1.dp,
                        when {
                            selectedPoint.netPnL > 0.01 -> ProfitGreen.copy(alpha = 0.4f)
                            selectedPoint.netPnL < -0.01 -> LossRed.copy(alpha = 0.4f)
                            else -> CardBorder
                        },
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    selectedPoint.netPnL > 0.01 -> ProfitGreen
                                    selectedPoint.netPnL < -0.01 -> LossRed
                                    else -> TextTertiary
                                }
                            )
                    )
                    Column {
                        Text(
                            text = "${selectedPoint.shortLabel} (${selectedPoint.date})",
                            color = TextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (selectedPoint.tradesCount > 0) {
                                "${selectedPoint.tradesCount} Trades (${selectedPoint.winCount}W / ${selectedPoint.lossCount}L)"
                            } else {
                                "No executions recorded on this session"
                            },
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val prefix = if (selectedPoint.netPnL > 0.01) "+" else ""
                    Text(
                        text = "$prefix${TradeAnalyticsCalculator.formatCurrency(selectedPoint.netPnL)}",
                        color = when {
                            selectedPoint.netPnL > 0.01 -> ProfitGreenLight
                            selectedPoint.netPnL < -0.01 -> LossRedLight
                            else -> TextSecondary
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    IconButton(
                        onClick = { selectedIndex = null },
                        modifier = Modifier.size(22.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear day selection",
                            tint = TextTertiary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // Canvas Bar Chart Area
        val maxAbsPnL = remember(dailyPoints) {
            val peak = dailyPoints.maxOfOrNull { abs(it.netPnL) } ?: 0.0
            max(peak, 100.0)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(176.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Navy950.copy(alpha = 0.55f))
                .border(1.dp, CardBorder.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                .padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            // Y-axis reference labels on left edge
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "+${TradeAnalyticsCalculator.formatCurrency(maxAbsPnL).substringBefore(".")}",
                    color = ProfitGreenLight.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$0",
                    color = TextTertiary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "-${TradeAnalyticsCalculator.formatCurrency(maxAbsPnL).substringBefore(".")}",
                    color = LossRedLight.copy(alpha = 0.75f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 42.dp)
                    .testTag("daily_pnl_canvas")
                    .pointerInput(dailyPoints) {
                        detectTapGestures { tapOffset ->
                            if (size.width > 0 && dailyPoints.isNotEmpty()) {
                                val slotWidth = size.width.toFloat() / dailyPoints.size
                                val tappedIndex = (tapOffset.x / slotWidth).toInt()
                                    .coerceIn(0, dailyPoints.lastIndex)
                                selectedIndex = if (selectedIndex == tappedIndex) null else tappedIndex
                            }
                        }
                    }
            ) {
                val width = size.width
                val height = size.height
                val centerY = height / 2f
                val maxBarHalfHeight = (height / 2f) - 6.dp.toPx()

                val dashEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

                // Top (+Max) and Bottom (-Max) subtle reference lines
                drawLine(
                    color = cardBorderColor.copy(alpha = 0.45f),
                    start = Offset(0f, 6.dp.toPx()),
                    end = Offset(width, 6.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )
                drawLine(
                    color = cardBorderColor.copy(alpha = 0.45f),
                    start = Offset(0f, height - 6.dp.toPx()),
                    end = Offset(width, height - 6.dp.toPx()),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )

                // Quarter grid lines (+50% and -50%)
                drawLine(
                    color = cardBorderColor.copy(alpha = 0.25f),
                    start = Offset(0f, centerY - maxBarHalfHeight * 0.5f),
                    end = Offset(width, centerY - maxBarHalfHeight * 0.5f),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )
                drawLine(
                    color = cardBorderColor.copy(alpha = 0.25f),
                    start = Offset(0f, centerY + maxBarHalfHeight * 0.5f),
                    end = Offset(width, centerY + maxBarHalfHeight * 0.5f),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = dashEffect
                )

                // Zero P&L Baseline
                drawLine(
                    color = textTertiaryColor.copy(alpha = 0.65f),
                    start = Offset(0f, centerY),
                    end = Offset(width, centerY),
                    strokeWidth = 1.25.dp.toPx()
                )

                val count = dailyPoints.size
                if (count == 0) return@Canvas

                val slotWidth = width / count
                val barWidth = (slotWidth * 0.68f).coerceAtLeast(3.dp.toPx())
                val cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())

                dailyPoints.forEachIndexed { index, point ->
                    val slotLeft = index * slotWidth
                    val barLeft = slotLeft + (slotWidth - barWidth) / 2f
                    val isSelected = selectedIndex == index

                    // Highlight column vertical guide if selected
                    if (isSelected) {
                        drawRoundRect(
                            color = brandIndigoColor.copy(alpha = 0.16f),
                            topLeft = Offset(slotLeft, 0f),
                            size = Size(slotWidth, height),
                            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                        )
                    }

                    val normalized = (abs(point.netPnL) / maxAbsPnL).toFloat().coerceIn(0f, 1f)
                    val barHeight = (normalized * maxBarHalfHeight).coerceAtLeast(
                        if (point.tradesCount > 0) 4.dp.toPx() else 2.dp.toPx()
                    )

                    when {
                        point.netPnL > 0.01 -> {
                            // Profit bar extending upward from centerY
                            val topY = centerY - barHeight
                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    colors = if (isSelected) {
                                        listOf(electricCyanColor, profitGreenLightColor)
                                    } else {
                                        listOf(profitGreenLightColor, profitGreenColor.copy(alpha = 0.85f))
                                    },
                                    startY = topY,
                                    endY = centerY
                                ),
                                topLeft = Offset(barLeft, topY),
                                size = Size(barWidth, barHeight),
                                cornerRadius = cornerRadius
                            )
                        }
                        point.netPnL < -0.01 -> {
                            // Loss bar extending downward from centerY
                            drawRoundRect(
                                brush = Brush.verticalGradient(
                                    colors = if (isSelected) {
                                        listOf(accentAmberColor, lossRedLightColor)
                                    } else {
                                        listOf(lossRedColor.copy(alpha = 0.85f), lossRedLightColor)
                                    },
                                    startY = centerY,
                                    endY = centerY + barHeight
                                ),
                                topLeft = Offset(barLeft, centerY),
                                size = Size(barWidth, barHeight),
                                cornerRadius = cornerRadius
                            )
                        }
                        point.tradesCount > 0 -> {
                            // Breakeven active session
                            drawRoundRect(
                                color = if (isSelected) electricCyanColor else accentAmberColor,
                                topLeft = Offset(barLeft, centerY - 2.dp.toPx()),
                                size = Size(barWidth, 4.dp.toPx()),
                                cornerRadius = cornerRadius
                            )
                        }
                        else -> {
                            // Flat tick on baseline for inactive days
                            drawRoundRect(
                                color = if (isSelected) electricCyanColor else cardBorderColor.copy(alpha = 0.8f),
                                topLeft = Offset(barLeft, centerY - 1.dp.toPx()),
                                size = Size(barWidth, 2.dp.toPx()),
                                cornerRadius = cornerRadius
                            )
                        }
                    }
                }
            }

            if (activeDays.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "30-day baseline active • Log or sync trades to populate daily P&L bars",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // X-Axis Date Milestones (Every 6 days across the 30-day window)
        if (dailyPoints.size >= 30) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 42.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val indices = listOf(0, 6, 12, 18, 24, 29)
                indices.forEach { idx ->
                    val pt = dailyPoints[idx]
                    Text(
                        text = pt.shortLabel,
                        color = if (selectedIndex == idx) ElectricCyan else TextTertiary,
                        fontSize = 9.sp,
                        fontWeight = if (selectedIndex == idx) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        // Bottom Summary Metrics Row (Profitable Days, Losing Days, Best Day, Worst Day)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DailyBarStatPill(
                label = "Green Days",
                value = "$greenDaysCount",
                valueColor = ProfitGreenLight,
                modifier = Modifier.weight(1f)
            )
            DailyBarStatPill(
                label = "Red Days",
                value = "$redDaysCount",
                valueColor = LossRedLight,
                modifier = Modifier.weight(1f)
            )
            DailyBarStatPill(
                label = "Best Day",
                value = bestDay?.let { "+${TradeAnalyticsCalculator.formatCurrency(it.netPnL).substringBefore(".")}" } ?: "$0",
                valueColor = ProfitGreen,
                modifier = Modifier.weight(1f)
            )
            DailyBarStatPill(
                label = "Worst Day",
                value = worstDay?.let { TradeAnalyticsCalculator.formatCurrency(it.netPnL).substringBefore(".") } ?: "$0",
                valueColor = LossRed,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DailyBarStatPill(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Navy850.copy(alpha = 0.75f))
            .border(1.dp, CardBorder.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label.uppercase(),
            color = TextTertiary,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.3.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}
