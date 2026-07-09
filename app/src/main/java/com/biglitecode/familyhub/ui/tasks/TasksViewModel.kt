package com.biglitecode.familyhub.ui.tasks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.Task
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import com.biglitecode.familyhub.data.repository.TaskRepository
import com.biglitecode.familyhub.util.NotificationHelper
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Shared ViewModel for Dashboard "Today's Tasks", Tasks tab, detail, and Report.
 * Uses [AndroidViewModel] so task-assigned notifications can use application context.
 */
class TasksViewModel(
    application: Application,
    private val repository: TaskRepository = FakeTaskRepository
) : AndroidViewModel(application) {

    val tasks: StateFlow<List<Task>> = repository.observeTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val members: StateFlow<List<FamilyMember>> = repository.observeMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val pendingCount: StateFlow<Int> = tasks
        .map { list -> list.count { it.status != TaskStatus.DONE } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val familyPoints: StateFlow<Int> = tasks
        .map { list ->
            list.filter { it.status == TaskStatus.DONE }.sumOf { it.rewardPoints }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    val completedThisWeek: StateFlow<Int> = tasks
        .map { list -> list.count { it.status == TaskStatus.DONE } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    /** Leaderboard rows: member + completed count + points. */
    val leaderboard: StateFlow<List<LeaderboardEntry>> = combine(tasks, members) { taskList, memberList ->
        memberList.map { member ->
            val done = taskList.filter { it.assignedTo == member.id && it.status == TaskStatus.DONE }
            LeaderboardEntry(
                member = member,
                tasksDone = done.size,
                points = done.sumOf { it.rewardPoints }
            )
        }.sortedByDescending { it.tasksDone }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun taskById(id: String): Task? = tasks.value.find { it.id == id }

    fun markComplete(taskId: String) {
        viewModelScope.launch {
            repository.markComplete(taskId)
        }
    }

    fun toggleComplete(task: Task) {
        viewModelScope.launch {
            val next = if (task.status == TaskStatus.DONE) TaskStatus.PENDING else TaskStatus.DONE
            repository.updateStatus(task.id, next)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun addTask(
        title: String,
        description: String,
        assignee: FamilyMember,
        dueDate: Long,
        rewardPoints: Int
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val trimmedTitle = title.trim()
            val assignerId = repository.currentUserId()
            repository.addTask(
                Task(
                    id = "",
                    title = trimmedTitle,
                    description = description.trim(),
                    assignedTo = assignee.id,
                    assignedToName = assignee.name,
                    assignedBy = assignerId,
                    dueDate = dueDate,
                    status = TaskStatus.PENDING,
                    createdAt = now,
                    rewardPoints = rewardPoints.coerceAtLeast(0)
                )
            )
            NotificationHelper.showTaskAssignedNotification(
                context = getApplication(),
                taskTitle = trimmedTitle,
                assignedToName = assignee.name
            )
        }
    }
}

data class LeaderboardEntry(
    val member: FamilyMember,
    val tasksDone: Int,
    val points: Int
)
