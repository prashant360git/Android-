package com.example.tradepulse.data

import com.example.tradepulse.data.model.*

object InitialData {

    val ACCOUNTS = listOf(
        "Main Account"
    )

    val JOURNALS = listOf(
        TradingJournal(
            id = "journal-main",
            name = "Main Trading Journal",
            description = "Daily trade log & execution journal",
            color = "#6366f1",
            targetMonthlyProfit = 5000.0,
            maxDailyDrawdown = 500.0,
            createdAt = "2026-10-01T00:00:00.000Z"
        )
    )

    val CHECKLIST_CRITERIA = listOf(
        ChecklistCriterion("crit-1", "Higher timeframe trend aligned", true),
        ChecklistCriterion("crit-2", "Key support / resistance levels verified", true),
        ChecklistCriterion("crit-3", "Stop loss strictly defined before entry", true),
        ChecklistCriterion("crit-4", "Calculated Risk:Reward ratio is at least 1:2", true),
        ChecklistCriterion("crit-5", "Position size matches max account risk limit", true),
        ChecklistCriterion("crit-6", "Volume confirms breakout or reversal", false),
        ChecklistCriterion("crit-7", "No high-impact news catalyst within 15 minutes", false)
    )

    val PLAYBOOKS = listOf(
        PlaybookStrategy(
            id = "pb-orb",
            title = "Opening Range Breakout (ORB)",
            category = PlaybookCategory.BREAKOUT,
            description = "Capitalizes on market open directional momentum. Exploits early price discovery when institutional volume surges outside the initial 15-minute range.",
            marketCondition = "VOLATILE",
            timeframe = "5m Intraday",
            bestSession = "MARKET_OPEN",
            entryTrigger = "5m candle closes outside High/Low of first 15m session with RVOL > 1.5x.",
            stopLossRule = "Hard stop at midpoint of 15m opening range or 1 tick below breakout candle low.",
            takeProfitRule = "Scale 50% at 2R; trail remainder behind 9 EMA until 5m trend violation.",
            minRiskReward = 2.0,
            rulesChecklist = listOf(
                "Market open Relative Volume (RVOL) is greater than 1.5x 20-day average",
                "Broad market index (SPY / QQQ) is trending in the breakout direction",
                "No major overhead Daily pivot or VWAP resistance within 1.5R of entry",
                "First 15-minute range was tighter than average true range (clean consolidation)",
                "Stop loss is predetermined and strictly positioned beyond range midpoint"
            ),
            createdAt = "2026-09-01T08:00:00.000Z"
        ),
        PlaybookStrategy(
            id = "pb-vwap-pullback",
            title = "VWAP Pullback & Reclaim",
            category = PlaybookCategory.PULLBACK,
            description = "Institutional mean-reversion trend continuation. Enters when an established uptrend pulls back to VWAP with drying volume, showing clear absorption by institutional bids.",
            marketCondition = "TRENDING",
            timeframe = "2m / 5m Intraday",
            bestSession = "MID_DAY",
            entryTrigger = "Price tests VWAP with declining volume, prints a bullish rejection candle, and re-crosses above VWAP.",
            stopLossRule = "2 ticks below the lowest wick that touched VWAP or local swing pivot.",
            takeProfitRule = "First target at prior High of Day (HOD) for 2R; runner target at ATR extension (3R+).",
            minRiskReward = 2.5,
            rulesChecklist = listOf(
                "Asset has been holding above VWAP for at least 30 minutes prior to pullback",
                "Pullback to VWAP occurs with declining sell volume (low-volume retest)",
                "Bullish rejection wick formed on VWAP touch showing buyer defense",
                "Risk is strictly defined with stop loss placed below swing low under VWAP",
                "Target provides at least 2.5R to the morning High of Day (HOD)"
            ),
            createdAt = "2026-09-05T08:00:00.000Z"
        ),
        PlaybookStrategy(
            id = "pb-liquidity-fvg",
            title = "Liquidity Sweep & FVG Reversal",
            category = PlaybookCategory.REVERSAL,
            description = "Smart Money Concepts (SMC) setup. Enters when previous day or session highs/lows are swept for buy/sell stops, followed by market structure shift and displacement into a Fair Value Gap.",
            marketCondition = "VOLATILE",
            timeframe = "1m / 5m Futures & Equities",
            bestSession = "ANY_TIME",
            entryTrigger = "Stop run past key swing point, followed by displacement creating a 3-candle Fair Value Gap (FVG); enter on tap into FVG.",
            stopLossRule = "1 tick beyond the extreme wick of the liquidity sweep high/low.",
            takeProfitRule = "Opposite internal range liquidity pool (opposing swing high/low) targeting 3R+.",
            minRiskReward = 3.0,
            rulesChecklist = listOf(
                "Clear high-timeframe liquidity level (equal highs/lows or session extreme) was swept",
                "Energetic displacement candle formed with imbalance (Fair Value Gap)",
                "Market Structure Shift (MSS) confirmed by break of previous minor swing pivot",
                "Limit order placed inside the 50% equilibrium of the Fair Value Gap",
                "Minimum 3R Risk:Reward ratio to opposite liquidity pool"
            ),
            createdAt = "2026-09-10T08:00:00.000Z"
        ),
        PlaybookStrategy(
            id = "pb-gap-go",
            title = "Morning Gap & Go Momentum",
            category = PlaybookCategory.MOMENTUM,
            description = "High momentum catalyst play. Targets stocks gapping >4% on fresh fundamental catalyst with immense pre-market volume breaking out at the bell.",
            marketCondition = "VOLATILE",
            timeframe = "1m / 2m Intraday",
            bestSession = "MARKET_OPEN",
            entryTrigger = "Break and hold of pre-market high on 1-minute chart within first 5 minutes of opening bell.",
            stopLossRule = "Low of 1-minute entry candle or low of pre-market consolidation.",
            takeProfitRule = "Scale 33% at 1.5R, 33% at 2.5R, trail remainder with Breakeven stop.",
            minRiskReward = 2.0,
            rulesChecklist = listOf(
                "Catalyst is high conviction (earnings beat + raised guidance, major contract)",
                "Pre-market volume is above 500,000 shares before market open",
                "Float is under 100M shares to ensure rapid momentum expansion",
                "No major overhead daily resistance level within 3% of current price",
                "Position size calculated so max risk is exactly 1R account risk limit"
            ),
            createdAt = "2026-09-18T08:00:00.000Z"
        ),
        PlaybookStrategy(
            id = "pb-key-breakdown",
            title = "Key Level Breakdown Short",
            category = PlaybookCategory.PULLBACK,
            description = "Trend continuation short setup. Exploits failure at multi-day support when heavy seller volume forces a breakdown followed by a weak, low-volume back-test into old support.",
            marketCondition = "TRENDING",
            timeframe = "5m / 15m Chart",
            bestSession = "MID_DAY",
            entryTrigger = "Failed re-test of broken support (now resistance) with strong red rejection candle.",
            stopLossRule = "1-2 ticks above the re-test high pivot.",
            takeProfitRule = "Next key daily horizontal support zone or round psychological number.",
            minRiskReward = 2.5,
            rulesChecklist = listOf(
                "Higher timeframe trend (Daily & 1-hour) is decisively bearish",
                "Support level has been tested at least twice prior to breaking",
                "Re-test of broken level occurs on declining, exhausted volume",
                "Bearish engulfing or rejection wick printed right at old support level",
                "Risk is strictly bounded above the re-test wick"
            ),
            createdAt = "2026-09-25T08:00:00.000Z"
        )
    )

