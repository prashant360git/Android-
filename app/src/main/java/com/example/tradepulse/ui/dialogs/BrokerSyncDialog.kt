package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.data.model.BrokerConnection
import com.example.tradepulse.ui.theme.*

@Composable
fun BrokerSyncDialog(
    brokers: List<BrokerConnection>,
    accounts: List<String>,
    isGlobalAutoSync: Boolean,
    isSyncing: Boolean,
    onToggleGlobalAutoSync: (Boolean) -> Unit,
    onToggleBrokerAutoSync: (brokerId: String, enabled: Boolean) -> Unit,
    onSetSyncInterval: (brokerId: String, interval: String) -> Unit,
    onSetTargetAccount: (brokerId: String, account: String) -> Unit,
    onToggleBrokerConnection: (brokerId: String) -> Unit,
    onSyncBroker: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                .testTag("broker_sync_dialog"),
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
                        Text("Broker Auto-Synchronize", color = TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                        Text("Automated trade execution feed & settlement", color = TextSecondary, fontSize = 11.sp)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("broker_sync_close_button")) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Master Auto-Sync Switch
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isGlobalAutoSync) BrandIndigo.copy(alpha = 0.2f) else Navy850)
                        .border(1.dp, if (isGlobalAutoSync) BrandIndigo else CardBorder, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isGlobalAutoSync) ProfitGreenSoft else Navy700),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Autorenew,
                                    contentDescription = null,
                                    tint = if (isGlobalAutoSync) ProfitGreenLight else TextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Global Auto-Sync Engine",
                                        color = TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    if (isGlobalAutoSync) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(ProfitGreen)
                                        )
                                    }
                                }
                                Text(
                                    text = if (isGlobalAutoSync) "Polling connected brokers for new fills" else "Auto-sync paused",
                                    color = if (isGlobalAutoSync) ProfitGreenLight else TextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Switch(
                            checked = isGlobalAutoSync,
                            onCheckedChange = { onToggleGlobalAutoSync(it) },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ProfitGreen
                            ),
                            modifier = Modifier.testTag("toggle_global_autosync")
                        )
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 12.dp))

                // Brokers List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(brokers, key = { it.id }) { broker ->
                        val isConnected = broker.status == "CONNECTED"

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(Navy850)
                                .border(1.dp, if (isConnected) ElectricCyan.copy(alpha = 0.4f) else CardBorder, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                                .testTag("broker_item_${broker.id}")
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Navy700),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = broker.logo,
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = broker.name,
                                                    color = TextPrimary,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(if (isConnected) ProfitGreenSoft else Navy700)
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = if (isConnected) "CONNECTED" else "OFFLINE",
                                                        color = if (isConnected) ProfitGreenLight else TextSecondary,
                                                        fontSize = 9.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                            Text(
                                                text = if (isConnected) "Acct: ${broker.accountNumber ?: "Live"} • Last Sync: ${broker.lastSynced?.take(16)?.replace("T", " ") ?: "Active"}" else "Tap Connect to link API",
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = { onToggleBrokerConnection(broker.id) },
                                        shape = RoundedCornerShape(6.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isConnected) LossRedSoft else BrandIndigo,
                                            contentColor = if (isConnected) LossRedLight else Color.White
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.testTag("connect_broker_${broker.id}")
                                    ) {
                                        Text(if (isConnected) "Disconnect" else "Connect", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                // Auto-Sync Controls when Connected
                                if (isConnected) {
                                    HorizontalDivider(color = CardBorder.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 2.dp))

                                    // Auto-Sync Toggle for this broker
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Auto-Synchronize Trades",
                                            color = TextPrimary,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Switch(
                                            checked = broker.autoSyncEnabled,
                                            onCheckedChange = { onToggleBrokerAutoSync(broker.id, it) },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = BrandIndigo
                                            ),
                                            modifier = Modifier.testTag("toggle_broker_autosync_${broker.id}")
                                        )
                                    }

                                    // Interval Selector
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Sync Frequency:", color = TextSecondary, fontSize = 11.sp)
                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            listOf("5m", "15m", "1h", "eod").forEach { interval ->
                                                val isSelected = broker.syncInterval == interval
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(if (isSelected) BrandIndigo else Navy700)
                                                        .clickable { onSetSyncInterval(broker.id, interval) }
                                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                                ) {
                                                    Text(
                                                        text = if (interval == "eod") "4PM Close" else interval,
                                                        color = if (isSelected) Color.White else TextSecondary,
                                                        fontSize = 10.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Target Account Selector & Instant Sync
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        var accountDropdownOpen by remember { mutableStateOf(false) }

                                        Box {
                                            Surface(
                                                color = Navy700,
                                                shape = RoundedCornerShape(4.dp),
                                                modifier = Modifier.clickable { accountDropdownOpen = true }
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                ) {
                                                    Text(
                                                        text = "Into: ${broker.targetAccount}",
                                                        color = ElectricCyan,
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Medium
                                                    )
                                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                                                }
                                            }

                                            DropdownMenu(
                                                expanded = accountDropdownOpen,
                                                onDismissRequest = { accountDropdownOpen = false },
                                                modifier = Modifier.background(Navy850)
                                            ) {
                                                accounts.forEach { acc ->
                                                    DropdownMenuItem(
                                                        text = { Text(acc, color = TextPrimary, fontSize = 12.sp) },
                                                        onClick = {
                                                            onSetTargetAccount(broker.id, acc)
                                                            accountDropdownOpen = false
                                                        }
                                                    )
                                                }
                                            }
                                        }

                                        Button(
                                            onClick = { onSyncBroker(broker.id) },
                                            enabled = !isSyncing,
                                            colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                            modifier = Modifier.testTag("sync_now_${broker.id}")
                                        ) {
                                            if (isSyncing) {
                                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(12.dp), strokeWidth = 2.dp)
                                            } else {
                                                Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(12.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text("Sync Now", fontSize = 11.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Navy850)
                    ) {
                        Text("Done", color = TextPrimary)
                    }
                }
            }
        }
    }
}
