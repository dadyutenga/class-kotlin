package com.biglitecode.familyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.biglitecode.familyhub.ui.privacy.PrivacyPolicyScreen
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme

/**
 * Lightweight host for [PrivacyPolicyScreen].
 * In the main shell, Privacy Policy is also reachable via the NavGraph "privacy" route
 * (same pattern as Help / Contact / Complains). This Activity remains available for
 * direct launch if needed.
 */
class PrivacyPolicyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyHubTheme {
                PrivacyPolicyScreen(onBackClick = { finish() })
            }
        }
    }
}
