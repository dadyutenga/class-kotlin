package com.biglitecode.familyhub.data.repository

import com.biglitecode.familyhub.data.model.Complaint
import com.biglitecode.familyhub.data.model.ComplaintStatus
import com.biglitecode.familyhub.data.model.FamilyGroup
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.model.Feedback
import com.biglitecode.familyhub.data.model.Task
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.data.session.SessionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
 * In-memory sample data so the UI is fully testable before Room/Supabase.
 */
object FakeTaskRepository : TaskRepository {

    private val now = System.currentTimeMillis()
    private val dayMs = TimeUnit.DAYS.toMillis(1)

    private const val FALLBACK_USER_ID = "m1"

    private val sampleMembers = listOf(
        FamilyMember(
            id = "m1",
            name = "Alex",
            role = FamilyRole.PARENT,
            avatarColor = "#2F6B44",
            isActive = true,
            // ACTION_DIAL only — no CALL_PHONE permission required.
            phoneNumber = "+255700000000",
            email = "alex@familyhub.app"
        ),
        FamilyMember(
            id = "m2",
            name = "Sam",
            role = FamilyRole.PARENT,
            avatarColor = "#F5C242",
            isActive = true,
            phoneNumber = "+255700000001",
            email = "sam@familyhub.app"
        ),
        FamilyMember(
            id = "m3",
            name = "Jordan",
            role = FamilyRole.CHILD,
            avatarColor = "#4A90D9",
            isActive = true,
            phoneNumber = "+255700000002",
            email = "jordan@familyhub.app"
        ),
        FamilyMember(
            id = "m4",
            name = "Riley",
            role = FamilyRole.CHILD,
            avatarColor = "#E05C5C",
            isActive = false,
            phoneNumber = "+255700000003",
            email = "riley@familyhub.app"
        ),
        FamilyMember(
            id = "m5",
            name = "Casey",
            role = FamilyRole.CHILD,
            avatarColor = "#9B59B6",
            isActive = true,
            phoneNumber = "+255700000004",
            email = "casey@familyhub.app"
        )
    )

    private val sampleTasks = listOf(
        Task(
            id = "t1",
            title = "Wash the dishes",
            description = "Load dishwasher and wipe counters after dinner.",
            assignedTo = "m3",
            assignedToName = "Jordan",
            assignedBy = "m1",
            dueDate = now + dayMs,
            status = TaskStatus.PENDING,
            createdAt = now - dayMs,
            rewardPoints = 15
        ),
        Task(
            id = "t2",
            title = "Take out trash",
            description = "Kitchen and bathroom bins. Remember recycling.",
            assignedTo = "m4",
            assignedToName = "Riley",
            assignedBy = "m2",
            dueDate = now,
            status = TaskStatus.PENDING,
            createdAt = now - 2 * dayMs,
            rewardPoints = 10
        ),
        Task(
            id = "t3",
            title = "Walk the dog",
            description = "Morning walk around the block (20 min).",
            assignedTo = "m3",
            assignedToName = "Jordan",
            assignedBy = "m1",
            dueDate = now - dayMs,
            status = TaskStatus.DONE,
            createdAt = now - 3 * dayMs,
            rewardPoints = 20
        ),
        Task(
            id = "t4",
            title = "Vacuum living room",
            description = "Move cushions and vacuum under the sofa.",
            assignedTo = "m5",
            assignedToName = "Casey",
            assignedBy = "m2",
            dueDate = now + 2 * dayMs,
            status = TaskStatus.PENDING,
            createdAt = now - dayMs / 2,
            rewardPoints = 25
        ),
        Task(
            id = "t5",
            title = "Fold laundry",
            description = "Fold clean clothes and put them away.",
            assignedTo = "m4",
            assignedToName = "Riley",
            assignedBy = "m1",
            dueDate = now - 2 * dayMs,
            status = TaskStatus.OVERDUE,
            createdAt = now - 4 * dayMs,
            rewardPoints = 15
        ),
        Task(
            id = "t6",
            title = "Water plants",
            description = "Living room and balcony plants.",
            assignedTo = "m5",
            assignedToName = "Casey",
            assignedBy = "m2",
            dueDate = now,
            status = TaskStatus.DONE,
            createdAt = now - dayMs,
            rewardPoints = 10
        ),
        Task(
            id = "t7",
            title = "Set the table",
            description = "Plates, glasses, and napkins for dinner.",
            assignedTo = "m3",
            assignedToName = "Jordan",
            assignedBy = "m1",
            dueDate = now + dayMs / 4,
            status = TaskStatus.PENDING,
            createdAt = now,
            rewardPoints = 5
        ),
        Task(
            id = "t8",
            title = "Clean bedroom",
            description = "Make bed, tidy desk, put clothes in hamper.",
            assignedTo = "m4",
            assignedToName = "Riley",
            assignedBy = "m2",
            dueDate = now + 3 * dayMs,
            status = TaskStatus.DONE,
            createdAt = now - 5 * dayMs,
            rewardPoints = 30
        )
    )

