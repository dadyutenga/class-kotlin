package com.biglitecode.familyhub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.biglitecode.familyhub.data.local.SessionStore
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.repository.AppUsageRepository
import com.biglitecode.familyhub.ui.theme.BackgroundLight
import com.biglitecode.familyhub.ui.theme.FamilyBlue
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.TextDark
import com.biglitecode.familyhub.ui.theme.TextGray
import com.biglitecode.familyhub.ui.usage.UsageStatsConsentDialog
import com.biglitecode.familyhub.utils.UsageStatsPermissionHelper
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * Main landing screen after sign-in. Provides navigation to the app's primary
 * features, including App Usage.
 */
class HomeActivity : ComponentActivity() {

    private lateinit var sessionStore: SessionStore
    private lateinit var appUsageRepository: AppUsageRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sessionStore = SessionStore(this)
        appUsageRepository = AppUsageRepository(this)

        setContent {
            FamilyHubTheme {
                HomeScreen(
                    onAppUsageClick = {
                        startActivity(Intent(this, AppUsageActivity::class.java))
                    },
                    onSignOutClick = {
                        lifecycleScope.launch {
                            sessionStore.clearSession()
                            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                )

                ChildUsagePermissionGate(
                    sessionStore = sessionStore,
                    appUsageRepository = appUsageRepository
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            val role = sessionStore.role.firstOrNull()
            val memberId = sessionStore.familyMemberId.firstOrNull()
            val groupId = sessionStore.familyGroupId.firstOrNull()
            if (role == FamilyRole.CHILD && memberId != null && groupId != null) {
                appUsageRepository.syncTodayUsage(memberId, groupId)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onAppUsageClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FamilyHub") },
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sign out"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome home",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "Manage your family, tasks, and screen time.",
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxSize(0.9f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ListItem(
                        headlineContent = { Text("App Usage") },
                        supportingContent = { Text("View screen time for your family") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Apps,
                                contentDescription = null,
                                tint = FamilyBlue
                            )
                        },
                        modifier = Modifier.clickable { onAppUsageClick() },
                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface)
                    )
                }
            }
        }
    }
}

/**
 * Shows a one-time consent dialog to a CHILD before redirecting to system usage
 * access settings. Once the dialog has been shown, it is not shown again.
 */
@Composable
private fun ChildUsagePermissionGate(
    sessionStore: SessionStore,
    appUsageRepository: AppUsageRepository
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val role = sessionStore.role.firstOrNull()
        if (role != FamilyRole.CHILD) {
            checked = true
            return@LaunchedEffect
        }

        val consentShown = sessionStore.usageStatsConsentShown.firstOrNull() ?: false
        if (!consentShown && !UsageStatsPermissionHelper.hasUsageStatsPermission(context)) {
            showDialog = true
        }
        checked = true
    }

    if (showDialog) {
        UsageStatsConsentDialog(
            onConfirm = {
                showDialog = false
                scope.launch {
                    sessionStore.markUsageStatsConsentShown()
                    UsageStatsPermissionHelper.requestUsageStatsPermission(context)
                }
            },
            onDismiss = {
                showDialog = false
                scope.launch {
                    sessionStore.markUsageStatsConsentShown()
                }
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    FamilyHubTheme {
        HomeScreen(onAppUsageClick = {}, onSignOutClick = {})
    }
}
