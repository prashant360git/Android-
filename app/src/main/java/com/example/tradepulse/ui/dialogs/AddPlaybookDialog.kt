package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.example.tradepulse.data.model.PlaybookCategory
import com.example.tradepulse.data.model.PlaybookStrategy
import com.example.tradepulse.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddPlaybookDialog(
    onDismiss: () -> Unit,
    onSave: (PlaybookStrategy) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(PlaybookCategory.BREAKOUT) }
    var description by remember { mutableStateOf("") }
    var timeframe by remember { mutableStateOf("5m Intraday") }
    var entryTrigger by remember { mutableStateOf("") }
    var stopLossRule by remember { mutableStateOf("") }
    var takeProfitRule by remember { mutableStateOf("") }
    var minRiskRewardStr by remember { mutableStateOf("2.0") }
    var checklistText by remember { mutableStateOf("") }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Create Strategy Playbook", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Define entry triggers, stop rules, and criteria checklist", color = TextSecondary, fontSize = 11.sp)
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Strategy Name") },
                        placeholder = { Text("e.g. VWAP Trend Day Continuation") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )

                    // Category Chips
                    Column {
                        Text("Category", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PlaybookCategory.values().take(3).forEach { cat ->
                                FilterChip(
                                    selected = category == cat,
                                    onClick = { category = cat },
                                    label = { Text(cat.name, fontSize = 10.sp) },
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

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Strategy Thesis & Description") },
                        modifier = Modifier.fillMaxWidth().height(75.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )

                    OutlinedTextField(
                        value = entryTrigger,
                        onValueChange = { entryTrigger = it },
                        label = { Text("Specific Entry Trigger") },
                        placeholder = { Text("e.g. Candle close outside 15m range with high RVOL") },
                        modifier = Modifier.fillMaxWidth().height(65.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = stopLossRule,
                            onValueChange = { stopLossRule = it },
                            label = { Text("Stop Loss Rule") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                        OutlinedTextField(
                            value = takeProfitRule,
                            onValueChange = { takeProfitRule = it },
                            label = { Text("Take Profit Rule") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                    }

                    OutlinedTextField(
                        value = checklistText,
                        onValueChange = { checklistText = it },
                        label = { Text("Rules Checklist (one per line)") },
                        placeholder = { Text("Rule 1\nRule 2\nRule 3") },
                        modifier = Modifier.fillMaxWidth().height(90.dp),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                    )
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                val rules = checklistText.lines().map { it.trim() }.filter { it.isNotBlank() }
                                val pb = PlaybookStrategy(
                                    id = "pb-${System.currentTimeMillis()}",
                                    title = title.trim(),
                                    category = category,
                                    description = description.trim(),
                                    entryTrigger = entryTrigger.trim(),
                                    stopLossRule = stopLossRule.trim(),
                                    takeProfitRule = takeProfitRule.trim(),
                                    minRiskReward = minRiskRewardStr.toDoubleOrNull() ?: 2.0,
                                    rulesChecklist = if (rules.isNotEmpty()) rules else listOf("Predetermined risk defined", "Volume confirmation"),
                                    createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date())
                                )
                                onSave(pb)
                            }
                        },
                        modifier = Modifier.testTag("save_new_playbook_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                        enabled = title.isNotBlank()
                    ) {
                        Text("Create Playbook", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
