package com.biglitecode.familyhub.ui.feedback

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = viewModel(),
    onBack: () -> Unit = {},
    onOpenDrawer: (() -> Unit)? = null
) {
    val cards by viewModel.cards.collectAsStateWithLifecycle()

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
                    text = "Task Feedback",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = TextBrown
                )
                Text(
                    text = "Rate completed chores and leave a note",
                    fontSize = 13.sp,
                    color = TextMutedBrown
                )
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (cards.isEmpty()) {
                item {
                    Text(
                        text = "No completed tasks to rate yet.",
                        color = TextMutedBrown,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
            items(cards, key = { it.task.id }) { card ->
                FeedbackTaskCard(
                    card = card,
                    onRatingChange = { viewModel.setRating(card.task.id, it) },
                    onCommentChange = { viewModel.setComment(card.task.id, it) }
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
        }

        Button(
            onClick = { viewModel.submitAll() },
            enabled = cards.any { it.rating > 0 },
            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
            shape = MaterialTheme.shapes.large,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .height(52.dp)
        ) {
            Text("Submit Feedback", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun FeedbackTaskCard(
    card: FeedbackCardUi,
    onRatingChange: (Int) -> Unit,
    onCommentChange: (String) -> Unit
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ForestGreen,
        unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
        cursorColor = ForestGreen,
        focusedLabelColor = ForestGreen,
        unfocusedLabelColor = TextMutedBrown
    )

    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardCream),
        border = BorderStroke(1.5.dp, BorderGreen.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = card.task.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextBrown
            )
            Text(
                text = "Assigned to ${card.task.assignedToName}",
                fontSize = 13.sp,
                color = TextMutedBrown,
                modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                (1..5).forEach { star ->
                    val filled = star <= card.rating
                    Icon(
                        imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = "Rate $star",
                        tint = if (filled) GoldYellow else TextMutedBrown.copy(alpha = 0.5f),
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { onRatingChange(star) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = card.comment,
                onValueChange = onCommentChange,
                label = { Text("Comment (optional)") },
                minLines = 2,
                shape = MaterialTheme.shapes.small,
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
            if (card.alreadySubmitted) {
                Text(
                    text = "Previously submitted — update and submit again anytime.",
                    fontSize = 11.sp,
                    color = TextMutedBrown,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Feedback")
@Composable
private fun FeedbackScreenPreview() {
    FamilyHubTheme {
        FeedbackScreen()
    }
}
