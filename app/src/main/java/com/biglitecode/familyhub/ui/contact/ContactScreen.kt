package com.biglitecode.familyhub.ui.contact

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.ui.account.AccountViewModel
import com.biglitecode.familyhub.ui.components.MemberAvatar
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

private const val SUPPORT_EMAIL = "support@familyhub.app"

@Composable
fun ContactScreen(
    viewModel: AccountViewModel = viewModel(),
    onBack: () -> Unit = {},
    onOpenDrawer: (() -> Unit)? = null
) {
    val members by viewModel.members.collectAsStateWithLifecycle()
    val context = LocalContext.current
    // Prefer a parent with a phone number as family admin.
    val admin = remember(members) {
        members.firstOrNull { it.role == FamilyRole.PARENT && !it.phoneNumber.isNullOrBlank() }
            ?: members.firstOrNull { it.role == FamilyRole.PARENT }
            ?: members.firstOrNull()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 28.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            if (onOpenDrawer != null) {
                IconButton(onClick = onOpenDrawer) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = TextBrown)
                }
            } else {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextBrown
                    )
                }
            }
            Column {
                Text(
                    text = "Contact Family Admin",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextBrown
                )
                Text(
                    text = "Get in touch with your household lead",
                    fontSize = 13.sp,
                    color = TextMutedBrown
                )
            }
        }

        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                MemberAvatar(
                    name = admin?.name ?: "?",
                    avatarColorHex = admin?.avatarColor,
                    isActive = true,
                    size = 72.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = admin?.name ?: "Family Admin",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextBrown
                )
                Text(
                    text = "Parent / Guardian",
                    fontSize = 13.sp,
                    color = TextMutedBrown
                )
                if (!admin?.phoneNumber.isNullOrBlank()) {
                    Text(
                        text = admin?.phoneNumber.orEmpty(),
                        fontSize = 14.sp,
                        color = ForestGreen,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // ACTION_DIAL does not require CALL_PHONE runtime permission
                            // (only ACTION_CALL would). Opens the dialer with the number filled in.
                            val phone = admin?.phoneNumber
                            if (phone.isNullOrBlank()) {
                                Toast.makeText(context, "No phone number on file", Toast.LENGTH_SHORT)
                                    .show()
                                return@Button
                            }
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$phone")
                            }
                            runCatching { context.startActivity(intent) }
                                .onFailure {
                                    Toast.makeText(context, "Unable to open dialer", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Filled.Call, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Call", fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        onClick = {
                            val phone = admin?.phoneNumber
                            if (phone.isNullOrBlank()) {
                                Toast.makeText(context, "No phone number on file", Toast.LENGTH_SHORT)
                                    .show()
                                return@OutlinedButton
                            }
                            // SMS via ACTION_SENDTO — no SEND_SMS permission needed for chooser.
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("smsto:$phone")
                                putExtra("sms_body", "Hi ${admin?.name}, regarding FamilyHub: ")
                            }
                            runCatching { context.startActivity(intent) }
                                .onFailure {
                                    Toast.makeText(context, "Unable to open messages", Toast.LENGTH_SHORT)
                                        .show()
                                }
                        },
                        border = BorderStroke(1.5.dp, GoldYellow),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Filled.Sms, contentDescription = null, tint = TextBrown)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Message", fontWeight = FontWeight.SemiBold, color = TextBrown)
                    }
                }
            }
        }

        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen.copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Support",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextBrown
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "App support email",
                    fontSize = 12.sp,
                    color = TextMutedBrown
                )
                Text(
                    text = SUPPORT_EMAIL,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = ForestGreen
                )
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:$SUPPORT_EMAIL")
                            putExtra(Intent.EXTRA_SUBJECT, "FamilyHub Feedback")
                        }
                        runCatching { context.startActivity(intent) }
                            .onFailure {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    },
                    border = BorderStroke(1.5.dp, ForestGreen),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Filled.Email, contentDescription = null, tint = ForestGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Send Feedback", color = ForestGreen, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Contact")
@Composable
private fun ContactScreenPreview() {
    FamilyHubTheme {
        ContactScreen()
    }
}
