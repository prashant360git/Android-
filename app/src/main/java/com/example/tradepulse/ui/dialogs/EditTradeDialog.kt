package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.*
import com.example.tradepulse.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTradeDialog(
    trade: Trade?,
    defaultDate: String?,
    initialPlaybook: PlaybookStrategy?,
    accounts: List<String>,
    onDismiss: () -> Unit,
    onSave: (Trade) -> Unit
) {
    var symbol by remember { mutableStateOf(trade?.symbol ?: "") }
    var side by remember { mutableStateOf(trade?.side ?: TradeSide.LONG) }
    var assetClass by remember { mutableStateOf(trade?.assetClass ?: AssetClass.STOCKS) }
    var entryPriceStr by remember { mutableStateOf(trade?.entryPrice?.toString() ?: "") }
    var exitPriceStr by remember { mutableStateOf(trade?.exitPrice?.toString() ?: "") }
    var quantityStr by remember { mutableStateOf(trade?.quantity?.toString() ?: "100") }
    var commissionStr by remember { mutableStateOf(trade?.commission?.toString() ?: "2.0") }
    var setup by remember { mutableStateOf(trade?.setup ?: initialPlaybook?.title ?: "Opening Drive Breakout") }
    var preEmotion by remember { mutableStateOf(trade?.preEmotion ?: PreTradeEmotion.CALM) }
    var postEmotion by remember { mutableStateOf(trade?.postEmotion ?: PostTradeEmotion.SATISFIED) }
    var disciplineScore by remember { mutableStateOf(trade?.disciplineScore ?: 5) }
    var followedPlan by remember { mutableStateOf(trade?.followedPlan ?: true) }
    var selectedMistakes by remember { mutableStateOf(trade?.mistakes?.toSet() ?: emptySet()) }
    var notes by remember { mutableStateOf(trade?.notes ?: "") }
    var account by remember { mutableStateOf(trade?.account ?: accounts.firstOrNull() ?: "Interactive Brokers (Main)") }

    val entryPrice = entryPriceStr.toDoubleOrNull() ?: 0.0
    val exitPrice = exitPriceStr.toDoubleOrNull() ?: 0.0
    val quantity = quantityStr.toDoubleOrNull() ?: 0.0
    val commission = commissionStr.toDoubleOrNull() ?: 0.0

    // Auto-calculate Net P&L and %
    val calculatedNetPnL = remember(entryPrice, exitPrice, quantity, commission, side) {
        if (entryPrice > 0 && exitPrice > 0 && quantity > 0) {
            val gross = if (side == TradeSide.LONG) {
                (exitPrice - entryPrice) * quantity
            } else {
                (entryPrice - exitPrice) * quantity
            }
            gross - commission
        } else {
            0.0
        }
    }

    val calculatedPct = remember(entryPrice, exitPrice, side) {
        if (entryPrice > 0 && exitPrice > 0) {
            if (side == TradeSide.LONG) {
                ((exitPrice - entryPrice) / entryPrice) * 100.0
            } else {
                ((entryPrice - exitPrice) / entryPrice) * 100.0
            }
        } else {
            0.0
        }
    }

    val commonMistakes = listOf(
        "Chased Entry",
        "Revenge Trade",
        "Oversized Position",
        "Moved Stop Loss",
        "Lunch Lull Chop",
        "Early Exit",
        "FOMO Entry",
        "Hesitation"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.9f)
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
                            text = if (trade == null) "Log New Trade" else "Edit Trade: ${trade.symbol}",
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Record execution parameters, emotional state, and discipline score",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Form Fields
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Symbol & Side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = symbol,
                            onValueChange = { symbol = it.uppercase() },
                            label = { Text("Ticker Symbol") },
                            placeholder = { Text("e.g. NVDA, SPY") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("trade_symbol_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        // Long / Short selector
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Direction", color = TextSecondary, fontSize = 11.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Button(
                                    onClick = { side = TradeSide.LONG },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (side == TradeSide.LONG) ProfitGreenSoft else Navy850,
                                        contentColor = if (side == TradeSide.LONG) ProfitGreenLight else TextSecondary
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("LONG", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                                Button(
                                    onClick = { side = TradeSide.SHORT },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (side == TradeSide.SHORT) LossRedSoft else Navy850,
                                        contentColor = if (side == TradeSide.SHORT) LossRedLight else TextSecondary
                                    ),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("SHORT", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Prices & Quantity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = entryPriceStr,
                            onValueChange = { entryPriceStr = it },
                            label = { Text("Entry $") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("trade_entry_price_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = exitPriceStr,
                            onValueChange = { exitPriceStr = it },
                            label = { Text("Exit $") },
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("trade_exit_price_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = quantityStr,
                            onValueChange = { quantityStr = it },
                            label = { Text("Qty") },
                            singleLine = true,
                            modifier = Modifier.weight(0.9f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )
                    }

                    // Real-time P&L preview card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (calculatedNetPnL >= 0) ProfitGreenSoft else LossRedSoft)
                            .border(
                                1.dp,
                                if (calculatedNetPnL >= 0) ProfitGreen.copy(alpha = 0.5f) else LossRed.copy(alpha = 0.5f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Net P&L",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${TradeAnalyticsCalculator.formatCurrency(calculatedNetPnL)} (${TradeAnalyticsCalculator.formatPercent(calculatedPct)})",
                                color = if (calculatedNetPnL >= 0) ProfitGreenLight else LossRedLight,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Setup Strategy Field
                    OutlinedTextField(
                        value = setup,
                        onValueChange = { setup = it },
                        label = { Text("Strategy / Setup") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    // Pre-Trade Emotion
                    Column {
                        Text("Pre-Trade Emotional State", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PreTradeEmotion.values().forEach { em ->
                                FilterChip(
                                    selected = preEmotion == em,
                                    onClick = { preEmotion = em },
                                    label = { Text(em.name, fontSize = 10.sp) },
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

                    // Discipline Rating (1 to 5 Stars)
                    Column {
                        Text("Discipline & Plan Execution: $disciplineScore / 5", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            (1..5).forEach { star ->
                                IconButton(
                                    onClick = { disciplineScore = star },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "$star stars",
                                        tint = if (star <= disciplineScore) AccentAmber else Navy700,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Behavioral Mistakes Tags
                    Column {
                        Text("Mistakes (if any)", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            commonMistakes.forEach { m ->
                                val isSelected = selectedMistakes.contains(m)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        selectedMistakes = if (isSelected) {
                                            selectedMistakes - m
                                        } else {
                                            selectedMistakes + m
                                        }
                                    },
                                    label = { Text(m, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = LossRedSoft,
                                        selectedLabelColor = LossRedLight,
                                        containerColor = Navy850,
                                        labelColor = TextSecondary
                                    )
                                )
                            }
                        }
                    }

                    // Notes
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Execution Notes & Lessons") },
                        placeholder = { Text("What did order flow look like? What prompted the exit?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Action Buttons
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
                            if (symbol.isNotBlank() && entryPrice > 0 && exitPrice > 0) {
                                val now = defaultDate?.let { "${it}T10:00:00.000Z" } ?: SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                    Locale.US
                                ).apply {
                                    timeZone = TimeZone.getTimeZone("UTC")
                                }.format(Date())

                                val updatedTrade = Trade(
                                    id = trade?.id ?: "tr-${System.currentTimeMillis()}",
                                    symbol = symbol.trim(),
                                    side = side,
                                    assetClass = assetClass,
                                    entryDate = trade?.entryDate ?: now,
                                    exitDate = trade?.exitDate ?: now,
                                    entryPrice = entryPrice,
                                    exitPrice = exitPrice,
                                    quantity = quantity,
                                    commission = commission,
                                    netPnL = (calculatedNetPnL * 100.0).toInt() / 100.0,
                                    pnlPercent = (calculatedPct * 100.0).toInt() / 100.0,
                                    rMultiple = if (trade?.rMultiple != null && trade.rMultiple != 0.0) trade.rMultiple else if (calculatedNetPnL > 0) 2.0 else -1.0,
                                    setup = setup.trim(),
                                    preEmotion = preEmotion,
                                    postEmotion = postEmotion,
                                    disciplineScore = disciplineScore,
                                    followedPlan = followedPlan,
                                    mistakes = selectedMistakes.toList(),
                                    notes = notes.trim(),
                                    account = account,
                                    playbookId = initialPlaybook?.id ?: trade?.playbookId,
                                    playbookTitle = initialPlaybook?.title ?: trade?.playbookTitle
                                )
                                onSave(updatedTrade)
                            }
                        },
                        modifier = Modifier.testTag("save_trade_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                        enabled = symbol.isNotBlank() && entryPrice > 0 && exitPrice > 0
                    ) {
                        Text("Save Trade", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
