package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.data.model.DailyJournalEntry
import com.example.tradepulse.ui.theme.*

@Composable
fun DailyJournalDialog(
    date: String,
    existingEntry: DailyJournalEntry?,
    onDismiss: () -> Unit,
    onSave: (DailyJournalEntry) -> Unit
) {
    var preMarketPlan by remember { mutableStateOf(existingEntry?.preMarketPlan ?: "") }
    var marketCondition by remember { mutableStateOf(existingEntry?.marketCondition ?: "TRENDING_UP") }
    var mentalScore by remember { mutableStateOf(existingEntry?.mentalPreparednessScore ?: 5) }
    var sessionReview by remember { mutableStateOf(existingEntry?.sessionReview ?: "") }
    var lessonsLearned by remember { mutableStateOf(existingEntry?.lessonsLearned ?: "") }
    var overallRating by remember { mutableStateOf(existingEntry?.rating ?: 5) }

    val conditions = listOf(
        "TRENDING_UP" to "Bullish Trend",
        "TRENDING_DOWN" to "Bearish Trend",
        "CHOPPY_RANGE" to "Choppy Range",
        "HIGH_VOLATILITY" to "High Volatility",
        "BALANCED" to "Balanced Profile"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
            color = CardBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Journal: $date",
                            color = TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Psychological preparedness & post-session review",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Market Regime
                    Column {
                        Text("Session Market Condition", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            conditions.take(3).forEach { (code, label) ->
                                FilterChip(
                                    selected = marketCondition == code,
                                    onClick = { marketCondition = code },
                                    label = { Text(label, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = BrandIndigo,
                                        selectedLabelColor = Color.White,
                                        containerColor = Navy850,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }

                    // Mental Readiness
                    Column {
                        Text("Mental Readiness / Focus Level ($mentalScore / 5)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            (1..5).forEach { s ->
                                IconButton(onClick = { mentalScore = s }, modifier = Modifier.size(32.dp)) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (s <= mentalScore) AccentAmber else Navy700,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Pre-Market Plan
                    OutlinedTextField(
                        value = preMarketPlan,
                        onValueChange = { preMarketPlan = it },
                        label = { Text("Pre-Market Game Plan") },
                        placeholder = { Text("Key levels watched, news catalysts, risk parameters...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    // Session Review
                    OutlinedTextField(
                        value = sessionReview,
                        onValueChange = { sessionReview = it },
                        label = { Text("Post-Session Trade Review") },
                        placeholder = { Text("How well did you stick to your plan? Did you fight the trend?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    // Lessons Learned
                    OutlinedTextField(
                        value = lessonsLearned,
                        onValueChange = { lessonsLearned = it },
                        label = { Text("Key Lessons & Rules for Tomorrow") },
                        placeholder = { Text("Rules to enforce in next session...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(85.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            val entry = DailyJournalEntry(
                                id = existingEntry?.id ?: "entry-${System.currentTimeMillis()}",
                                date = date,
                                preMarketPlan = preMarketPlan.trim(),
                                marketCondition = marketCondition,
                                mentalPreparednessScore = mentalScore,
                                sessionReview = sessionReview.trim(),
                                lessonsLearned = lessonsLearned.trim(),
                                rating = overallRating
                            )
                            onSave(entry)
                        },
                        modifier = Modifier.testTag("save_daily_journal_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
                    ) {
                        Text("Save Entry", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
