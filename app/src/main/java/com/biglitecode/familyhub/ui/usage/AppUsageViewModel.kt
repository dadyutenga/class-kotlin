package com.biglitecode.familyhub.ui.usage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.biglitecode.familyhub.data.local.SessionStore
import com.biglitecode.familyhub.data.model.AppUsage
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.repository.AppUsageRepository
import com.biglitecode.familyhub.data.repository.FamilyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * UI state for [AppUsageScreen].
 */
data class AppUsageUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val selectedDate: String = today(),
    val isParent: Boolean = false,
    val currentMemberId: String? = null,
    val children: List<FamilyMember> = emptyList(),
    val selectedChildId: String? = null,
    val usage: List<AppUsage> = emptyList()
)

class AppUsageViewModel(
    private val sessionStore: SessionStore,
    private val appUsageRepository: AppUsageRepository,
    private val familyRepository: FamilyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppUsageUiState())
    val uiState: StateFlow<AppUsageUiState> = _uiState

    private val dateOptions: List<String> = lastSevenDays()

    init {
        loadInitialSession()
    }

    private fun loadInitialSession() {
        viewModelScope.launch {
            val memberId = sessionStore.familyMemberId.firstOrNull()
            val groupId = sessionStore.familyGroupId.firstOrNull()
            val role = sessionStore.role.firstOrNull()

            if (memberId == null || groupId == null || role == null) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Session not found. Please sign in again."
                )
                return@launch
            }

            val isParent = role == FamilyRole.PARENT
            _uiState.value = _uiState.value.copy(
                currentMemberId = memberId,
                isParent = isParent,
                selectedChildId = if (isParent) null else memberId
            )

            if (isParent) {
                fetchChildren()
            } else {
                loadUsage()
            }
        }
    }

    fun selectDate(date: String) {
        if (date == _uiState.value.selectedDate) return
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadUsage()
    }

    fun selectChild(childId: String) {
        if (childId == _uiState.value.selectedChildId) return
        _uiState.value = _uiState.value.copy(selectedChildId = childId)
        loadUsage()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            syncIfChild()
            loadUsage()
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }

    private fun fetchChildren() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            familyRepository.getChildren().fold(
                onSuccess = { children ->
                    val selected = children.firstOrNull()?.id ?: _uiState.value.currentMemberId
                    _uiState.value = _uiState.value.copy(
                        children = children,
                        selectedChildId = selected,
                        isLoading = false
                    )
                    loadUsage()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load children."
                    )
                }
            )
        }
    }

    private fun loadUsage() {
        viewModelScope.launch {
            val state = _uiState.value
            val memberId = state.selectedChildId ?: state.currentMemberId ?: return@launch

            _uiState.value = state.copy(isLoading = true, errorMessage = null)

            val result = if (state.isParent) {
                appUsageRepository.getUsageForMember(memberId, state.selectedDate)
            } else {
                appUsageRepository.getUsageForMember(memberId, state.selectedDate)
            }

            result.fold(
                onSuccess = { usage ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        usage = usage.sortedByDescending { it.total_time_ms }
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = error.localizedMessage ?: "Failed to load usage."
                    )
                }
            )
        }
    }

    private suspend fun syncIfChild() {
        val state = _uiState.value
        if (state.isParent) return
        val memberId = state.currentMemberId ?: return
        val groupId = sessionStore.familyGroupId.firstOrNull() ?: return
        appUsageRepository.syncTodayUsage(memberId, groupId)
    }

    fun getDateOptions(): List<String> = dateOptions

    class Factory(
        context: android.content.Context,
        private val sessionStore: SessionStore = SessionStore(context),
        private val appUsageRepository: AppUsageRepository = AppUsageRepository(context),
        private val familyRepository: FamilyRepository = FamilyRepository()
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == AppUsageViewModel::class.java)
            return AppUsageViewModel(
                sessionStore,
                appUsageRepository,
                familyRepository
            ) as T
        }
    }
}

private fun today(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    return formatter.format(Calendar.getInstance().timeInMillis)
}

private fun lastSevenDays(): List<String> {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val calendar = Calendar.getInstance()
    return buildList {
        for (i in 0 until 7) {
            add(formatter.format(calendar.timeInMillis))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
    }
}
