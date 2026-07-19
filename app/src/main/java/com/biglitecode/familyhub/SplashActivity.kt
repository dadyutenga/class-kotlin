package com.biglitecode.familyhub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.biglitecode.familyhub.data.local.SessionStore
import com.biglitecode.familyhub.ui.splash.SplashScreen
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val sessionStore = SessionStore(this)

        setContent {
            FamilyHubTheme {
                SplashScreen(onTimeout = {
                    lifecycleScope.launch {
                        val memberId = sessionStore.familyMemberId.firstOrNull()
                        val destination = if (memberId != null) {
                            HomeActivity::class.java
                        } else {
                            LoginActivity::class.java
                        }
                        startActivity(Intent(this@SplashActivity, destination))
                        finish()
                    }
                })
            }
        }
    }
}
