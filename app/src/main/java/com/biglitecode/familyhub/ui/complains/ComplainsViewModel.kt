package com.biglitecode.familyhub.ui.complains

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.model.Complaint
import com.biglitecode.familyhub.data.model.ComplaintStatus
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import com.biglitecode.familyhub.data.repository.TaskRepository
import com.biglitecode.familyhub.data.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ComplainsViewModel(
    private val repository: TaskRepository = FakeTaskRepository
) : ViewModel() {

    val complaints: StateFlow<List<Complaint>> = repository.observeComplaints()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _form = MutableStateFlow(ComplaintFormState())
    val form: StateFlow<ComplaintFormState> = _form.asStateFlow()

    fun setSubject(value: String) {
        _form.update { it.copy(subject = value) }
    }

    fun setDescription(value: String) {
        _form.update { it.copy(description = value) }
    }

    fun submit() {
        val current = _form.value
        if (current.subject.isBlank() || current.description.isBlank()) return
        viewModelScope.launch {
            val userId = SessionManager.currentUser.value?.id
                ?: repository.currentUserId()
            repository.submitComplaint(
                Complaint(
                    id = "",
                    subject = current.subject.trim(),
                    description = current.description.trim(),
                    submittedBy = userId,
                    submittedAt = System.currentTimeMillis(),
                    status = ComplaintStatus.OPEN
                )
            )
            _form.value = ComplaintFormState()
        }
    }
}

data class ComplaintFormState(
    val subject: String = "",
    val description: String = ""
)
