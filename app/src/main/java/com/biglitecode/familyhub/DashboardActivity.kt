package com.biglitecode.familyhub

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.biglitecode.familyhub.data.session.SessionManager
import com.biglitecode.familyhub.navigation.FamilyHubNavGraph
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.util.NotificationHelper

/**
 * Post-auth main shell. Hosts bottom navigation (Home, Tasks, Progress, Settings)
 * via [FamilyHubNavGraph]. Launched after successful login/signup.
 */
class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.createNotificationChannel(this)
        // Fallback if opened without login (e.g. process restore): demo as parent.
        if (SessionManager.currentUser.value == null) {
            SessionManager.setDemoParent()
        }
        enableEdgeToEdge()
        setContent {
            FamilyHubTheme {
                val context = LocalContext.current
                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) {
                    // Granted or denied — either way we only ask once via SharedPreferences.
                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return@LaunchedEffect
                    val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    val alreadyAsked = prefs.getBoolean(KEY_NOTIF_PERMISSION_ASKED, false)
                    if (alreadyAsked) return@LaunchedEffect

                    prefs.edit().putBoolean(KEY_NOTIF_PERMISSION_ASKED, true).apply()
                    val granted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!granted) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

                FamilyHubNavGraph()
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "familyhub_prefs"
        private const val KEY_NOTIF_PERMISSION_ASKED = "notif_permission_asked"
    }
}
