package com.biglitecode.familyhub.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.model.Feedback
import com.biglitecode.familyhub.data.model.Task
import com.biglitecode.familyhub.data.model.TaskStatus
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import com.biglitecode.familyhub.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FeedbackViewModel(
    private val repository: TaskRepository = FakeTaskRepository
) : ViewModel() {

    val completedTasks: StateFlow<List<Task>> = repository.observeTasks()
        .map { list -> list.filter { it.status == TaskStatus.DONE } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _drafts = MutableStateFlow<Map<String, FeedbackDraft>>(emptyMap())
    val drafts: StateFlow<Map<String, FeedbackDraft>> = _drafts.asStateFlow()

    val submitted: StateFlow<List<Feedback>> = repository.observeFeedback()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val cards: StateFlow<List<FeedbackCardUi>> = combine(
        completedTasks,
        _drafts,
        submitted
    ) { tasks, drafts, feedbackList ->
        tasks.map { task ->
            val existing = feedbackList.find { it.taskId == task.id }
            val draft = drafts[task.id]
            FeedbackCardUi(
                task = task,
                rating = draft?.rating ?: existing?.rating ?: 0,
                comment = draft?.comment ?: existing?.comment.orEmpty(),
                alreadySubmitted = existing != null && draft == null
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setRating(taskId: String, rating: Int) {
        _drafts.update { map ->
            val prev = map[taskId] ?: FeedbackDraft()
            map + (taskId to prev.copy(rating = rating.coerceIn(1, 5)))
        }
    }

    fun setComment(taskId: String, comment: String) {
        _drafts.update { map ->
            val prev = map[taskId] ?: FeedbackDraft()
            map + (taskId to prev.copy(comment = comment))
        }
    }

    fun submitAll() {
        viewModelScope.launch {
            val userId = repository.currentUserId()
            completedTasks.value.forEach { task ->
                val draft = _drafts.value[task.id] ?: return@forEach
                if (draft.rating <= 0) return@forEach
                repository.submitFeedback(
                    Feedback(
                        id = "",
                        taskId = task.id,
                        userId = userId,
                        comment = draft.comment.trim(),
                        rating = draft.rating
                    )
                )
            }
            _drafts.value = emptyMap()
        }
    }

    fun submitForTask(taskId: String) {
        viewModelScope.launch {
            val draft = _drafts.value[taskId] ?: return@launch
            if (draft.rating <= 0) return@launch
            repository.submitFeedback(
                Feedback(
                    id = "",
                    taskId = taskId,
                    userId = repository.currentUserId(),
                    comment = draft.comment.trim(),
                    rating = draft.rating
                )
            )
            _drafts.update { it - taskId }
        }
    }
}

data class FeedbackDraft(
    val rating: Int = 0,
    val comment: String = ""
)

data class FeedbackCardUi(
    val task: Task,
    val rating: Int,
    val comment: String,
    val alreadySubmitted: Boolean
)
