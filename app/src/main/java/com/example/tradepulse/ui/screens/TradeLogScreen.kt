package com.example.tradepulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.tradepulse.data.model.AssetClass
import com.example.tradepulse.data.model.Trade
import com.example.tradepulse.data.model.TradeSide
import com.example.tradepulse.ui.components.TradeItemCard
import com.example.tradepulse.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradeLogScreen(
    trades: List<Trade>,
    onEditTrade: (Trade) -> Unit,
    onDeleteTrade: (String) -> Unit,
    onRemoveAllTrades: () -> Unit,
    onAddNewTrade: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedSide by remember { mutableStateOf<TradeSide?>(null) }
    var selectedAssetClass by remember { mutableStateOf<AssetClass?>(null) }

    val filteredTrades = remember(trades, searchQuery, selectedSide, selectedAssetClass) {
        trades.filter { t ->
            val matchQuery = searchQuery.isBlank() ||
                    t.symbol.contains(searchQuery.trim(), ignoreCase = true) ||
                    t.setup.contains(searchQuery.trim(), ignoreCase = true) ||
                    t.notes.contains(searchQuery.trim(), ignoreCase = true)
            val matchSide = selectedSide == null || t.side == selectedSide
            val matchAsset = selectedAssetClass == null || t.assetClass == selectedAssetClass
            matchQuery && matchSide && matchAsset
        }.sortedByDescending { it.entryDate }
    }

    val totalPnL = remember(filteredTrades) { filteredTrades.sumOf { it.netPnL } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("trade_log_screen")
    ) {
        // Search & Filters Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy950)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_trade_input"),
                placeholder = { Text("Search by ticker, setup, notes...", color = TextTertiary, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandIndigo,
                    unfocusedBorderColor = CardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Side Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = selectedSide == null,
                    onClick = { selectedSide = null },
                    label = { Text("All Sides", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = BrandIndigo,
                        selectedLabelColor = Color.White,
                        containerColor = Navy850,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = selectedSide == TradeSide.LONG,
                    onClick = { selectedSide = if (selectedSide == TradeSide.LONG) null else TradeSide.LONG },
                    label = { Text("Longs Only", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ProfitGreenSoft,
                        selectedLabelColor = ProfitGreenLight,
                        containerColor = Navy850,
                        labelColor = TextSecondary
                    )
                )

                FilterChip(
                    selected = selectedSide == TradeSide.SHORT,
                    onClick = { selectedSide = if (selectedSide == TradeSide.SHORT) null else TradeSide.SHORT },
                    label = { Text("Shorts Only", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = LossRedSoft,
                        selectedLabelColor = LossRedLight,
                        containerColor = Navy850,
                        labelColor = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Asset classes
                AssetClass.values().forEach { ac ->
                    FilterChip(
                        selected = selectedAssetClass == ac,
                        onClick = { selectedAssetClass = if (selectedAssetClass == ac) null else ac },
                        label = { Text(ac.name, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricCyanSoft,
                            selectedLabelColor = ElectricCyan,
                            containerColor = Navy850,
                            labelColor = TextSecondary
                        )
                    )
                }
            }

            // Results count, Net P&L Summary Bar, and Remove All Entries Action
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${filteredTrades.size} Trades found",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )

                    if (trades.isNotEmpty()) {
                        Surface(
                            color = LossRedSoft,
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LossRed.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { onRemoveAllTrades() }
                                .testTag("remove_all_entries_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteSweep,
                                    contentDescription = "Remove All Entries",
                                    tint = LossRedLight,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Remove All",
                                    color = LossRedLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Filtered P&L:",
                        color = TextTertiary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = TradeAnalyticsCalculator.formatCurrency(totalPnL),
                        color = if (totalPnL >= 0) ProfitGreen else LossRed,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Trade List
        if (filteredTrades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No trades match the current filter",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onAddNewTrade,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo)
                    ) {
                        Text("Log a New Trade", color = Color.White)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredTrades, key = { it.id }) { trade ->
                    TradeItemCard(
                        trade = trade,
                        onEdit = { onEditTrade(trade) },
                        onDelete = { onDeleteTrade(trade.id) }
                    )
                }
            }
        }
    }
}
