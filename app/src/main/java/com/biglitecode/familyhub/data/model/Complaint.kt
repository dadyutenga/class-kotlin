package com.biglitecode.familyhub.data.model

enum class ComplaintStatus {
    OPEN,
    RESOLVED
}

/**
 * Family complaint / issue raised by a member.
 */
data class Complaint(
    val id: String,
    val subject: String,
    val description: String,
    val submittedBy: String,
    val submittedAt: Long,
    val status: ComplaintStatus
)
