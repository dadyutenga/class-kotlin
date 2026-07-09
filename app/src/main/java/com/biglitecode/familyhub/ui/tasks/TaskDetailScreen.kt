package com.biglitecode.familyhub.ui.tasks

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Bluetooth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.ui.components.MemberAvatar
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown
import com.biglitecode.familyhub.util.SmsHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskDetailScreen(
    taskId: String,
    viewModel: TasksViewModel = viewModel(),
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val task = tasks.find { it.id == taskId }
    val member = members.find { it.id == task?.assignedTo }
    val context = LocalContext.current

    // Hold pending reminder targets so the permission callback can send SMS after grant.
    val pendingReminder = remember {
        object {
            var phone: String? = null
            var title: String? = null
            var name: String? = null
        }
    }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val phone = pendingReminder.phone
            val title = pendingReminder.title
            val name = pendingReminder.name
            if (!phone.isNullOrBlank() && !title.isNullOrBlank()) {
                val sent = SmsHelper.sendReminderSms(context, phone, title)
                if (sent) {
                    Toast.makeText(
                        context,
                        "Reminder sent to ${name ?: "member"}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(
                context,
                "SMS permission is required to send reminders",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    if (task == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .padding(20.dp)
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextBrown
                )
            }
            Text("Task not found", color = TextMutedBrown)
        }
        return
    }

    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val isDone = task.status == TaskStatus.DONE
    val canRemind = task.status == TaskStatus.PENDING || task.status == TaskStatus.OVERDUE

    fun sendReminder() {
        val phone = member?.phoneNumber
        if (phone.isNullOrBlank()) {
            Toast.makeText(
                context,
                "No phone number on file for ${task.assignedToName}",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) {
            val sent = SmsHelper.sendReminderSms(context, phone, task.title)
            if (sent) {
                Toast.makeText(
                    context,
                    "Reminder sent to ${task.assignedToName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            pendingReminder.phone = phone
            pendingReminder.title = task.title
            pendingReminder.name = task.assignedToName
            smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
        }
    }

    fun openBluetoothSettings() {
        // Simplified Bluetooth demo: open system Bluetooth settings as an integration point
        // instead of implementing full peer-to-peer task transfer / Nearby Share APIs.
        val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        runCatching { context.startActivity(intent) }
            .onFailure {
                Toast.makeText(context, "Unable to open Bluetooth settings", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextBrown
                )
            }
            Text(
                text = "Chore Details",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextBrown,
                modifier = Modifier.weight(1f)
            )
            // Secondary: open Bluetooth settings to demonstrate device connectivity.
            IconButton(onClick = { openBluetoothSettings() }) {
                Icon(
                    Icons.Filled.Bluetooth,
                    contentDescription = "Share Task Nearby",
                    tint = ForestGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = task.title,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = TextBrown
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Assigned to
        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                MemberAvatar(
                    name = task.assignedToName,
                    avatarColorHex = member?.avatarColor,
                    isActive = true,
                    size = 44.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Assigned To",
                        fontSize = 12.sp,
                        color = TextMutedBrown
                    )
                    Text(
                        text = task.assignedToName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = TextBrown
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            InfoMiniCard(
                label = "Due date",
                value = dateFormat.format(Date(task.dueDate)),
                modifier = Modifier.weight(1f)
            )
            InfoMiniCard(
                label = "Reward",
                value = "${task.rewardPoints} pts",
                modifier = Modifier.weight(1f),
                accent = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Description",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TextMutedBrown
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = task.description.ifBlank { "No description provided." },
            fontSize = 15.sp,
            color = TextBrown,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = { viewModel.markComplete(task.id) },
            enabled = !isDone,
            colors = ButtonDefaults.buttonColors(
                containerColor = ForestGreen,
                disabledContainerColor = ForestGreen.copy(alpha = 0.45f)
            ),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = if (isDone) "Completed" else "Mark Complete",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onEdit,
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextBrown),
            border = BorderStroke(1.5.dp, GoldYellow),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text(
                text = "Edit Task",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }

        if (canRemind) {
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { sendReminder() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreen),
                border = BorderStroke(1.5.dp, ForestGreen),
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Icon(Icons.Filled.Sms, contentDescription = null, tint = ForestGreen)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Send Reminder",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = ForestGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun InfoMiniCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    accent: Boolean = false
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (accent) GoldYellowLight else CardCream
        ),
        border = BorderStroke(1.dp, if (accent) GoldYellow else BorderGreen.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = label, fontSize = 12.sp, color = TextMutedBrown)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = TextBrown
            )
        }
    }
}
