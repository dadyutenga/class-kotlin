package com.biglitecode.familyhub.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory settings toggles (not persisted yet).
 */
class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun setDarkMode(enabled: Boolean) {
        _uiState.update { it.copy(darkMode = enabled) }
    }

    fun setPushNotifications(enabled: Boolean) {
        _uiState.update { it.copy(pushNotifications = enabled) }
    }

    fun setSmsReminders(enabled: Boolean) {
        _uiState.update { it.copy(smsReminders = enabled) }
    }
}

data class SettingsUiState(
    val darkMode: Boolean = false,
    val pushNotifications: Boolean = true,
    val smsReminders: Boolean = false,
    val language: String = "English",
    val appVersion: String = "1.0.0"
)
