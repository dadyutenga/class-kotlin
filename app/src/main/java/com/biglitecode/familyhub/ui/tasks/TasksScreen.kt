package com.biglitecode.familyhub.ui.tasks

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.ui.components.TaskCard
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

enum class TaskFilter { ALL, PENDING, DONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = viewModel(),
    onTaskClick: (String) -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    var filter by remember { mutableStateOf(TaskFilter.ALL) }
    var showAddDialog by remember { mutableStateOf(false) }

    val filtered = when (filter) {
        TaskFilter.ALL -> tasks
        TaskFilter.PENDING -> tasks.filter {
            it.status == TaskStatus.PENDING || it.status == TaskStatus.OVERDUE
        }
        TaskFilter.DONE -> tasks.filter { it.status == TaskStatus.DONE }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = ForestGreen,
                contentColor = CardCream
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add task")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            Text(
                text = "Tasks",
                fontWeight = FontWeight.Bold,
                fontSize = 26.sp,
                color = TextBrown,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                TaskFilter.entries.forEach { option ->
                    val selected = filter == option
                    FilterChip(
                        selected = selected,
                        onClick = { filter = option },
                        label = {
                            Text(
                                text = when (option) {
                                    TaskFilter.ALL -> "All"
                                    TaskFilter.PENDING -> "Pending"
                                    TaskFilter.DONE -> "Done"
                                }
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ForestGreen,
                            selectedLabelColor = CardCream,
                            containerColor = CardCream,
                            labelColor = TextBrown
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filtered, key = { it.id }) { task ->
                    TaskCard(
                        task = task,
                        onClick = { onTaskClick(task.id) },
                        onToggleComplete = { viewModel.toggleComplete(task) }
                    )
                }
                if (filtered.isEmpty()) {
                    item {
                        Text(
                            text = "No tasks in this filter.",
                            color = TextMutedBrown,
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(72.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            members = members,
            onDismiss = { showAddDialog = false },
            onConfirm = { title, description, assignee, dueDate, points ->
                viewModel.addTask(title, description, assignee, dueDate, points)
                showAddDialog = false
            }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Tasks")
@Composable
private fun TasksScreenPreview() {
    FamilyHubTheme {
        TasksScreen()
    }
}
