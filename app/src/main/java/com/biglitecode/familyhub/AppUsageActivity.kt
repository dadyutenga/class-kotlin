package com.biglitecode.familyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.usage.AppUsageScreen

/**
 * Screen where a PARENT can view CHILD app usage and a CHILD can view their own
 * usage.
 */
class AppUsageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyHubTheme {
                AppUsageScreen(
                    onNavigateBack = { finish() }
                )
            }
        }
    }
}
