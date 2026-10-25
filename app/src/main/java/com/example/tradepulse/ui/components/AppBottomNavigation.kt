package com.example.tradepulse.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tradepulse.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Trades : Screen("trades", "Trades", Icons.Default.ReceiptLong)
    object Analytics : Screen("analytics", "Analytics", Icons.Default.QueryStats)
    object Playbooks : Screen("playbooks", "Playbooks", Icons.AutoMirrored.Filled.MenuBook)
    object Backtest : Screen("backtest", "Backtest", Icons.Default.Science)
    object Reports : Screen("reports", "AI Audit", Icons.Default.AutoAwesome)
}

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Trades,
        Screen.Analytics,
        Screen.Playbooks,
        Screen.Backtest,
        Screen.Reports
    )

    NavigationBar(
        containerColor = if (TradePulseTheme.colors.isGlass) Color(0x550A1024) else Navy950,
        contentColor = TextSecondary,
        tonalElevation = if (TradePulseTheme.colors.isGlass) 0.dp else 8.dp
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                modifier = Modifier.testTag("nav_item_${screen.route}"),
                selected = selected,
                onClick = { onNavigate(screen.route) },
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ElectricCyan,
                    selectedTextColor = ElectricCyan,
                    unselectedIconColor = TextTertiary,
                    unselectedTextColor = TextTertiary,
                    indicatorColor = Navy850
                )
            )
        }
    }
}
