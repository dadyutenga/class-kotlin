package com.biglitecode.familyhub.data.repository

import com.biglitecode.familyhub.data.model.Complaint
import com.biglitecode.familyhub.data.model.FamilyGroup
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.Feedback
import com.biglitecode.familyhub.data.model.Task
import com.biglitecode.familyhub.data.model.TaskStatus
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over task / family-member data so Room or Supabase can replace
 * the in-memory fake without UI changes.
 */
interface TaskRepository {
    fun observeTasks(): Flow<List<Task>>
    fun observeMembers(): Flow<List<FamilyMember>>
    fun observeFeedback(): Flow<List<Feedback>>
    fun observeComplaints(): Flow<List<Complaint>>
    fun observeFamilyGroup(): Flow<FamilyGroup>
    /** Currently signed-in member id (fake: first parent). */
    fun currentUserId(): String

    suspend fun getTaskById(id: String): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun markComplete(taskId: String)
    suspend fun updateStatus(taskId: String, status: TaskStatus)
    suspend fun deleteTask(taskId: String)
    suspend fun removeMember(memberId: String)
    suspend fun updateFamilyGroupName(name: String)
    suspend fun submitFeedback(feedback: Feedback)
    suspend fun submitComplaint(complaint: Complaint)
}
