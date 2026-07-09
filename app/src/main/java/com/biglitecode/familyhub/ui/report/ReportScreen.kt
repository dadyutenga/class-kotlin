package com.biglitecode.familyhub.ui.report

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.ui.components.MemberAvatar
import com.biglitecode.familyhub.ui.tasks.LeaderboardEntry
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
fun ReportScreen(
    viewModel: TasksViewModel = viewModel()
) {
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val completedThisWeek by viewModel.completedThisWeek.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = ForestGreen,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Family Leaderboard",
                    color = CardCream,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "This Week",
                fontSize = 14.sp,
                color = TextMutedBrown
            )
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = CardCream),
                border = BorderStroke(1.5.dp, BorderGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 20.dp)
                ) {
                    Text(
                        text = "Tasks completed",
                        fontSize = 14.sp,
                        color = TextMutedBrown
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completedThisWeek",
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        color = ForestGreen
                    )
                    Text(
                        text = "chores finished this period",
                        fontSize = 13.sp,
                        color = TextMutedBrown
                    )
                }
            }
        }

        item {
            Text(
                text = "Rankings",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextBrown,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
        }

        itemsIndexed(leaderboard, key = { _, entry -> entry.member.id }) { index, entry ->
            LeaderboardRow(
                rank = index + 1,
                entry = entry
            )
        }
    }
}

@Composable
private fun LeaderboardRow(
    rank: Int,
    entry: LeaderboardEntry
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = CardCream),
        border = BorderStroke(
            1.5.dp,
            if (rank == 1) GoldYellow else BorderGreen.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (rank == 1) GoldYellow else GoldYellowLight)
            ) {
                Text(
                    text = "$rank",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = TextBrown
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            MemberAvatar(
                name = entry.member.name,
                avatarColorHex = entry.member.avatarColor,
                isActive = entry.member.isActive,
                size = 44.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.member.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = TextBrown
                )
                Text(
                    text = "${entry.tasksDone} tasks done",
                    fontSize = 13.sp,
                    color = TextMutedBrown
                )
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                color = GoldYellow
            ) {
                Text(
                    text = "★ ${entry.points}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = TextBrown,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Report")
@Composable
private fun ReportScreenPreview() {
    FamilyHubTheme {
        ReportScreen()
    }
}
