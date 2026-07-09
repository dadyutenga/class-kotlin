package com.biglitecode.familyhub.ui.complains

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.Complaint
import com.biglitecode.familyhub.data.model.ComplaintStatus
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.session.SessionManager
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.CoralRed
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.ForestGreenLight
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ComplainsScreen(
    viewModel: ComplainsViewModel = viewModel(),
    onBack: () -> Unit = {},
    onOpenDrawer: (() -> Unit)? = null
) {
    val complaints by viewModel.complaints.collectAsStateWithLifecycle()
    val form by viewModel.form.collectAsStateWithLifecycle()
    val currentUser by SessionManager.currentUser.collectAsStateWithLifecycle()

    val visibleComplaints = if (currentUser?.role == FamilyRole.PARENT) {
        complaints
    } else {
        complaints.filter { it.submittedBy == currentUser?.id }
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ForestGreen,
        unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
        cursorColor = ForestGreen,
        focusedLabelColor = ForestGreen,
        unfocusedLabelColor = TextMutedBrown
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
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
                        text = "Family Complaints",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = TextBrown
                    )
                    Text(
                        text = "Raise an issue with a family member or task",
                        fontSize = 13.sp,
                        color = TextMutedBrown
                    )
                }
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = CardCream),
                border = BorderStroke(1.5.dp, BorderGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = form.subject,
                        onValueChange = viewModel::setSubject,
                        label = { Text("Subject") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = form.description,
                        onValueChange = viewModel::setDescription,
                        label = { Text("Description") },
                        minLines = 4,
                        maxLines = 5,
                        shape = MaterialTheme.shapes.small,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { viewModel.submit() },
                        enabled = form.subject.isNotBlank() && form.description.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Submit Complaint", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Text(
                text = if (currentUser?.role == FamilyRole.PARENT) {
                    "All family complaints"
                } else {
                    "My complaints"
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextBrown,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
            )
        }

        items(visibleComplaints, key = { it.id }) { complaint ->
            ComplaintCard(
                complaint = complaint,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }

        if (visibleComplaints.isEmpty()) {
            item {
                Text(
                    text = "No complaints yet.",
                    color = TextMutedBrown,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ComplaintCard(
    complaint: Complaint,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
    val isOpen = complaint.status == ComplaintStatus.OPEN

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardCream),
        border = BorderStroke(1.5.dp, if (isOpen) CoralRed.copy(alpha = 0.55f) else BorderGreen.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = complaint.subject,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextBrown,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (isOpen) CoralRed.copy(alpha = 0.15f) else ForestGreenLight
                ) {
                    Text(
                        text = if (isOpen) "Open" else "Resolved",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isOpen) CoralRed else ForestGreen,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = complaint.description,
                fontSize = 13.sp,
                color = TextMutedBrown,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Submitted ${dateFormat.format(Date(complaint.submittedAt))}",
                fontSize = 12.sp,
                color = TextMutedBrown
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Complains")
@Composable
private fun ComplainsScreenPreview() {
    FamilyHubTheme {
        ComplainsScreen()
    }
}
