package com.biglitecode.familyhub.data.model

/**
 * A family chore / task assigned to a member.
 */
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val assignedTo: String,
    val assignedToName: String,
    val assignedBy: String,
    val dueDate: Long,
    val status: TaskStatus,
    val createdAt: Long,
    val rewardPoints: Int = 10
)

enum class TaskStatus {
    PENDING,
    DONE,
    OVERDUE
}
