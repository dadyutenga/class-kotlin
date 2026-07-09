package com.biglitecode.familyhub.ui.privacy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

/** Same support address as ContactScreen. */
private const val SUPPORT_EMAIL = "support@familyhub.app"

/**
 * Demo Privacy Policy content for the FamilyHub student assignment.
 * Not a real legal document.
 */
@Composable
fun PrivacyPolicyScreen(
    onBackClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextBrown
                )
            }
            Text(
                text = "Privacy Policy",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextBrown
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            PolicySection(
                title = "Introduction",
                body = "FamilyHub is a family chore and task coordination app. To provide " +
                    "the service, we collect family member profiles, task assignments, and " +
                    "activity related to completing chores. This page describes, in plain " +
                    "language, how that information is handled in this demo application."
            )

            PolicySection(
                title = "Information We Collect",
                body = "We may collect the following categories of information:\n" +
                    "• Account information such as name and email address\n" +
                    "• Family group data, including group name and invite codes\n" +
                    "• Task and activity history (assignments, status, points, feedback)\n" +
                    "• Device features used for reminders and contact (notifications, SMS " +
                    "intents, and dialer intents for calling a family admin)"
            )

            PolicySection(
                title = "How We Use Your Information",
                body = "We use this information to sync tasks across your family group, " +
                    "show progress and rewards, send reminders when enabled, and help " +
                    "members coordinate household responsibilities. Data is used only for " +
                    "features you see in the app, not for advertising."
            )

            PolicySection(
                title = "Data Sharing",
                body = "Your family data is shared only with members of your own family " +
                    "group so everyone can see assigned chores and progress. We do not sell " +
                    "personal information or share it with third-party advertisers. Future " +
                    "backend services (for example Supabase) would store data solely to " +
                    "operate FamilyHub."
            )

            PolicySection(
                title = "Data Security",
                body = "We aim to protect your information with industry-standard practices " +
                    "such as encrypted connections (HTTPS) and secure authentication when " +
                    "a backend is connected. Access to family data is limited to signed-in " +
                    "members of that group. No method of transmission is 100% secure, but " +
                    "we take reasonable steps to reduce risk."
            )

            PolicySection(
                title = "Your Rights",
                body = "You may request correction or deletion of your account and related " +
                    "family data. Parents or guardians managing a group can ask to remove " +
                    "a member or dissolve the group. In this demo build, deletion is " +
                    "simulated; a production release would process requests through support."
            )

            PolicySection(
                title = "Contact Us",
                body = "Questions about this Privacy Policy or your data can be sent to " +
                    "$SUPPORT_EMAIL. We will do our best to respond within a few business days."
            )

            PolicySection(
                title = "Last Updated",
                body = "July 9, 2026"
            )
        }
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String
) {
    Text(
        text = title,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        color = TextBrown,
        modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
    )
    Text(
        text = body,
        fontSize = 14.sp,
        color = TextMutedBrown,
        lineHeight = 20.sp
    )
    Spacer(modifier = Modifier.height(4.dp))
}

@Preview(showBackground = true, showSystemUi = true, name = "Privacy Policy")
@Composable
private fun PrivacyPolicyScreenPreview() {
    FamilyHubTheme {
        PrivacyPolicyScreen()
    }
}