    val BROKERS = listOf(
        BrokerConnection(
            id = "ibkr",
            name = "Interactive Brokers",
            logo = "IBKR",
            status = "DISCONNECTED",
            lastSynced = null,
            accountNumber = null,
            autoSyncEnabled = false,
            syncInterval = "15m"
        ),
        BrokerConnection(
            id = "tradovate",
            name = "Tradovate / NinjaTrader",
            logo = "TRAD",
            status = "DISCONNECTED",
            lastSynced = null,
            accountNumber = null,
            autoSyncEnabled = false,
            syncInterval = "1h"
        ),
        BrokerConnection(
            id = "schwab",
            name = "Charles Schwab / TD",
            logo = "SCHW",
            status = "DISCONNECTED",
            lastSynced = null,
            accountNumber = null,
            autoSyncEnabled = false,
            syncInterval = "eod"
        ),
        BrokerConnection(
            id = "alpaca",
            name = "Alpaca Markets",
            logo = "ALPA",
            status = "DISCONNECTED",
            lastSynced = null,
            accountNumber = null,
            autoSyncEnabled = false,
            syncInterval = "15m"
        ),
        BrokerConnection(
            id = "binance",
            name = "Binance Live",
            logo = "BNB",
            status = "DISCONNECTED",
            lastSynced = null,
            accountNumber = null,
            autoSyncEnabled = false,
            syncInterval = "1h"
        )
    )

    // Cleaned state - zero mock entries
    val BACKTEST_TRADES: List<BacktestTrade> = emptyList()

    val TRADES: List<Trade> = emptyList()
}
