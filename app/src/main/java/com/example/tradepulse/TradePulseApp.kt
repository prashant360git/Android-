package com.example.tradepulse

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tradepulse.ui.components.AppBottomNavigation
import com.example.tradepulse.ui.components.AppTopBar
import com.example.tradepulse.ui.components.Screen
import com.example.tradepulse.ui.dialogs.*
import com.example.tradepulse.ui.screens.*
import com.example.tradepulse.ui.theme.*
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
        val aiErrorMessage by viewModel.aiErrorMessage.collectAsState()
        val aiCoachAnswer by viewModel.aiCoachAnswer.collectAsState()
        val isAskingCoach by viewModel.isAskingCoach.collectAsState()
        val isSyncingBroker by viewModel.isSyncingBroker.collectAsState()
        val brokers by viewModel.brokers.collectAsState()
        val activeDialog by viewModel.activeDialog.collectAsState()
        val dailyEntries by viewModel.dailyEntries.collectAsState()
        val userProfile by viewModel.userProfile.collectAsState()
        val isDriveSaving by viewModel.isDriveSaving.collectAsState()
        val lastDriveBackup by viewModel.lastDriveBackup.collectAsState()
        val driveStatusMessage by viewModel.driveStatusMessage.collectAsState()
        val isGlobalAutoSyncEnabled by viewModel.isGlobalAutoSyncEnabled.collectAsState()
        val autoSyncNotification by viewModel.autoSyncNotification.collectAsState()

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
                val isAutoSyncActive = isGlobalAutoSyncEnabled && brokers.any { it.status == "CONNECTED" && it.autoSyncEnabled }
                AppTopBar(
                    currentTitle = title,
                    accounts = accounts,
                    selectedAccount = selectedAccount,
                    userProfile = userProfile,
                    isAutoSyncActive = isAutoSyncActive,
                    onSelectAccount = { viewModel.setSelectedAccount(it) },
                    onRemoveAccount = { viewModel.openDialog(AppDialog.ConfirmDeleteAccount(it)) },
                    onAddAccountClick = { viewModel.openDialog(AppDialog.AddAccount) },
                    onSyncClick = { viewModel.openDialog(AppDialog.BrokerSync) },
                    onThemeClick = { viewModel.openDialog(AppDialog.ThemeSelect) },
                    onProfileClick = {
                        if (userProfile != null) viewModel.openDialog(AppDialog.UserProfileDialog) else viewModel.openDialog(AppDialog.AuthDialog)
                    },
                    onGoogleDriveClick = { viewModel.openDialog(AppDialog.GoogleDriveSync) },
                    onUserMenuClick = { viewModel.openDialog(AppDialog.UserMenuDialog) },
                    onAddTradeClick = { viewModel.openDialog(AppDialog.EditTrade(null)) },
                    onCleanDataClick = { viewModel.openDialog(AppDialog.ConfirmCleanAllData) }
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
                            onRemoveAllTrades = { viewModel.openDialog(AppDialog.ConfirmRemoveAllTrades(selectedAccount)) },
                            onViewAllTrades = { currentRoute = Screen.Trades.route }
                        )
                    }
                    Screen.Trades.route -> {
                        TradeLogScreen(
                            trades = trades,
                            onEditTrade = { viewModel.openDialog(AppDialog.EditTrade(it)) },
                            onDeleteTrade = { viewModel.deleteTrade(it) },
                            onRemoveAllTrades = { viewModel.openDialog(AppDialog.ConfirmRemoveAllTrades(selectedAccount)) },
                            onAddNewTrade = { viewModel.openDialog(AppDialog.EditTrade(null)) }
                        )
                    }
                    Screen.Analytics.route -> {
                        AnalyticsScreen(
                            stats = stats,
                            trades = trades,
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
                            onGenerateReport = { viewModel.generateWeeklyReport() },
                            aiErrorMessage = aiErrorMessage,
                            aiCoachAnswer = aiCoachAnswer,
                            isAskingCoach = isAskingCoach,
                            onAskCoach = { viewModel.askGeminiCoach(it) },
                            onDismissError = { viewModel.clearAiError() }
                        )
                    }
                }

                // Floating Notification Banner for Auto-Sync & Google Drive Status
                val notification = autoSyncNotification ?: driveStatusMessage
                if (notification != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp, start = 16.dp, end = 16.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, if (notification.contains("Auto-synced") || notification.contains("Saved")) ProfitGreen else ElectricCyan, RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.clearAutoSyncNotification()
                                viewModel.clearDriveStatusMessage()
                            },
                        color = Navy850,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = if (notification.contains("Auto-synced")) Icons.Default.Autorenew else Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = if (notification.contains("Auto-synced")) ProfitGreenLight else ElectricCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = notification,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dismiss",
                                tint = TextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
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
                    onSave = { viewModel.saveTrade(it) },
                    onDelete = { viewModel.deleteTrade(it) }
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
                    accounts = accounts,
                    isGlobalAutoSync = isGlobalAutoSyncEnabled,
                    isSyncing = isSyncingBroker,
                    onToggleGlobalAutoSync = { viewModel.toggleGlobalAutoSync(it) },
                    onToggleBrokerAutoSync = { brokerId, enabled -> viewModel.toggleBrokerAutoSync(brokerId, enabled) },
                    onSetSyncInterval = { brokerId, interval -> viewModel.setBrokerSyncInterval(brokerId, interval) },
                    onSetTargetAccount = { brokerId, account -> viewModel.setBrokerTargetAccount(brokerId, account) },
                    onToggleBrokerConnection = { viewModel.toggleBrokerConnection(it) },
                    onSyncBroker = { viewModel.syncBroker(it) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.AuthDialog -> {
                AuthDialog(
                    onSignInWithGoogle = { viewModel.signInWithGoogle() },
                    onSignInWithEmail = { email, password -> viewModel.signInWithEmail(email, password) },
                    onSignUpWithEmail = { name, email, password -> viewModel.signUpWithEmail(name, email, password) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.UserProfileDialog -> {
                UserProfileDialog(
                    profile = userProfile,
                    onOpenDriveSync = { viewModel.openDialog(AppDialog.GoogleDriveSync) },
                    onOpenBrokerSync = { viewModel.openDialog(AppDialog.BrokerSync) },
                    onSignOut = { viewModel.signOut() },
                    onSwitchAccount = { viewModel.openDialog(AppDialog.AuthDialog) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.GoogleDriveSync -> {
                GoogleDriveSyncDialog(
                    userProfile = userProfile,
                    lastBackup = lastDriveBackup,
                    tradesCount = trades.size,
                    accountsCount = accounts.size,
                    isSaving = isDriveSaving,
                    statusMessage = driveStatusMessage,
                    onSaveToDrive = { viewModel.backupToGoogleDrive(silent = false) },
                    onRestoreFromDrive = { viewModel.restoreFromGoogleDrive() },
                    onToggleAutoSave = { viewModel.toggleAutoSaveToDrive(it) },
                    onSignInClick = { viewModel.openDialog(AppDialog.AuthDialog) },
                    onDismiss = {
                        viewModel.clearDriveStatusMessage()
                        viewModel.dismissDialog()
                    }
                )
            }
            is AppDialog.ThemeSelect -> {
                ThemeSelectionDialog(
                    currentTheme = currentTheme,
                    onSelectTheme = { viewModel.setTheme(it) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.ConfirmRemoveAllTrades -> {
                ConfirmRemoveAllDialog(
                    accountFilter = dialog.accountFilter,
                    totalEntriesCount = trades.size,
                    onConfirm = { viewModel.removeAllTrades(dialog.accountFilter) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.ConfirmDeleteAccount -> {
                val accountTradesCount = trades.count { it.account == dialog.accountName }
                ConfirmDeleteAccountDialog(
                    accountName = dialog.accountName,
                    tradeCount = accountTradesCount,
                    onConfirm = { deleteTrades ->
                        viewModel.removeAccount(dialog.accountName, deleteAccountTrades = deleteTrades)
                    },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.AddAccount -> {
                AddAccountDialog(
                    existingAccounts = accounts,
                    onAddAccount = { viewModel.addAccount(it) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.ConfirmCleanAllData -> {
                ConfirmCleanAllDataDialog(
                    onConfirm = { viewModel.cleanAllData() },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.UserMenuDialog -> {
                UserMenuDialog(
                    profile = userProfile,
                    onOpenSettings = { viewModel.openDialog(AppDialog.SettingsDialog) },
                    onOpenBackup = { viewModel.openDialog(AppDialog.GoogleDriveSync) },
                    onOpenHelp = { viewModel.openDialog(AppDialog.HelpDialog) },
                    onOpenBrokerSync = { viewModel.openDialog(AppDialog.BrokerSync) },
                    onSignIn = { viewModel.openDialog(AppDialog.AuthDialog) },
                    onSignOut = { viewModel.signOut() },
                    onCleanData = { viewModel.openDialog(AppDialog.ConfirmCleanAllData) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.SettingsDialog -> {
                SettingsDialog(
                    currentTheme = currentTheme,
                    isGlobalAutoSync = isGlobalAutoSyncEnabled,
                    onSelectTheme = { viewModel.setTheme(it) },
                    onToggleGlobalAutoSync = { viewModel.toggleGlobalAutoSync(it) },
                    onCleanDataClick = { viewModel.openDialog(AppDialog.ConfirmCleanAllData) },
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            is AppDialog.HelpDialog -> {
                HelpDialog(
                    onDismiss = { viewModel.dismissDialog() }
                )
            }
            null -> Unit
        }
    }
}
