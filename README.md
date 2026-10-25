# TradePulse - Professional Trading Journal & Analytics (Android)

TradePulse is a high-performance Android trading journal and execution analytics application built with **Kotlin** and **Jetpack Compose (Material Design 3)**. It is ported from the web framework to native Android, preserving full business logic, emotional sentiment telemetry, and strategy playbooks.

## Core Features

1. **Dashboard & Executive KPIs**:
   - Real-time Net P&L, Win Rate %, Profit Factor, Expectancy, Max Drawdown, and Discipline Score.
   - Interactive Canvas-rendered **Cumulative Equity Curve** with high-water mark and drawdown tracking.
   - Monthly **Execution Calendar** with daily P&L badges and bottom-sheet session trade inspections.

2. **Trade Journal & Filter Engine**:
   - Complete execution history tracking ticker, side (LONG/SHORT), asset class (Stocks, Options, Futures, Crypto, Forex), entry/exit prices, quantity, and commissions.
   - Real-time search by symbol, setup, or notes.
   - Multi-parameter filters by direction and asset class.

3. **Psychological & Emotional Leak Breakdown**:
   - Telemetry tracking pre-trade emotional states (Calm, Confident, Patient, Anxious, FOMO, Greedy, Hesitant, Impulsive).
   - Dollar impact and win-rate analysis isolating profitable edge states from expensive emotional leaks.
   - Behavioral mistakes audit (Chasing candles, revenge trading, moving stop losses).

4. **Strategy Playbooks**:
   - Pre-loaded institutional setups: Opening Range Breakout (ORB), VWAP Pullback & Reclaim, Liquidity Sweep & Fair Value Gap (FVG), Morning Gap & Go, Key Level Breakdown.
   - Strict criteria checklist rules, defined risk/reward targets, and entry/stop invalidation rules.

5. **Backtesting & Replay Engine**:
   - Forward-testing and historical replay logging.
   - Rule adherence percentage tracking against playbook criteria checklists.

6. **Weekly AI Performance Audits**:
   - Executive grade assessment (A- to C+).
   - Structured psychological leak analysis, strategy audit, recurring mistake elimination, and non-negotiable tactical rules for upcoming sessions.

7. **Brokerage Sync**:
   - Multi-broker connection support (Interactive Brokers, Tradovate, Binance, Alpaca, Charles Schwab).
   - One-tap simulated trade settlement and synchronization.

8. **Daily Pre/Post Market Mental Reviews**:
   - Market regime classification (Trend, Choppy, Volatile).
   - Mental focus score and tactical game plan documentation.
