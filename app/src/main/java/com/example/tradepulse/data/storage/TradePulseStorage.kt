package com.example.tradepulse.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.tradepulse.data.InitialData
import com.example.tradepulse.data.model.*
import com.example.tradepulse.ui.theme.AppThemeMode
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TradePulseStorage(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "tradepulse_user_clean_v2",
        Context.MODE_PRIVATE
    )

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
    }

    fun clearAllData() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_THEME = "key_theme"
        private const val KEY_SELECTED_ACCOUNT = "key_selected_account"
        private const val KEY_TRADES_SAVED = "key_trades_saved"
        private const val KEY_TRADES_DATA = "key_trades_data"
        private const val KEY_ACCOUNTS_SAVED = "key_accounts_saved"
        private const val KEY_ACCOUNTS_DATA = "key_accounts_data"
        private const val KEY_PLAYBOOKS_SAVED = "key_playbooks_saved"
        private const val KEY_PLAYBOOKS_DATA = "key_playbooks_data"
        private const val KEY_BACKTEST_SAVED = "key_backtest_saved"
        private const val KEY_BACKTEST_DATA = "key_backtest_data"
        private const val KEY_DAILY_SAVED = "key_daily_saved"
        private const val KEY_DAILY_DATA = "key_daily_data"
        private const val KEY_USER_PROFILE = "key_user_profile"
        private const val KEY_BROKERS_DATA = "key_brokers_data"
        private const val KEY_GLOBAL_AUTO_SYNC = "key_global_auto_sync"
        private const val KEY_DRIVE_BACKUP_DATA = "key_drive_backup_data"
    }

    // Theme Persistence
    fun loadTheme(): AppThemeMode {
        val savedName = prefs.getString(KEY_THEME, AppThemeMode.FROST_GLASS.name)
        return try {
            AppThemeMode.valueOf(savedName ?: AppThemeMode.FROST_GLASS.name)
        } catch (_: Exception) {
            AppThemeMode.FROST_GLASS
        }
    }

    fun saveTheme(theme: AppThemeMode) {
        prefs.edit().putString(KEY_THEME, theme.name).apply()
    }

    // Selected Account Filter Persistence
    fun loadSelectedAccount(): String {
        return prefs.getString(KEY_SELECTED_ACCOUNT, "ALL") ?: "ALL"
    }

    fun saveSelectedAccount(account: String) {
        prefs.edit().putString(KEY_SELECTED_ACCOUNT, account).apply()
    }

    // Accounts Persistence
    fun loadAccounts(): List<String> {
        if (!prefs.contains(KEY_ACCOUNTS_SAVED)) {
            return InitialData.ACCOUNTS
        }
        val raw = prefs.getString(KEY_ACCOUNTS_DATA, null) ?: return InitialData.ACCOUNTS
        return try {
            json.decodeFromString<List<String>>(raw)
        } catch (_: Exception) {
            InitialData.ACCOUNTS
        }
    }

    fun saveAccounts(accounts: List<String>) {
        try {
            val raw = json.encodeToString(accounts)
            prefs.edit()
                .putBoolean(KEY_ACCOUNTS_SAVED, true)
                .putString(KEY_ACCOUNTS_DATA, raw)
                .apply()
        } catch (_: Exception) {
            // ignore serialization error
        }
    }

    // Trades Persistence
    fun loadTrades(): List<Trade> {
        if (!prefs.contains(KEY_TRADES_SAVED)) {
            return InitialData.TRADES
        }
        val raw = prefs.getString(KEY_TRADES_DATA, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<Trade>>(raw)
        } catch (_: Exception) {
            InitialData.TRADES
        }
    }

    fun saveTrades(trades: List<Trade>) {
        try {
            val raw = json.encodeToString(trades)
            prefs.edit()
                .putBoolean(KEY_TRADES_SAVED, true)
                .putString(KEY_TRADES_DATA, raw)
                .apply()
        } catch (_: Exception) {
            // ignore serialization error
        }
    }

    // Playbooks Persistence
    fun loadPlaybooks(): List<PlaybookStrategy> {
        if (!prefs.contains(KEY_PLAYBOOKS_SAVED)) {
            return InitialData.PLAYBOOKS
        }
        val raw = prefs.getString(KEY_PLAYBOOKS_DATA, null) ?: return InitialData.PLAYBOOKS
        return try {
            json.decodeFromString<List<PlaybookStrategy>>(raw)
        } catch (_: Exception) {
            InitialData.PLAYBOOKS
        }
    }

    fun savePlaybooks(playbooks: List<PlaybookStrategy>) {
        try {
            val raw = json.encodeToString(playbooks)
            prefs.edit()
                .putBoolean(KEY_PLAYBOOKS_SAVED, true)
                .putString(KEY_PLAYBOOKS_DATA, raw)
                .apply()
        } catch (_: Exception) {
            // ignore serialization error
        }
    }

    // Backtest Trades Persistence
    fun loadBacktestTrades(): List<BacktestTrade> {
        if (!prefs.contains(KEY_BACKTEST_SAVED)) {
            return InitialData.BACKTEST_TRADES
        }
        val raw = prefs.getString(KEY_BACKTEST_DATA, null) ?: return InitialData.BACKTEST_TRADES
        return try {
            json.decodeFromString<List<BacktestTrade>>(raw)
        } catch (_: Exception) {
            InitialData.BACKTEST_TRADES
        }
    }

    fun saveBacktestTrades(trades: List<BacktestTrade>) {
        try {
            val raw = json.encodeToString(trades)
            prefs.edit()
                .putBoolean(KEY_BACKTEST_SAVED, true)
                .putString(KEY_BACKTEST_DATA, raw)
                .apply()
        } catch (_: Exception) {
            // ignore serialization error
        }
    }

    // Daily Journal Entries Persistence
    fun loadDailyEntries(): List<DailyJournalEntry> {
        if (!prefs.contains(KEY_DAILY_SAVED)) {
            return emptyList()
        }
        val raw = prefs.getString(KEY_DAILY_DATA, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<DailyJournalEntry>>(raw)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun saveDailyEntries(entries: List<DailyJournalEntry>) {
        try {
            val raw = json.encodeToString(entries)
            prefs.edit()
                .putBoolean(KEY_DAILY_SAVED, true)
                .putString(KEY_DAILY_DATA, raw)
                .apply()
        } catch (_: Exception) {
            // ignore serialization error
        }
    }

    // User Profile Persistence (Sign Up / Sign In)
    fun loadUserProfile(): UserProfile? {
        val raw = prefs.getString(KEY_USER_PROFILE, null) ?: return null
        return try {
            json.decodeFromString<UserProfile>(raw)
        } catch (_: Exception) {
            null
        }
    }

    fun saveUserProfile(profile: UserProfile?) {
        if (profile == null) {
            prefs.edit().remove(KEY_USER_PROFILE).apply()
        } else {
            try {
                val raw = json.encodeToString(profile)
                prefs.edit().putString(KEY_USER_PROFILE, raw).apply()
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    // Brokers Persistence
    fun loadBrokers(): List<BrokerConnection> {
        val raw = prefs.getString(KEY_BROKERS_DATA, null) ?: return InitialData.BROKERS
        return try {
            json.decodeFromString<List<BrokerConnection>>(raw)
        } catch (_: Exception) {
            InitialData.BROKERS
        }
    }

    fun saveBrokers(brokers: List<BrokerConnection>) {
        try {
            val raw = json.encodeToString(brokers)
            prefs.edit().putString(KEY_BROKERS_DATA, raw).apply()
        } catch (_: Exception) {
            // ignore
        }
    }

    // Global Broker Auto-Sync Toggle
    fun loadGlobalAutoSync(): Boolean {
        return prefs.getBoolean(KEY_GLOBAL_AUTO_SYNC, true)
    }

    fun saveGlobalAutoSync(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_GLOBAL_AUTO_SYNC, enabled).apply()
    }

    // Google Drive Backup Persistence
    fun loadLastDriveBackup(): GoogleDriveBackup? {
        val raw = prefs.getString(KEY_DRIVE_BACKUP_DATA, null) ?: return null
        return try {
            json.decodeFromString<GoogleDriveBackup>(raw)
        } catch (_: Exception) {
            null
        }
    }

    fun saveLastDriveBackup(backup: GoogleDriveBackup) {
        try {
            val raw = json.encodeToString(backup)
            prefs.edit().putString(KEY_DRIVE_BACKUP_DATA, raw).apply()
        } catch (_: Exception) {
            // ignore
        }
    }
}
