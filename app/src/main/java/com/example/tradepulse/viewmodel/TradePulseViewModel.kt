package com.example.tradepulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tradepulse.data.InitialData
import com.example.tradepulse.data.TradeAnalyticsCalculator
import com.example.tradepulse.data.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed interface AppDialog {
    data class EditTrade(val trade: Trade?, val defaultDate: String? = null, val initialPlaybook: PlaybookStrategy? = null) : AppDialog
    data class DayDetail(val daySummary: DaySummary) : AppDialog
    data class DailyJournal(val date: String) : AppDialog
    data class PlaybookDetail(val playbook: PlaybookStrategy) : AppDialog
    object AddPlaybook : AppDialog
    data class AddBacktestTrade(val playbook: PlaybookStrategy? = null) : AppDialog
    object BrokerSync : AppDialog
    object ThemeSelect : AppDialog
}

class TradePulseViewModel : ViewModel() {

    private val _currentTheme = MutableStateFlow(com.example.tradepulse.ui.theme.AppThemeMode.FROST_GLASS)
    val currentTheme: StateFlow<com.example.tradepulse.ui.theme.AppThemeMode> = _currentTheme.asStateFlow()

    fun setTheme(theme: com.example.tradepulse.ui.theme.AppThemeMode) {
        _currentTheme.value = theme
    }

    private val _trades = MutableStateFlow(InitialData.TRADES)
    val trades: StateFlow<List<Trade>> = _trades.asStateFlow()

    private val _accounts = MutableStateFlow(InitialData.ACCOUNTS)
    val accounts: StateFlow<List<String>> = _accounts.asStateFlow()

    private val _journals = MutableStateFlow(InitialData.JOURNALS)
    val journals: StateFlow<List<TradingJournal>> = _journals.asStateFlow()

    private val _brokers = MutableStateFlow(InitialData.BROKERS)
    val brokers: StateFlow<List<BrokerConnection>> = _brokers.asStateFlow()

    private val _playbooks = MutableStateFlow(InitialData.PLAYBOOKS)
    val playbooks: StateFlow<List<PlaybookStrategy>> = _playbooks.asStateFlow()

    private val _backtestTrades = MutableStateFlow(InitialData.BACKTEST_TRADES)
    val backtestTrades: StateFlow<List<BacktestTrade>> = _backtestTrades.asStateFlow()

    private val _dailyEntries = MutableStateFlow<List<DailyJournalEntry>>(emptyList())
    val dailyEntries: StateFlow<List<DailyJournalEntry>> = _dailyEntries.asStateFlow()

    private val _selectedAccount = MutableStateFlow("ALL")
    val selectedAccount: StateFlow<String> = _selectedAccount.asStateFlow()

    private val _activeDialog = MutableStateFlow<AppDialog?>(null)
    val activeDialog: StateFlow<AppDialog?> = _activeDialog.asStateFlow()

    private val _isGeneratingReport = MutableStateFlow(false)
    val isGeneratingReport: StateFlow<Boolean> = _isGeneratingReport.asStateFlow()

    private val _isSyncingBroker = MutableStateFlow(false)
    val isSyncingBroker: StateFlow<Boolean> = _isSyncingBroker.asStateFlow()

    private val _weeklyReport = MutableStateFlow<WeeklyReportData?>(null)
    val weeklyReport: StateFlow<WeeklyReportData?> = _weeklyReport.asStateFlow()

    val filteredTrades: StateFlow<List<Trade>> = combine(_trades, _selectedAccount) { tradesList, account ->
        if (account == "ALL") tradesList else tradesList.filter { it.account == account }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitialData.TRADES)

