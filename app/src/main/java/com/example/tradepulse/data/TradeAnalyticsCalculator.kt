package com.example.tradepulse.data

import com.example.tradepulse.data.model.*
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.roundToInt

object TradeAnalyticsCalculator {

    fun calculateAccountStats(trades: List<Trade>): AccountStats {
        if (trades.isEmpty()) return AccountStats()

        var grossProfit = 0.0
        var grossLoss = 0.0
        var winningTrades = 0
        var losingTrades = 0
        var breakEvenTrades = 0
        var totalDiscipline = 0
        var followedPlanCount = 0

        var runningPnL = 0.0
        var peakPnL = 0.0
        var maxDrawdown = 0.0

        val sortedTrades = trades.sortedBy { it.entryDate }

        sortedTrades.forEach { t ->
            val pnl = t.netPnL
            runningPnL += pnl
            if (runningPnL > peakPnL) {
                peakPnL = runningPnL
            }
            val currentDrawdown = peakPnL - runningPnL
            if (currentDrawdown > maxDrawdown) {
                maxDrawdown = currentDrawdown
            }

            when {
                pnl > 0.01 -> {
                    grossProfit += pnl
                    winningTrades++
                }
                pnl < -0.01 -> {
                    grossLoss += abs(pnl)
                    losingTrades++
                }
                else -> {
                    breakEvenTrades++
                }
            }

            totalDiscipline += t.disciplineScore
            if (t.followedPlan) followedPlanCount++
        }

        val totalTrades = trades.size
        val netPnL = grossProfit - grossLoss
        val winRate = if (totalTrades > 0) (winningTrades.toDouble() / totalTrades) * 100.0 else 0.0
        val profitFactor = when {
            grossLoss > 0.0 -> grossProfit / grossLoss
            grossProfit > 0.0 -> 99.9
            else -> 0.0
        }
        val avgWin = if (winningTrades > 0) grossProfit / winningTrades else 0.0
        val avgLoss = if (losingTrades > 0) grossLoss / losingTrades else 0.0
        val winLossRatio = when {
            avgLoss > 0.0 -> avgWin / avgLoss
            avgWin > 0.0 -> 99.9
            else -> 0.0
        }

        val winProb = winningTrades.toDouble() / totalTrades
        val lossProb = losingTrades.toDouble() / totalTrades
        val expectancy = (winProb * avgWin) - (lossProb * avgLoss)

        val disciplineAvg = if (totalTrades > 0) totalDiscipline.toDouble() / totalTrades else 0.0
        val followedPlanRate = if (totalTrades > 0) (followedPlanCount.toDouble() / totalTrades) * 100.0 else 0.0

        return AccountStats(
            netPnL = round2(netPnL),
            grossProfit = round2(grossProfit),
            grossLoss = round2(grossLoss),
            totalTrades = totalTrades,
            winningTrades = winningTrades,
            losingTrades = losingTrades,
            breakEvenTrades = breakEvenTrades,
            winRate = round1(winRate),
            profitFactor = round2(profitFactor),
            avgWin = round2(avgWin),
            avgLoss = round2(avgLoss),
            winLossRatio = round2(winLossRatio),
            maxDrawdown = round2(maxDrawdown),
            maxDrawdownPercent = 0.0,
            expectancy = round2(expectancy),
            avgHoldMinutes = 38,
            disciplineAvg = round1(disciplineAvg),
            followedPlanRate = round1(followedPlanRate)
        )
    }

    fun getEmotionalBreakdown(trades: List<Trade>): List<EmotionMetric> {
        val map = mutableMapOf<PreTradeEmotion, EmotionAccumulator>()

        trades.forEach { t ->
            val em = t.preEmotion
            val acc = map.getOrPut(em) { EmotionAccumulator() }
            acc.count++
            acc.netPnL += t.netPnL
            if (t.netPnL > 0) acc.winCount++
            acc.totalR += t.rMultiple
        }

        return PreTradeEmotion.values().mapNotNull { em ->
            val acc = map[em] ?: return@mapNotNull null
            EmotionMetric(
                emotion = em,
                count = acc.count,
                netPnL = round2(acc.netPnL),
                winCount = acc.winCount,
                winRate = if (acc.count > 0) round1((acc.winCount.toDouble() / acc.count) * 100.0) else 0.0,
                avgPnL = if (acc.count > 0) round2(acc.netPnL / acc.count) else 0.0,
                avgRMultiple = if (acc.count > 0) round2(acc.totalR / acc.count) else 0.0
            )
        }.sortedByDescending { it.netPnL }
    }

