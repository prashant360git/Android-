package com.example.tradepulse.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.Trade
import com.example.tradepulse.ui.theme.*

@Composable
fun EquityCurveChart(
    trades: List<Trade>,
    modifier: Modifier = Modifier
) {
    val points = remember(trades) {
        val sorted = trades.sortedBy { it.entryDate }
        var acc = 0.0
        val list = mutableListOf(0.0)
        sorted.forEach { t ->
            acc += t.netPnL
            list.add(acc)
        }
        list
    }

    val maxVal = remember(points) { (points.maxOrNull() ?: 1000.0).coerceAtLeast(500.0) }
    val minVal = remember(points) { (points.minOrNull() ?: 0.0).coerceAtMost(0.0) }
    val totalRange = (maxVal - minVal).coerceAtLeast(100.0)
    val finalPnL = points.lastOrNull() ?: 0.0

    Box(
        modifier = modifier
            .testTag("equity_curve_card")
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
                        text = "CUMULATIVE EQUITY CURVE",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = TradeAnalyticsCalculator.formatCurrency(finalPnL),
                        color = if (finalPnL >= 0) ProfitGreen else LossRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (finalPnL >= 0) ProfitGreenSoft else LossRedSoft)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${points.size - 1} Trades",
                        color = if (finalPnL >= 0) ProfitGreenLight else LossRedLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val gridBorderColor = CardBorder
            val baselineColor = TextTertiary.copy(alpha = 0.5f)
            val chartColor = if (finalPnL >= 0) ProfitGreen else LossRed

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .testTag("equity_curve_canvas")
            ) {
                val width = size.width
                val height = size.height

                // Draw background grid lines
                val gridLines = 4
                for (i in 0..gridLines) {
                    val y = height * (i.toFloat() / gridLines)
                    drawLine(
                        color = gridBorderColor,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1f
                    )
                }

                // Draw baseline zero line if minVal < 0
                val zeroY = (height - ((0.0 - minVal) / totalRange * height)).toFloat()
                if (zeroY in 0f..height) {
                    drawLine(
                        color = baselineColor,
                        start = Offset(0f, zeroY),
                        end = Offset(width, zeroY),
                        strokeWidth = 1.5f
                    )
                }

                if (points.size < 2) return@Canvas

                val stepX = width / (points.size - 1)
                val linePath = Path()
                val fillPath = Path()

                points.forEachIndexed { idx, value ->
                    val x = idx * stepX
                    val y = (height - ((value - minVal) / totalRange * height)).toFloat().coerceIn(0f, height)

                    if (idx == 0) {
                        linePath.moveTo(x, y)
                        fillPath.moveTo(x, height)
                        fillPath.lineTo(x, y)
                    } else {
                        linePath.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }

                    if (idx == points.size - 1) {
                        fillPath.lineTo(x, height)
                        fillPath.close()
                    }
                }

                // Draw filled gradient area
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(
                        chartColor.copy(alpha = 0.35f),
                        Color.Transparent
                    )
                )
                drawPath(
                    path = fillPath,
                    brush = gradientBrush
                )

                // Draw curve stroke
                drawPath(
                    path = linePath,
                    color = chartColor,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Draw end point dot
                val lastX = (points.size - 1) * stepX
                val lastY = (height - ((finalPnL - minVal) / totalRange * height)).toFloat().coerceIn(0f, height)
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
                drawCircle(
                    color = chartColor,
                    radius = 2.5.dp.toPx(),
                    center = Offset(lastX, lastY)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Initial: $0.00",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Text(
                    text = "Peak: ${TradeAnalyticsCalculator.formatCurrency(maxVal)}",
                    color = TextTertiary,
                    fontSize = 10.sp
                )
                Text(
                    text = "Max DD: -${TradeAnalyticsCalculator.formatCurrency((maxVal - finalPnL).coerceAtLeast(0.0))}",
                    color = LossRedLight,
                    fontSize = 10.sp
                )
            }
        }
    }
}
