package com.example.tradepulse.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.tradepulse.data.model.UserProfile
import com.example.tradepulse.ui.theme.*

@Composable
fun UserMenuDialog(
    profile: UserProfile?,
    onOpenSettings: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenHelp: () -> Unit,
    onOpenBrokerSync: () -> Unit,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    onCleanData: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(18.dp))
                .border(1.dp, CardBorder, RoundedCornerShape(18.dp))
                .testTag("user_menu_dialog"),
            color = CardBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header with Cube Icon & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 3 Dots in Cube Shape
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(BrandIndigo),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.height(18.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(ElectricCyan)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(4.dp)
                                        .clip(CircleShape)
                                        .background(Color.White)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "User Menu & Controls",
                                color = TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Settings, account, backup & guides",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("user_menu_close_button")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // User Profile & Login / Logout Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Navy850)
                        .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                        .testTag("user_menu_profile_section")
                ) {
                    if (profile != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .clip(CircleShape)
                                        .background(if (profile.authProvider == "GOOGLE") Color(0xFF4285F4) else BrandIndigo),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = profile.displayName.take(2).uppercase(),
                                        color = Color.White,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = profile.displayName,
                                            color = TextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(if (profile.authProvider == "GOOGLE") ElectricCyanSoft else Navy700)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (profile.authProvider == "GOOGLE") "Google" else "Email",
                                                color = if (profile.authProvider == "GOOGLE") ElectricCyan else TextSecondary,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                    Text(
                                        text = profile.email,
                                        color = TextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedButton(
                                    onClick = onSignIn,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary)
                                ) {
                                    Text("Switch User", fontSize = 11.sp)
                                }

                                Button(
                                    onClick = onSignOut,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("user_menu_logout_button"),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(vertical = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = LossRedSoft,
                                        contentColor = LossRedLight
                                    )
                                ) {
                                    Icon(imageVector = Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Log Out", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Navy700),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonOutline,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text("Not Signed In", color = TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("Sign in to sync with Google Drive", color = TextSecondary, fontSize = 11.sp)
                                }
                            }

                            Button(
                                onClick = onSignIn,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandIndigo),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("user_menu_login_button")
                            ) {
                                Text("Sign In", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Menu Options List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // 1. Settings (with Theme option inside!)
                    UserMenuItemCard(
                        title = "Settings",
                        subtitle = "Theme options (Glass effect, Obsidian, etc.) & preferences",
                        icon = Icons.Default.Settings,
                        iconTint = ElectricCyan,
                        badge = "Theme inside",
                        badgeColor = ElectricCyanSoft,
                        badgeTextColor = ElectricCyan,
                        testTag = "user_menu_settings",
                        onClick = onOpenSettings
                    )

                    // 2. Data Backup
                    UserMenuItemCard(
                        title = "Data Backup",
                        subtitle = "Google Drive cloud save, restore & auto-save",
                        icon = Icons.Default.CloudSync,
                        iconTint = Color(0xFF4285F4),
                        badge = if (profile?.isDriveSyncEnabled == true) "Auto-Save ON" else "Cloud Safe",
                        badgeColor = if (profile?.isDriveSyncEnabled == true) ProfitGreenSoft else Navy700,
                        badgeTextColor = if (profile?.isDriveSyncEnabled == true) ProfitGreenLight else TextSecondary,
                        testTag = "user_menu_backup",
                        onClick = onOpenBackup
                    )

                    // 3. Broker Auto-Synchronize
                    UserMenuItemCard(
                        title = "Broker Auto-Synchronize",
                        subtitle = "Automated execution polling & multi-broker feed",
                        icon = Icons.Default.Autorenew,
                        iconTint = ProfitGreenLight,
                        badge = "Active",
                        badgeColor = ProfitGreenSoft,
                        badgeTextColor = ProfitGreenLight,
                        testTag = "user_menu_broker_sync",
                        onClick = onOpenBrokerSync
                    )

                    // 4. Help & Trader Guides
                    UserMenuItemCard(
                        title = "Help & Guidelines",
                        subtitle = "Playbook rules, 2-loss stop protocol, and user manual",
                        icon = Icons.Default.HelpOutline,
                        iconTint = AccentAmber,
                        badge = "FAQ & Rules",
                        badgeColor = AccentAmber.copy(alpha = 0.2f),
                        badgeTextColor = AccentAmber,
                        testTag = "user_menu_help",
                        onClick = onOpenHelp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Clean Workspace Reset Option at Bottom
                Surface(
                    onClick = onCleanData,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_menu_clean_data"),
                    shape = RoundedCornerShape(8.dp),
                    color = LossRedSoft,
                    border = androidx.compose.foundation.BorderStroke(1.dp, LossRed.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CleaningServices, contentDescription = null, tint = LossRedLight, modifier = Modifier.size(16.dp))
                            Text("Reset Workspace to Clean Slate", color = LossRedLight, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = LossRedLight, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun UserMenuItemCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    badge: String? = null,
    badgeColor: Color = Navy700,
    badgeTextColor: Color = TextSecondary,
    testTag: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Navy850)
            .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            color = TextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (badge != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(badgeColor)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = badge,
                                    color = badgeTextColor,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = subtitle,
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
