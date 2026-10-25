package com.example.tradepulse.data.model

import kotlinx.serialization.Serializable

enum class TradeSide {
    LONG,
    SHORT
}

enum class AssetClass {
    STOCKS,
    OPTIONS,
    FUTURES,
    FOREX,
    CRYPTO
}

enum class PreTradeEmotion {
    CALM,
    CONFIDENT,
    PATIENT,
    ANXIOUS,
    FOMO,
    GREEDY,
    HESITANT,
    IMPULSIVE
}

enum class PostTradeEmotion {
    SATISFIED,
    DISCIPLINED,
    PROUD,
    CONFIDENT,
    RELIEVED,
    FRUSTRATED,
    ANGRY,
    REGRETFUL,
    EXHAUSTED
}

@Serializable
data class ChecklistCriterion(
    val id: String,
    val label: String,
    val isMandatory: Boolean = true
)

@Serializable
data class TradeChecklistItem(
    val criterionId: String,
    val label: String,
    val isMandatory: Boolean,
    val passed: Boolean
)

@Serializable
data class Trade(
    val id: String,
    val symbol: String,
    val side: TradeSide,
    val assetClass: AssetClass,
    val entryDate: String, // ISO String (e.g. 2026-10-23T09:35:00.000Z)
    val exitDate: String,
    val entryPrice: Double,
    val exitPrice: Double,
    val quantity: Double,
    val commission: Double = 0.0,
    val netPnL: Double,
    val pnlPercent: Double,
    val stopLossPrice: Double? = null,
    val targetPrice: Double? = null,
    val riskAmount: Double? = null,
    val rMultiple: Double = 0.0,
    val setup: String = "Discretionary",
    val preEmotion: PreTradeEmotion = PreTradeEmotion.CALM,
    val postEmotion: PostTradeEmotion = PostTradeEmotion.SATISFIED,
    val disciplineScore: Int = 5, // 1 to 5
    val followedPlan: Boolean = true,
    val mistakes: List<String> = emptyList(),
    val notes: String = "",
    val account: String = "Interactive Brokers (Main)",
    val broker: String = "Interactive Brokers",
    val accountNumber: String? = null,
    val checklist: List<TradeChecklistItem> = emptyList(),
    val playbookId: String? = null,
    val playbookTitle: String? = null,
    val ruleAdherenceRate: Double? = null
)

data class DaySummary(
    val date: String, // YYYY-MM-DD
    val tradesCount: Int,
    val netPnL: Double,
    val winCount: Int,
    val lossCount: Int,
    val winRate: Double,
    val trades: List<Trade>
)

data class AccountStats(
    val netPnL: Double = 0.0,
    val grossProfit: Double = 0.0,
    val grossLoss: Double = 0.0,
    val totalTrades: Int = 0,
    val winningTrades: Int = 0,
    val losingTrades: Int = 0,
    val breakEvenTrades: Int = 0,
    val winRate: Double = 0.0,
    val profitFactor: Double = 0.0,
    val avgWin: Double = 0.0,
    val avgLoss: Double = 0.0,
    val winLossRatio: Double = 0.0,
    val maxDrawdown: Double = 0.0,
    val maxDrawdownPercent: Double = 0.0,
    val expectancy: Double = 0.0,
    val avgHoldMinutes: Int = 0,
    val disciplineAvg: Double = 0.0,
    val followedPlanRate: Double = 0.0
)

data class BrokerConnection(
    val id: String,
    val name: String,
    val logo: String,
    val status: String, // CONNECTED, DISCONNECTED, SYNCING, ERROR
    val lastSynced: String? = null,
    val accountNumber: String? = null,
    val autoSyncEnabled: Boolean = true,
    val syncInterval: String = "15m"
)

data class WeeklyReportData(
    val id: String,
    val weekLabel: String,
    val startDate: String,
    val endDate: String,
    val stats: AccountStats,
    val aiReport: String? = null,
    val generatedAt: String? = null,
    val keyTakeaways: List<String> = emptyList(),
    val grade: String = "B+"
)

data class TradingJournal(
    val id: String,
    val name: String,
    val description: String,
    val color: String,
    val targetMonthlyProfit: Double? = 10000.0,
    val maxDailyDrawdown: Double? = 1000.0,
    val createdAt: String
)

data class DailyJournalEntry(
    val id: String,
    val date: String, // YYYY-MM-DD
    val journalId: String? = null,
    val preMarketPlan: String,
    val marketCondition: String = "TRENDING_UP",
    val mentalPreparednessScore: Int = 5,
    val sessionReview: String,
    val lessonsLearned: String,
    val rating: Int = 5,
    val updatedAt: String? = null
)

enum class PlaybookCategory {
    BREAKOUT,
    PULLBACK,
    REVERSAL,
    MOMENTUM,
    RANGE_BOUND,
    CATALYST_NEWS
}

data class PlaybookStrategy(
    val id: String,
    val title: String,
    val category: PlaybookCategory,
    val description: String,
    val marketCondition: String = "VOLATILE",
    val timeframe: String = "5m Intraday",
    val bestSession: String = "MARKET_OPEN",
    val entryTrigger: String,
    val stopLossRule: String,
    val takeProfitRule: String,
    val minRiskReward: Double = 2.0,
    val rulesChecklist: List<String> = emptyList(),
    val createdAt: String
)

data class BacktestTrade(
    val id: String,
    val symbol: String,
    val direction: String = "BUY", // BUY or SELL
    val timeframe: String = "5m",
    val quantity: Double,
    val openTime: String,
    val closeTime: String? = null,
    val entry: Double,
    val stopLoss: Double,
    val profitTarget: Double,
    val exit: Double? = null,
    val realizedPnL: Double? = null,
    val rMultiple: Double? = null,
    val status: String = "Closed", // Open or Closed
    val playbookId: String? = null,
    val playbookTitle: String? = null,
    val checklistPassedCount: Int = 0,
    val checklistTotalCount: Int = 0,
    val ruleAdherencePercent: Double = 100.0,
    val notes: String = "",
    val createdAt: String
)

data class EmotionMetric(
    val emotion: PreTradeEmotion,
    val count: Int,
    val netPnL: Double,
    val winCount: Int,
    val winRate: Double,
    val avgPnL: Double,
    val avgRMultiple: Double
)

data class SetupMetric(
    val setup: String,
    val count: Int,
    val netPnL: Double,
    val winCount: Int,
    val winRate: Double,
    val profitFactor: Double
)

data class MistakeMetric(
    val mistake: String,
    val count: Int,
    val totalLostPnL: Double
)
