package com.biglitecode.familyhub.ui.help

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

private data class FaqItem(val question: String, val answer: String)

private val sampleFaqs = listOf(
    FaqItem(
        question = "How do I add a family member?",
        answer = "Share your Family Code from the Account screen. They sign up, choose Join, and enter the code. Parents can also invite from Settings once multi-invite is enabled."
    ),
    FaqItem(
        question = "How do I assign a task?",
        answer = "Open the Tasks tab, tap the + button, fill in the title and description, pick an assignee and due date, then add reward points and save."
    ),
    FaqItem(
        question = "How do points/rewards work?",
        answer = "Each task has reward points. Completing a task adds those points to the family total. The Dashboard shows weekly progress toward a shared reward goal."
    ),
    FaqItem(
        question = "Can I use FamilyHub offline?",
        answer = "You can view recently loaded tasks offline. Creating or completing tasks requires a connection so the family group stays in sync. Offline queue support is planned."
    ),
    FaqItem(
        question = "How do I reset my password?",
        answer = "On the login screen, tap Forgot password. Enter the email for your account and follow the reset link we send. Check spam if it doesn't arrive within a few minutes."
    ),
    FaqItem(
        question = "How do I change my role?",
        answer = "Roles (Parent/Guardian vs Child/Member) are set at sign-up. A parent admin can request a role change via Contact. Self-serve role edits will arrive in a future update."
    )
)

@Composable
fun HelpScreen(
    onBack: () -> Unit = {},
    onOpenDrawer: (() -> Unit)? = null
) {
    var searchQuery by remember { mutableStateOf("") }
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ForestGreen,
        unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
        cursorColor = ForestGreen,
        focusedLabelColor = ForestGreen,
        unfocusedLabelColor = TextMutedBrown,
        focusedLeadingIconColor = ForestGreen,
        unfocusedLeadingIconColor = TextMutedBrown
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 28.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                Text(
                    text = "Help & FAQ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextBrown
                )
            }
        }

        item {
            // Visual search bar — filtering not wired yet.
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search help topics") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = MaterialTheme.shapes.small,
                colors = fieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        itemsIndexed(sampleFaqs) { index, faq ->
            ExpandableFaqCard(
                question = faq.question,
                answer = faq.answer,
                expanded = expandedIndex == index,
                onToggle = {
                    expandedIndex = if (expandedIndex == index) null else index
                },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
    }
}

@Composable
private fun ExpandableFaqCard(
    question: String,
    answer: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardCream),
        border = BorderStroke(1.5.dp, BorderGreen.copy(alpha = 0.45f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextBrown,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = ForestGreen,
                    modifier = Modifier.rotate(rotation)
                )
            }
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    color = TextMutedBrown,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Help")
@Composable
private fun HelpScreenPreview() {
    FamilyHubTheme {
        HelpScreen()
    }
}
