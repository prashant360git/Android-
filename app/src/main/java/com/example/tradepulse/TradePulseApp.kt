package com.example.tradepulse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tradepulse.ui.components.AppBottomNavigation
import com.example.tradepulse.ui.components.AppTopBar
import com.example.tradepulse.ui.components.Screen
import com.example.tradepulse.ui.dialogs.*
import com.example.tradepulse.ui.screens.*
import com.example.tradepulse.ui.theme.BrandIndigo
import com.example.tradepulse.ui.theme.Navy950
import com.example.tradepulse.ui.theme.TradePulseTheme
import com.example.tradepulse.viewmodel.AppDialog
import com.example.tradepulse.viewmodel.TradePulseViewModel

@Composable
fun TradePulseApp(
    viewModel: TradePulseViewModel = viewModel()
) {
    val currentTheme by viewModel.currentTheme.collectAsState()

    TradePulseTheme(theme = currentTheme) {
        var currentRoute by remember { mutableStateOf(Screen.Dashboard.route) }

        val accounts by viewModel.accounts.collectAsState()
        val selectedAccount by viewModel.selectedAccount.collectAsState()
        val trades by viewModel.filteredTrades.collectAsState()
        val stats by viewModel.stats.collectAsState()
        val daySummaries by viewModel.daySummaries.collectAsState()
        val emotionalBreakdown by viewModel.emotionalBreakdown.collectAsState()
        val setupBreakdown by viewModel.setupBreakdown.collectAsState()
        val mistakesBreakdown by viewModel.mistakesBreakdown.collectAsState()
        val playbooks by viewModel.playbooks.collectAsState()
        val backtestTrades by viewModel.backtestTrades.collectAsState()
        val weeklyReport by viewModel.weeklyReport.collectAsState()
        val isGeneratingReport by viewModel.isGeneratingReport.collectAsState()
        val isSyncingBroker by viewModel.isSyncingBroker.collectAsState()
        val brokers by viewModel.brokers.collectAsState()
        val activeDialog by viewModel.activeDialog.collectAsState()
        val dailyEntries by viewModel.dailyEntries.collectAsState()

        // Handle system back navigation
        if (currentRoute != Screen.Dashboard.route) {
            BackHandler {
                currentRoute = Screen.Dashboard.route
            }
        }

        val backgroundModifier = if (TradePulseTheme.colors.backgroundBrush != null) {
            Modifier.background(TradePulseTheme.colors.backgroundBrush!!)
        } else {
            Modifier.background(Navy950)
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .then(backgroundModifier),
            containerColor = Color.Transparent,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            topBar = {
                val title = when (currentRoute) {
                    Screen.Dashboard.route -> "Overview & KPIs"
                    Screen.Trades.route -> "Trade Journal"
                    Screen.Analytics.route -> "Execution Analytics"
                    Screen.Playbooks.route -> "Strategy Playbooks"
                    Screen.Backtest.route -> "Replay Testing"
                    Screen.Reports.route -> "Weekly AI Audit"
                    else -> "TradePulse"
                }
                AppTopBar(
                    currentTitle = title,
                    accounts = accounts,
                    selectedAccount = selectedAccount,
                    onSelectAccount = { viewModel.setSelectedAccount(it) },
                    onSyncClick = { viewModel.openDialog(AppDialog.BrokerSync) },
                    onThemeClick = { viewModel.openDialog(AppDialog.ThemeSelect) },
                    onAddTradeClick = { viewModel.openDialog(AppDialog.EditTrade(null)) }
                )
            },
            bottomBar = {
                AppBottomNavigation(
                    currentRoute = currentRoute,
                    onNavigate = { currentRoute = it }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.openDialog(AppDialog.EditTrade(null)) },
                    modifier = Modifier.testTag("global_fab_add_trade"),
                    containerColor = BrandIndigo,
                    contentColor = Color.White,
                    shape = CircleShape
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Log Trade")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentRoute) {
                    Screen.Dashboard.route -> {
                        DashboardScreen(
                            stats = stats,
                            trades = trades,
                            daySummaries = daySummaries,
                            onSelectDay = { viewModel.openDialog(AppDialog.DayDetail(it)) },
                            onEditTrade = { viewModel.openDialog(AppDialog.EditTrade(it)) },
                            onDeleteTrade = { viewModel.deleteTrade(it) },
                            onViewAllTrades = { currentRoute = Screen.Trades.route }
                        )
                    }
                    Screen.Trades.route -> {
                        TradeLogScreen(
                            trades = trades,
                            onEditTrade = { viewModel.openDialog(AppDialog.EditTrade(it)) },
                            onDeleteTrade = { viewModel.deleteTrade(it) },
                            onAddNewTrade = { viewModel.openDialog(AppDialog.EditTrade(null)) }
                        )
                    }
                    Screen.Analytics.route -> {
                        AnalyticsScreen(
                            stats = stats,
                            emotionalBreakdown = emotionalBreakdown,
                            setupBreakdown = setupBreakdown,
                            mistakesBreakdown = mistakesBreakdown
                        )
                    }
                    Screen.Playbooks.route -> {
                        PlaybooksScreen(
                            playbooks = playbooks,
                            onSelectPlaybook = { viewModel.openDialog(AppDialog.PlaybookDetail(it)) },
                            onAddPlaybook = { viewModel.openDialog(AppDialog.AddPlaybook) },
                            onLogTradeWithPlaybook = { pb ->
                                viewModel.openDialog(AppDialog.EditTrade(null, initialPlaybook = pb))
                            }
                        )
                    }
                    Screen.Backtest.route -> {
                        BacktestingScreen(
                            backtestTrades = backtestTrades,
                            onAddBacktestTrade = { viewModel.openDialog(AppDialog.AddBacktestTrade(null)) }
                        )
                    }
                    Screen.Reports.route -> {
                        WeeklyReportsScreen(
                            report = weeklyReport,
                            isGenerating = isGeneratingReport,
                            onGenerateReport = { viewModel.generateWeeklyReport() }
                        )
                    }
                }
            }
        }

        // Active Dialog Handlers
        when (val dialog = activeDialog) {
            is AppDialog.EditTrade -> {
                EditTradeDialog(
                    trade = dialog.trade,
                    defaultDate = dialog.defaultDate,
                    initialPlaybook = dialog.initialPlaybook,
                    accounts = accounts,
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.saveTrade(it) }
                )
            }
            is AppDialog.DayDetail -> {
                DayDetailBottomSheet(
                    daySummary = dialog.daySummary,
                    onDismiss = { viewModel.dismissDialog() },
                    onEditTrade = { viewModel.openDialog(AppDialog.EditTrade(it)) },
                    onDeleteTrade = { viewModel.deleteTrade(it) },
                    onAddTradeForDay = { date -> viewModel.openDialog(AppDialog.EditTrade(null, defaultDate = date)) },
                    onOpenDailyJournal = { date -> viewModel.openDialog(AppDialog.DailyJournal(date)) }
                )
            }
            is AppDialog.DailyJournal -> {
                val existing = dailyEntries.find { it.date == dialog.date }
                DailyJournalDialog(
                    date = dialog.date,
                    existingEntry = existing,
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.saveDailyJournalEntry(it) }
                )
            }
            is AppDialog.PlaybookDetail -> {
                PlaybookDetailDialog(
                    playbook = dialog.playbook,
                    onDismiss = { viewModel.dismissDialog() },
                    onUsePlaybook = { pb ->
                        viewModel.openDialog(AppDialog.EditTrade(null, initialPlaybook = pb))
                    }
                )
            }
            is AppDialog.AddPlaybook -> {
                AddPlaybookDialog(
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.savePlaybook(it) }
                )
            }
            is AppDialog.AddBacktestTrade -> {
                AddBacktestTradeDialog(
                    initialPlaybook = dialog.playbook,
                    playbooks = playbooks,
                    onDismiss = { viewModel.dismissDialog() },
                    onSave = { viewModel.saveBacktestTrade(it) }
                )
            }
            is AppDialog.BrokerSync -> {
                BrokerSyncDialog(
                    brokers = brokers,
                    isSyncing = isSyncingBroker,
                    onSyncBroker = { viewModel.syncBroker(it) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.ThemeSelect -> {
                ThemeSelectionDialog(
                    currentTheme = currentTheme,
                    onSelectTheme = { viewModel.setTheme(it) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            null -> Unit
        }
    }
}
