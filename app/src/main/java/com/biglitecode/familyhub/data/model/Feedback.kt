package com.biglitecode.familyhub.data.model

/**
 * Star rating + optional comment for a completed task.
 */
data class Feedback(
    val id: String,
    val taskId: String,
    val userId: String,
    val comment: String,
    val rating: Int,
    val createdAt: Long = System.currentTimeMillis()
)
