package com.biglitecode.familyhub.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.model.FamilyGroup
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import com.biglitecode.familyhub.data.repository.TaskRepository
import com.biglitecode.familyhub.data.session.SessionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AccountViewModel(
    private val repository: TaskRepository = FakeTaskRepository
) : ViewModel() {

    val members: StateFlow<List<FamilyMember>> = repository.observeMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val familyGroup: StateFlow<FamilyGroup?> = repository.observeFamilyGroup()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Signed-in user from [SessionManager] (role-based UI). */
    val currentUser: StateFlow<FamilyMember?> = SessionManager.currentUser

    fun removeMember(memberId: String) {
        viewModelScope.launch {
            repository.removeMember(memberId)
        }
    }
}