    fun getSetupBreakdown(trades: List<Trade>): List<SetupMetric> {
        val map = mutableMapOf<String, SetupAccumulator>()

        trades.forEach { t ->
            val setupName = if (t.setup.isNotBlank()) t.setup else "Discretionary"
            val acc = map.getOrPut(setupName) { SetupAccumulator() }
            acc.count++
            acc.netPnL += t.netPnL
            if (t.netPnL > 0) {
                acc.winCount++
                acc.profit += t.netPnL
            } else {
                acc.loss += abs(t.netPnL)
            }
        }

        return map.map { (setup, acc) ->
            val pf = when {
                acc.loss > 0 -> acc.profit / acc.loss
                acc.profit > 0 -> 99.9
                else -> 0.0
            }
            SetupMetric(
                setup = setup,
                count = acc.count,
                netPnL = round2(acc.netPnL),
                winCount = acc.winCount,
                winRate = if (acc.count > 0) round1((acc.winCount.toDouble() / acc.count) * 100.0) else 0.0,
                profitFactor = round2(pf)
            )
        }.sortedByDescending { it.netPnL }
    }

    fun getMistakesBreakdown(trades: List<Trade>): List<MistakeMetric> {
        val map = mutableMapOf<String, MistakeAccumulator>()

        trades.forEach { t ->
            t.mistakes.forEach { m ->
                val acc = map.getOrPut(m) { MistakeAccumulator() }
                acc.count++
                if (t.netPnL < 0) {
                    acc.loss += abs(t.netPnL)
                }
            }
        }

        return map.map { (m, acc) ->
            MistakeMetric(
                mistake = m,
                count = acc.count,
                totalLostPnL = round2(acc.loss)
            )
        }.sortedByDescending { it.totalLostPnL }
    }

    fun getDaySummaries(trades: List<Trade>): List<DaySummary> {
        val byDate = trades.groupBy { it.entryDate.take(10) }

        return byDate.map { (date, dayTrades) ->
            val net = dayTrades.sumOf { it.netPnL }
            val wins = dayTrades.count { it.netPnL > 0 }
            val losses = dayTrades.count { it.netPnL < 0 }
            DaySummary(
                date = date,
                tradesCount = dayTrades.size,
                netPnL = round2(net),
                winCount = wins,
                lossCount = losses,
                winRate = if (dayTrades.isNotEmpty()) round1((wins.toDouble() / dayTrades.size) * 100.0) else 0.0,
                trades = dayTrades.sortedByDescending { it.entryDate }
            )
        }.sortedByDescending { it.date }
    }

    fun formatCurrency(amount: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.US)
        return format.format(amount)
    }

    fun formatRMultiple(r: Double): String {
        val prefix = if (r > 0) "+" else ""
        return "$prefix${"%.2f".format(Locale.US, r)}R"
    }

    fun formatPercent(pct: Double): String {
        val prefix = if (pct > 0) "+" else ""
        return "$prefix${"%.2f".format(Locale.US, pct)}%"
    }

    private fun round2(v: Double): Double = (v * 100.0).roundToInt() / 100.0
    private fun round1(v: Double): Double = (v * 10.0).roundToInt() / 10.0

    private class EmotionAccumulator(
        var count: Int = 0,
        var netPnL: Double = 0.0,
        var winCount: Int = 0,
        var totalR: Double = 0.0
    )

    private class SetupAccumulator(
        var count: Int = 0,
        var netPnL: Double = 0.0,
        var winCount: Int = 0,
        var profit: Double = 0.0,
        var loss: Double = 0.0
    )

    private class MistakeAccumulator(
        var count: Int = 0,
        var loss: Double = 0.0
    )
}