    val stats: StateFlow<AccountStats> = filteredTrades.map { list ->
        TradeAnalyticsCalculator.calculateAccountStats(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AccountStats())

    val daySummaries: StateFlow<List<DaySummary>> = filteredTrades.map { list ->
        TradeAnalyticsCalculator.getDaySummaries(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val emotionalBreakdown: StateFlow<List<EmotionMetric>> = filteredTrades.map { list ->
        TradeAnalyticsCalculator.getEmotionalBreakdown(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val setupBreakdown: StateFlow<List<SetupMetric>> = filteredTrades.map { list ->
        TradeAnalyticsCalculator.getSetupBreakdown(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mistakesBreakdown: StateFlow<List<MistakeMetric>> = filteredTrades.map { list ->
        TradeAnalyticsCalculator.getMistakesBreakdown(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Generate initial executive coaching report for recent executions
        generateInitialReport()
    }

    fun setSelectedAccount(account: String) {
        _selectedAccount.value = account
    }

    fun openDialog(dialog: AppDialog) {
        _activeDialog.value = dialog
    }

    fun dismissDialog() {
        _activeDialog.value = null
    }

    fun saveTrade(trade: Trade) {
        val current = _trades.value
        val exists = current.any { it.id == trade.id }
        _trades.value = if (exists) {
            current.map { if (it.id == trade.id) trade else it }
        } else {
            listOf(trade) + current
        }
        dismissDialog()
    }

    fun deleteTrade(tradeId: String) {
        _trades.value = _trades.value.filter { it.id !== tradeId }
    }

    fun addAccount(accountName: String) {
        if (accountName.isNotBlank() && !_accounts.value.contains(accountName)) {
            _accounts.value = _accounts.value + accountName
        }
    }

    fun savePlaybook(playbook: PlaybookStrategy) {
        val current = _playbooks.value
        val exists = current.any { it.id == playbook.id }
        _playbooks.value = if (exists) {
            current.map { if (it.id == playbook.id) playbook else it }
        } else {
            listOf(playbook) + current
        }
        dismissDialog()
    }

    fun saveBacktestTrade(trade: BacktestTrade) {
        _backtestTrades.value = listOf(trade) + _backtestTrades.value
        dismissDialog()
    }

    fun saveDailyJournalEntry(entry: DailyJournalEntry) {
        val current = _dailyEntries.value
        val filtered = current.filter { it.date != entry.date }
        _dailyEntries.value = listOf(entry) + filtered
        dismissDialog()
    }

    fun syncBroker(brokerId: String) {
        viewModelScope.launch {
            _isSyncingBroker.value = true
            delay(1200) // realistic network sync delay

            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date())

            val syncedTrade1 = Trade(
                id = "sync-${System.currentTimeMillis()}-1",
                symbol = "NVDA",
                side = TradeSide.LONG,
                assetClass = AssetClass.STOCKS,
                entryDate = now,
                exitDate = now,
                entryPrice = 138.45,
                exitPrice = 142.10,
                quantity = 150.0,
                commission = 2.15,
                netPnL = 545.35,
                pnlPercent = 2.64,
                rMultiple = 2.4,
                setup = "Opening Drive Breakout",
                preEmotion = PreTradeEmotion.CALM,
                postEmotion = PostTradeEmotion.CONFIDENT,
                disciplineScore = 5,
                followedPlan = true,
                mistakes = emptyList(),
                notes = "Synchronized from broker API. Clean breakout on high volume.",
                account = "Interactive Brokers (Main)",
                broker = brokerId
            )

            val syncedTrade2 = Trade(
                id = "sync-${System.currentTimeMillis()}-2",
                symbol = "NQ",
                side = TradeSide.LONG,
                assetClass = AssetClass.FUTURES,
                entryDate = now,
                exitDate = now,
                entryPrice = 20420.0,
                exitPrice = 20455.0,
                quantity = 2.0,
                commission = 8.5,
                netPnL = 691.5,
                pnlPercent = 0.17,
                rMultiple = 2.1,
                setup = "VWAP Pullback",
                preEmotion = PreTradeEmotion.PATIENT,
                postEmotion = PostTradeEmotion.SATISFIED,
                disciplineScore = 5,
                followedPlan = true,
                mistakes = emptyList(),
                notes = "Synchronized from broker API. Reclaim of institutional value line.",
                account = "Topstep 150K",
                broker = brokerId
            )

            _trades.value = listOf(syncedTrade1, syncedTrade2) + _trades.value

            _brokers.value = _brokers.value.map { b ->
                if (b.id == brokerId || b.name == brokerId) {
                    b.copy(status = "CONNECTED", lastSynced = now)
                } else b
            }

            _isSyncingBroker.value = false
        }
    }

    fun generateWeeklyReport() {
        viewModelScope.launch {
            _isGeneratingReport.value = true
            delay(1500) // simulation of Gemini institutional audit analysis

            val currentStats = stats.value
            val grade = when {
                currentStats.winRate >= 65 && currentStats.netPnL > 0 -> "A-"
                currentStats.winRate >= 50 && currentStats.netPnL > 0 -> "B+"
                currentStats.netPnL > 0 -> "B-"
                else -> "C+"
            }

            val reportContent = buildString {
                appendLine("### Executive Summary & Performance Rating")
                appendLine("**Weekly Grade: $grade (High Technical Edge, Controlled Psychological Variance)**")
                appendLine()
                appendLine("The execution cycle produced net **${TradeAnalyticsCalculator.formatCurrency(currentStats.netPnL)}** across **${currentStats.totalTrades} executed orders** with a **${currentStats.winRate}% win rate** and **${currentStats.profitFactor} Profit Factor**.")
                appendLine("Technical edge in momentum continuation and pullback retests was sharp, while emotional leaks primarily stemmed from mid-day impulsive sizing and premature profit-taking.")
                appendLine()
                appendLine("---")
                appendLine()
                appendLine("### 1. Psychological & Emotional Leak Analysis")
                appendLine("* **Calm & Patient Setups**: When entering in a **CALM** or **PATIENT** mental state, your win rate reached **74%**, capturing an average **+2.4R multiple**.")
                appendLine("* **Cost of Impulsive / FOMO Entries**: Trades taken in a state of **FOMO** or **ANXIETY** yielded an estimated capital drag of **-$1,280.00**.")
                appendLine("* **Revenge Trading Circuit**: Quick re-entries following stop-outs incurred excessive slippage.")
                appendLine()
                appendLine("---")
                appendLine()
                appendLine("### 2. Technical Playbook & Strategy Audit")
                appendLine("* **Top Alpha Strategy**: *Opening Drive Breakout* and *VWAP Pullback* proved most profitable, generating over **80% of total weekly gross profit**.")
                appendLine("* **Low Expectancy Strategy**: *Counter-Trend Dip Buys* suffered in high-trend market regimes.")
                appendLine()
                appendLine("---")
                appendLine()
                appendLine("### 3. Top 3 Recurring Behavioral Mistakes")
                appendLine("1. **Chasing Extended Candles**: Entering near highs without waiting for pullback to value.")
                appendLine("2. **Moving Stop Losses**: Turning 1R planned losses into larger drawdowns.")
                appendLine("3. **Lunch Lull Chop Overtrading**: Executions between 11:30 AM and 1:30 PM EST.")
                appendLine()
                appendLine("---")
                appendLine()
                appendLine("### 4. Non-Negotiable Tactical Protocol for Next Week")
                appendLine("1. **Enforce the 2-Loss Daily Stop**: If two consecutive trades stop out, mandatory 45-minute pause.")
                appendLine("2. **Pre-Trade Emotional Audit**: 10-second check-in before entering. If FOMO or Frustrated, cut size by 50% or cancel.")
                appendLine("3. **Focus on A+ Setups Only**: Prioritize Opening Range Breakouts and VWAP Pullbacks during peak session volume.")
            }

            _weeklyReport.value = WeeklyReportData(
                id = "report-${System.currentTimeMillis()}",
                weekLabel = "October 19 - 25, 2026",
                startDate = "2026-10-19",
                endDate = "2026-10-25",
                stats = currentStats,
                aiReport = reportContent,
                generatedAt = "2026-10-25T11:00:00Z",
                keyTakeaways = listOf(
                    "Calm execution yielded +2.4R average win",
                    "Eliminating FOMO entries saves ~$1,280/wk",
                    "Opening Range Breakout remains #1 edge"
                ),
                grade = grade
            )

            _isGeneratingReport.value = false
        }
    }

    private fun generateInitialReport() {
        val s = TradeAnalyticsCalculator.calculateAccountStats(_trades.value)
        _weeklyReport.value = WeeklyReportData(
            id = "report-initial",
            weekLabel = "October 19 - 25, 2026",
            startDate = "2026-10-19",
            endDate = "2026-10-25",
            stats = s,
            aiReport = """
                ### Executive Summary & Performance Rating
                **Weekly Grade: A- (High Technical Edge, Controlled Psychological Variance)**

                The execution cycle produced net **${TradeAnalyticsCalculator.formatCurrency(s.netPnL)}** across **${s.totalTrades} executed orders** with a **${s.winRate}% win rate** and **${s.profitFactor} Profit Factor**.
                Technical edge in momentum continuation and pullback retests was sharp, while emotional leaks primarily stemmed from mid-day impulsive sizing.

                ---

                ### 1. Psychological & Emotional Leak Analysis
                * **Calm & Patient Setups**: When entering in a **CALM** or **PATIENT** mental state, win rate reached **74%**, capturing **+2.4R multiple**.
                * **Cost of Impulsive / FOMO Entries**: Trades taken in **FOMO** or **ANXIETY** produced a drag of **-$1,280.00**.
                * **Revenge Trading Circuit**: Avoid quick re-entries right after stopping out.

                ---

                ### 2. Top 3 Recurring Behavioral Mistakes
                1. **Chasing Extended Candles**: Entering late after 3 consecutive green bars.
                2. **Moving Stop Losses**: Not honoring initial invalidation level.
                3. **Mid-Day Lunch Overtrading**: Initiating discretionary trades during volume lulls.

                ---

                ### 3. Non-Negotiable Tactical Protocol for Next Week
                1. **Enforce 2-Loss Daily Stop**: Close terminal for 45 minutes on 2 stops.
                2. **Pre-Trade Emotional Audit**: Confirm Calm/Patient state before submit.
                3. **Focus on A+ Setups Only**: Trade ORB and VWAP pullbacks in first 90 minutes.
            """.trimIndent(),
            generatedAt = "2026-10-25T10:00:00Z",
            keyTakeaways = listOf(
                "Calm execution produced +2.4R average win",
                "FOMO/Revenge trades cost -$1,280",
                "ORB strategy generated 80% of net profit"
            ),
            grade = "A-"
        )
    }
}
