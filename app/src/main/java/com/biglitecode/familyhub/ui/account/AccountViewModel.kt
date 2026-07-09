package com.biglitecode.familyhub.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.model.FamilyGroup
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import com.biglitecode.familyhub.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AccountViewModel(
    private val repository: TaskRepository = FakeTaskRepository
) : ViewModel() {

    val members: StateFlow<List<FamilyMember>> = repository.observeMembers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val familyGroup: StateFlow<FamilyGroup?> = repository.observeFamilyGroup()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val currentUser: StateFlow<FamilyMember?> = repository.observeMembers()
        .map { list ->
            list.firstOrNull { it.id == repository.currentUserId() } ?: list.firstOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}
