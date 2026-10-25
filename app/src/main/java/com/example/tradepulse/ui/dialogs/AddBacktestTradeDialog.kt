package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
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
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.BacktestTrade
import com.example.tradepulse.data.model.PlaybookStrategy
import com.example.tradepulse.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddBacktestTradeDialog(
    initialPlaybook: PlaybookStrategy?,
    playbooks: List<PlaybookStrategy>,
    onDismiss: () -> Unit,
    onSave: (BacktestTrade) -> Unit
) {
    var symbol by remember { mutableStateOf("") }
    var direction by remember { mutableStateOf("BUY") }
    var timeframe by remember { mutableStateOf("5m") }
    var entryStr by remember { mutableStateOf("") }
    var stopLossStr by remember { mutableStateOf("") }
    var profitTargetStr by remember { mutableStateOf("") }
    var exitStr by remember { mutableStateOf("") }
    var quantityStr by remember { mutableStateOf("100") }
    var selectedPlaybook by remember { mutableStateOf(initialPlaybook ?: playbooks.firstOrNull()) }
    var notes by remember { mutableStateOf("") }

    // Dynamic checklist from the chosen playbook
    val rules = selectedPlaybook?.rulesChecklist ?: listOf("Trend aligned", "Stop loss defined", "Risk/Reward >= 2.0")
    var checkedRules by remember(selectedPlaybook) { mutableStateOf(rules.map { true }.toMutableStateList()) }

    val entry = entryStr.toDoubleOrNull() ?: 0.0
    val exit = exitStr.toDoubleOrNull() ?: 0.0
    val quantity = quantityStr.toDoubleOrNull() ?: 0.0
    val stopLoss = stopLossStr.toDoubleOrNull() ?: 0.0
    val profitTarget = profitTargetStr.toDoubleOrNull() ?: 0.0

    val calculatedPnL = remember(entry, exit, quantity, direction) {
        if (entry > 0 && exit > 0 && quantity > 0) {
            if (direction == "BUY") (exit - entry) * quantity else (entry - exit) * quantity
        } else 0.0
    }

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
                        Text("Log Backtest Replay", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Simulate historical setup execution with criteria checklist", color = TextSecondary, fontSize = 11.sp)
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
                    // Symbol & Direction
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = symbol,
                            onValueChange = { symbol = it.uppercase() },
                            label = { Text("Symbol") },
                            placeholder = { Text("e.g. NVDA") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                        Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Button(
                                onClick = { direction = "BUY" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (direction == "BUY") ProfitGreenSoft else Navy850,
                                    contentColor = if (direction == "BUY") ProfitGreenLight else TextSecondary
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("BUY", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            Button(
                                onClick = { direction = "SELL" },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (direction == "SELL") LossRedSoft else Navy850,
                                    contentColor = if (direction == "SELL") LossRedLight else TextSecondary
                                ),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) { Text("SELL", fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                        }
                    }

                    // Prices
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedTextField(
                            value = entryStr,
                            onValueChange = { entryStr = it },
                            label = { Text("Entry $") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                        OutlinedTextField(
                            value = stopLossStr,
                            onValueChange = { stopLossStr = it },
                            label = { Text("Stop $") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                        OutlinedTextField(
                            value = exitStr,
                            onValueChange = { exitStr = it },
                            label = { Text("Exit $") },
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary)
                        )
                    }

                    // Checklist evaluation
                    Text(
                        text = "PLAYBOOK CHECKLIST RULES VERIFICATION",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    rules.forEachIndexed { i, rule ->
                        val checked = checkedRules.getOrNull(i) ?: true
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Navy850)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { isChecked ->
                                    if (i < checkedRules.size) checkedRules[i] = isChecked
                                },
                                colors = CheckboxDefaults.colors(checkedColor = ElectricCyan)
                            )
                            Text(text = rule, color = TextPrimary, fontSize = 11.sp)
                        }
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Replay Observations") },
                        modifier = Modifier.fillMaxWidth().height(65.dp),
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
                            if (symbol.isNotBlank() && entry > 0 && exit > 0) {
                                val passedCount = checkedRules.count { it }
                                val totalCount = checkedRules.size
                                val adherence = if (totalCount > 0) (passedCount.toDouble() / totalCount * 100.0) else 100.0

                                val bt = BacktestTrade(
                                    id = "bt-${System.currentTimeMillis()}",
                                    symbol = symbol.trim(),
                                    direction = direction,
                                    timeframe = timeframe,
                                    quantity = quantity,
                                    openTime = "09:45:00",
                                    closeTime = "10:30:00",
                                    entry = entry,
                                    stopLoss = stopLoss,
                                    profitTarget = profitTarget,
                                    exit = exit,
                                    realizedPnL = calculatedPnL,
                                    rMultiple = if (calculatedPnL > 0) 2.4 else -1.0,
                                    status = "Closed",
                                    playbookId = selectedPlaybook?.id,
                                    playbookTitle = selectedPlaybook?.title,
                                    checklistPassedCount = passedCount,
                                    checklistTotalCount = totalCount,
                                    ruleAdherencePercent = adherence,
                                    notes = notes.trim(),
                                    createdAt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).format(Date())
                                )
                                onSave(bt)
                            }
                        },
                        modifier = Modifier.testTag("save_backtest_trade_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                        enabled = symbol.isNotBlank() && entry > 0 && exit > 0
                    ) {
                        Text("Log Replay", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
