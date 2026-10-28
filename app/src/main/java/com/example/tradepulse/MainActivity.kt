package com.example.tradepulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.recaptcha.RecaptchaAppCheckProviderFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()
        setContent {
            TradePulseApp()
        }
    }
}

private const val APP_CHECK_DEBUG_STORE_PREFIX = "com.google.firebase.appcheck.debug.store"
private const val APP_CHECK_DEBUG_SECRET_KEY = "com.google.firebase.appcheck.debug.DEBUG_SECRET"
private const val INTENT_EXTRA_DEBUG_TOKEN = "FIREBASE_APPCHECK_DEBUG_TOKEN"

fun configureAppCheck(context: Context, intent: Intent?) {
    val firebaseApp = FirebaseApp.initializeApp(context) ?: return
    val firebaseAppCheck = FirebaseAppCheck.getInstance(firebaseApp)

    if (BuildConfig.DEBUG) {
        val debugToken = intent?.getStringExtra(INTENT_EXTRA_DEBUG_TOKEN)
        if (!debugToken.isNullOrEmpty()) {
            val prefsName = "$APP_CHECK_DEBUG_STORE_PREFIX.${firebaseApp.persistenceKey}"
            context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
                .edit()
                .putString(APP_CHECK_DEBUG_SECRET_KEY, debugToken)
                .commit()
        }

        firebaseAppCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance()
        )
    } else {
        val resId = context.resources.getIdentifier(
            "recaptcha_site_key", "string", context.packageName
        )
        if (resId != 0) {
            val siteKey = context.getString(resId)
            if (siteKey.isNotEmpty()) {
                firebaseAppCheck.installAppCheckProviderFactory(
                    RecaptchaAppCheckProviderFactory.getInstance(siteKey)
                )
            }
        }
    }
}