    private val sampleComplaints = listOf(
        Complaint(
            id = "c1",
            subject = "Dishes left overnight",
            description = "Jordan left dirty plates in the sink after dinner twice this week.",
            submittedBy = "m1",
            submittedAt = now - 2 * dayMs,
            status = ComplaintStatus.OPEN
        ),
        Complaint(
            id = "c2",
            subject = "Trash not taken out",
            description = "Recycling bin was full and nobody emptied it on collection day.",
            submittedBy = "m2",
            submittedAt = now - 5 * dayMs,
            status = ComplaintStatus.RESOLVED
        ),
        Complaint(
            id = "c3",
            subject = "Dog walk skipped",
            description = "Morning walk was missed on Monday without notice.",
            submittedBy = "m1",
            submittedAt = now - dayMs,
            status = ComplaintStatus.OPEN
        )
    )

    private val _tasks = MutableStateFlow(sampleTasks)
    private val _members = MutableStateFlow(sampleMembers)
    private val _feedback = MutableStateFlow<List<Feedback>>(emptyList())
    private val _complaints = MutableStateFlow(sampleComplaints)
    private val _familyGroup = MutableStateFlow(
        FamilyGroup(
            id = "fg1",
            name = "My Family",
            inviteCode = "FAM-HUB-7K2"
        )
    )

    override fun observeTasks(): Flow<List<Task>> = _tasks.asStateFlow()

    override fun observeMembers(): Flow<List<FamilyMember>> = _members.asStateFlow()

    override fun observeFeedback(): Flow<List<Feedback>> = _feedback.asStateFlow()

    override fun observeComplaints(): Flow<List<Complaint>> = _complaints.asStateFlow()

    override fun observeFamilyGroup(): Flow<FamilyGroup> = _familyGroup.asStateFlow()

    override fun currentUserId(): String =
        SessionManager.currentUser.value?.id ?: FALLBACK_USER_ID

    override suspend fun getTaskById(id: String): Task? =
        _tasks.value.find { it.id == id }

    override suspend fun addTask(task: Task) {
        val withId = if (task.id.isBlank()) {
            task.copy(id = UUID.randomUUID().toString())
        } else {
            task
        }
        _tasks.update { it + withId }
    }

    override suspend fun updateTask(task: Task) {
        _tasks.update { list ->
            list.map { if (it.id == task.id) task else it }
        }
    }

    override suspend fun markComplete(taskId: String) {
        updateStatus(taskId, TaskStatus.DONE)
    }

    override suspend fun updateStatus(taskId: String, status: TaskStatus) {
        _tasks.update { list ->
            list.map { if (it.id == taskId) it.copy(status = status) else it }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        _tasks.update { list -> list.filterNot { it.id == taskId } }
    }

    override suspend fun removeMember(memberId: String) {
        _members.update { list -> list.filterNot { it.id == memberId } }
        // Drop tasks that belonged only to the removed member.
        _tasks.update { list -> list.filterNot { it.assignedTo == memberId } }
    }

    override suspend fun updateFamilyGroupName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) return
        _familyGroup.update { it.copy(name = trimmed) }
    }

    override suspend fun submitFeedback(feedback: Feedback) {
        val withId = if (feedback.id.isBlank()) {
            feedback.copy(id = UUID.randomUUID().toString())
        } else {
            feedback
        }
        _feedback.update { existing ->
            // One feedback entry per task — replace if resubmitted.
            existing.filterNot { it.taskId == withId.taskId } + withId
        }
    }

    override suspend fun submitComplaint(complaint: Complaint) {
        val withId = if (complaint.id.isBlank()) {
            complaint.copy(id = UUID.randomUUID().toString())
        } else {
            complaint
        }
        _complaints.update { listOf(withId) + it }
    }
}
