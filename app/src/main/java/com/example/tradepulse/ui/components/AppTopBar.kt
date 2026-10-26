package com.example.tradepulse.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
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
import com.example.tradepulse.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentTitle: String,
    accounts: List<String>,
    selectedAccount: String,
    userProfile: com.example.tradepulse.data.model.UserProfile? = null,
    isAutoSyncActive: Boolean = false,
    onSelectAccount: (String) -> Unit,
    onRemoveAccount: (String) -> Unit,
    onAddAccountClick: () -> Unit,
    onSyncClick: () -> Unit,
    onThemeClick: () -> Unit,
    onProfileClick: () -> Unit = {},
    onGoogleDriveClick: () -> Unit = {},
    onUserMenuClick: () -> Unit = {},
    onAddTradeClick: () -> Unit,
    onCleanDataClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        color = if (TradePulseTheme.colors.isGlass) Color(0x400A1024) else Navy950,
        tonalElevation = if (TradePulseTheme.colors.isGlass) 0.dp else 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
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
                    // 3 Vertical Lines in Cube Shape for User Menu
                    Surface(
                        onClick = onUserMenuClick,
                        modifier = Modifier
                            .testTag("cube_user_menu_button")
                            .size(38.dp),
                        shape = RoundedCornerShape(9.dp),
                        color = if (userProfile != null) (if (userProfile.authProvider == "GOOGLE") Color(0xFF4285F4) else BrandIndigo) else BrandIndigo,
                        border = BorderStroke(1.dp, CardBorder)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.size(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(Color.White)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(ElectricCyan)
                                )
                                Box(
                                    modifier = Modifier
                                        .width(2.5.dp)
                                        .height(14.dp)
                                        .clip(RoundedCornerShape(1.dp))
                                        .background(Color.White)
                                )
                            }
                        }
                    }

                    Column {
                        Text(
                            text = "TradePulse",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            text = currentTitle,
                            color = ElectricCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Google Drive Cloud Save Button
                    IconButton(
                        onClick = onGoogleDriveClick,
                        modifier = Modifier
                            .testTag("top_bar_drive_button")
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Navy850)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudSync,
                            contentDescription = "Google Drive Cloud Save",
                            tint = if (userProfile?.isDriveSyncEnabled == true) Color(0xFF4285F4) else TextSecondary,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Broker Auto-Sync Button
                    IconButton(
                        onClick = onSyncClick,
                        modifier = Modifier
                            .testTag("top_bar_sync_button")
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Navy850)
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Broker Auto-Sync",
                                tint = if (isAutoSyncActive) ProfitGreenLight else TextSecondary,
                                modifier = Modifier.size(19.dp)
                            )
                            if (isAutoSyncActive) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(androidx.compose.foundation.shape.CircleShape)
                                        .background(ProfitGreen)
                                )
                            }
                        }
                    }

                    // Theme Switcher Button
                    IconButton(
                        onClick = onThemeClick,
                        modifier = Modifier
                            .testTag("top_bar_theme_button")
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Navy850)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme Options",
                            tint = ElectricCyan,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // User Profile / Sign In Button
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .testTag("top_bar_profile_button")
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (userProfile != null) (if (userProfile.authProvider == "GOOGLE") Color(0xFF4285F4) else BrandIndigo) else Navy850)
                    ) {
                        if (userProfile != null) {
                            Text(
                                text = userProfile.displayName.take(2).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Sign In / Sign Up",
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    FilledTonalButton(
                        onClick = onAddTradeClick,
                        modifier = Modifier
                            .testTag("top_bar_add_trade_button")
                            .height(36.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = BrandIndigo,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Trade",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Log",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Account Filter Chip Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Account:",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Box {
                    Surface(
                        color = Navy850,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, CardBorder),
                        modifier = Modifier
                            .clickable { expanded = true }
                            .testTag("account_filter_dropdown")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (selectedAccount == "ALL") "All Accounts" else selectedAccount,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Navy850)
                            .widthIn(min = 220.dp)
                    ) {
                        DropdownMenuItem(
                            text = { Text("All Accounts", color = TextPrimary, fontSize = 13.sp) },
                            onClick = {
                                onSelectAccount("ALL")
                                expanded = false
                            }
                        )
                        accounts.forEach { acc ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = acc,
                                        color = if (acc == selectedAccount) ElectricCyan else TextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = if (acc == selectedAccount) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        onClick = {
                                            expanded = false
                                            onRemoveAccount(acc)
                                        },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("delete_account_item_${acc}")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete $acc",
                                            tint = LossRedLight,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                },
                                onClick = {
                                    onSelectAccount(acc)
                                    expanded = false
                                }
                            )
                        }

                        HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 4.dp))

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "+ Add New Account",
                                    color = ElectricCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            onClick = {
                                expanded = false
                                onAddAccountClick()
                            }
                        )

                        HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 4.dp))

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Google Drive Cloud Save",
                                    color = ElectricCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CloudSync,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            onClick = {
                                expanded = false
                                onGoogleDriveClick()
                            }
                        )

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = if (userProfile != null) "Account Profile (${userProfile.displayName})" else "Sign In / Sign Up",
                                    color = TextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            onClick = {
                                expanded = false
                                onProfileClick()
                            }
                        )

                        HorizontalDivider(color = CardBorder, modifier = Modifier.padding(vertical = 4.dp))

                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "Clean Workspace Data",
                                    color = LossRedLight,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CleaningServices,
                                    contentDescription = null,
                                    tint = LossRedLight,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            modifier = Modifier.testTag("menu_clean_workspace_data"),
                            onClick = {
                                expanded = false
                                onCleanDataClick()
                            }
                        )
                    }
                }

                // Dedicated Remove Button for the Selected Account
                if (selectedAccount != "ALL") {
                    Surface(
                        color = LossRedSoft,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, LossRed.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clickable { onRemoveAccount(selectedAccount) }
                            .testTag("remove_selected_account_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Remove Account",
                                tint = LossRedLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Remove Account",
                                color = LossRedLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Add Account Quick Button
                Surface(
                    color = Navy850,
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .clickable { onAddAccountClick() }
                        .testTag("top_bar_add_account_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Account",
                            tint = TextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Add",
                            color = TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
