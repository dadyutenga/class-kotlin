package com.biglitecode.familyhub.ui.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.data.session.SessionManager
import com.biglitecode.familyhub.ui.components.MemberAvatar
import com.biglitecode.familyhub.ui.components.TaskCard
import com.biglitecode.familyhub.ui.tasks.TasksViewModel
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

@Composable
fun DashboardScreen(
    viewModel: TasksViewModel = viewModel(),
    familyName: String = "My Family",
    onTaskClick: (String) -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val familyPoints by viewModel.familyPoints.collectAsStateWithLifecycle()
    val currentUser by SessionManager.currentUser.collectAsStateWithLifecycle()

    val isParent = currentUser?.role == FamilyRole.PARENT
    val userName = currentUser?.name ?: "there"

    val visibleTasks = if (currentUser?.role == FamilyRole.PARENT) {
        tasks.filter { it.status != TaskStatus.DONE }
    } else {
        tasks.filter {
            it.status != TaskStatus.DONE && it.assignedTo == currentUser?.id
        }
    }.take(6)

    val pendingCount = visibleTasks.size
    val sectionTitle = if (currentUser?.role == FamilyRole.PARENT) {
        "Today's Tasks"
    } else {
        "My Tasks"
    }

    // Soft weekly goal for the progress bar (purely visual for now).
    val weeklyGoal = 100
    val progress = (familyPoints.toFloat() / weeklyGoal).coerceIn(0f, 1f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = ForestGreen,
                    shadowElevation = 4.dp
                ) {
                    Text(
                        text = familyName,
                        color = CardCream,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Welcome back, $userName",
                    fontSize = 15.sp,
                    color = TextMutedBrown
                )
                if (!isParent) {
                    Text(
                        text = "Showing your assigned chores only",
                        fontSize = 12.sp,
                        color = TextMutedBrown,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        item {
            Text(
                text = "Family",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextBrown,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(members, key = { it.id }) { member ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MemberAvatar(
                            name = member.name,
                            avatarColorHex = member.avatarColor,
                            isActive = member.isActive
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = member.name,
                            fontSize = 12.sp,
                            color = if (member.isActive) TextBrown else TextMutedBrown,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = sectionTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextBrown,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = GoldYellowLight
                ) {
                    Text(
                        text = "$pendingCount left",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextBrown,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        items(visibleTasks, key = { it.id }) { task ->
            TaskCard(
                task = task,
                onClick = { onTaskClick(task.id) },
                onToggleComplete = {
                    // Child may only toggle their own tasks; parent can toggle any.
                    if (currentUser?.role == FamilyRole.PARENT ||
                        task.assignedTo == currentUser?.id
                    ) {
                        viewModel.toggleComplete(task)
                    }
                },
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
            )
        }

        if (visibleTasks.isEmpty()) {
            item {
                Text(
                    text = "All caught up — great job!",
                    color = TextMutedBrown,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
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
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Family Points",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = TextMutedBrown
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$familyPoints",
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = ForestGreen
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "pts this week",
                            fontSize = 13.sp,
                            color = TextMutedBrown,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = GoldYellow,
                        trackColor = GoldYellowLight,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (progress >= 1f) {
                            "Weekly goal smashed — treat time!"
                        } else {
                            "Keep going — ${weeklyGoal - familyPoints} pts to the weekly reward"
                        },
                        fontSize = 13.sp,
                        color = TextMutedBrown
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Dashboard")
@Composable
private fun DashboardScreenPreview() {
    FamilyHubTheme {
        DashboardScreen()
    }
}
