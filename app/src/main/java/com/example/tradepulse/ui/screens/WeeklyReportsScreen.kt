package com.example.tradepulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.WeeklyReportData
import com.example.tradepulse.ui.theme.*

@Composable
fun WeeklyReportsScreen(
    report: WeeklyReportData?,
    isGenerating: Boolean,
    onGenerateReport: () -> Unit,
    aiErrorMessage: String? = null,
    aiCoachAnswer: String? = null,
    isAskingCoach: Boolean = false,
    onAskCoach: (String) -> Unit = {},
    onDismissError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var coachPrompt by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("weekly_reports_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "WEEKLY PERFORMANCE AUDIT",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BrandIndigo.copy(alpha = 0.25f))
                                .border(1.dp, ElectricCyan.copy(alpha = 0.45f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "GEMINI AI",
                                color = ElectricCyan,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Text(
                        text = "Powered by Firebase AI Logic & Gemini 3.8 Flash",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onGenerateReport,
                    enabled = !isGenerating,
                    modifier = Modifier.testTag("generate_report_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Analyzing...", fontSize = 12.sp)
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Run AI Audit", fontSize = 12.sp)
                    }
                }
            }
        }

        if (aiErrorMessage != null) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(LossRedSoft)
                        .border(1.dp, LossRed.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = LossRedLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = aiErrorMessage,
                            color = LossRedLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    IconButton(onClick = onDismissError, modifier = Modifier.size(24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Dismiss error",
                            tint = LossRedLight,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Interactive Gemini AI Trading Coach Box
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = null,
                        tint = ElectricCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "ASK GEMINI TRADING COACH",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = coachPrompt,
                        onValueChange = { coachPrompt = it },
                        placeholder = {
                            Text(
                                "Ask Gemini about your risk, setups, or discipline...",
                                color = TextTertiary,
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("gemini_coach_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = ElectricCyan,
                            unfocusedBorderColor = CardBorder,
                            focusedContainerColor = Navy950,
                            unfocusedContainerColor = Navy950
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Button(
                        onClick = {
                            if (coachPrompt.isNotBlank()) {
                                onAskCoach(coachPrompt)
                            }
                        },
                        enabled = !isAskingCoach && coachPrompt.isNotBlank(),
                        modifier = Modifier.testTag("gemini_coach_send_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricCyan),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 14.dp)
                    ) {
                        if (isAskingCoach) {
                            CircularProgressIndicator(
                                color = Navy950,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Ask Gemini",
                                tint = Navy950,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                if (!aiCoachAnswer.isNullOrBlank()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Navy850)
                            .border(1.dp, ElectricCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "GEMINI COACH INSIGHT",
                            color = ElectricCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp
                        )
                        Text(
                            text = aiCoachAnswer.replace("**", ""),
                            color = TextPrimary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }
        }

        if (report != null) {
            // Header Card with Grade & Week Label
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = report.weekLabel.uppercase(),
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Weekly Executive Grade",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${report.stats.totalTrades} Executions | ${TradeAnalyticsCalculator.formatCurrency(report.stats.netPnL)} P&L",
                                color = if (report.stats.netPnL >= 0) ProfitGreenLight else LossRedLight,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(BrandIndigo)
                                .border(2.dp, ElectricCyan, RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = report.grade,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            // Key Takeaways chips
            if (report.keyTakeaways.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "CORE COACHING TAKEAWAYS",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        report.keyTakeaways.forEach { takeaway ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Navy850)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = takeaway,
                                    color = TextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // Full Markdown Report Content Body
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardBackground)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        val reportLines = (report.aiReport ?: "").lines()
                        reportLines.forEach { line ->
                            when {
                                line.startsWith("### ") -> {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = line.removePrefix("### "),
                                        color = ElectricCyan,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                line.startsWith("**Weekly Grade:") -> {
                                    Text(
                                        text = line.replace("**", ""),
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                line.startsWith("---") -> {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        color = CardBorder
                                    )
                                }
                                line.startsWith("* ") -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text("•", color = BrandIndigoLight, fontSize = 13.sp)
                                        Text(
                                            text = line.removePrefix("* ").replace("**", ""),
                                            color = TextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                                line.matches(Regex("^\\d+\\..*")) -> {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = line.substringBefore("."),
                                            color = AccentAmber,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = line.substringAfter(". ").replace("**", ""),
                                            color = TextSecondary,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                                line.isNotBlank() -> {
                                    Text(
                                        text = line.replace("**", ""),
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } else {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Navy850)
                                .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Text(
                            text = "No Performance Audit Generated",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Workspace is clean. Tap 'Run AI Audit' above to evaluate your execution edge, psychological discipline, and trading expectancy.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }
                }
            }
        }
    }
}
