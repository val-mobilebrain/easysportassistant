package com.easysport.assistantappnewmb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    // Countries where Privacy Policy consent is required
    private val privacyPolicyCountries = setOf(
        // EU member states
        "AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI",
        "FR", "GR", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT",
        "NL", "PL", "PT", "RO", "SE", "SI", "SK",
        // EEA non-EU
        "IS", "LI", "NO",
        // UK (post-Brexit GDPR equivalent)
        "GB",
        // Other countries with strong privacy legislation
        "BR", "CA", "AU", "JP", "KR", "CH"
    )

    private val privacyPolicyUrl = "https://easysportassistaantapp.online/VpGzjS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(2000L)
            navigateNext()
        }
    }

    private fun navigateNext() {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val alreadyAccepted = prefs.getBoolean("privacy_accepted", false)

        val countryCode = Locale.getDefault().country
        val needsPrivacy = countryCode in privacyPolicyCountries

        val destination = if (needsPrivacy && !alreadyAccepted) {
            Intent(this, PrivacyPolicyActivity::class.java).apply {
                putExtra(PrivacyPolicyActivity.EXTRA_URL, privacyPolicyUrl)
            }
        } else {
            Intent(this, MainActivity::class.java)
        }
        startActivity(destination)
        finish()
    }
}
