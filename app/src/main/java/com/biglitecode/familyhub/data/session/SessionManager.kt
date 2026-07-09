package com.biglitecode.familyhub.data.session

import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.repository.FakeTaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

/**
 * In-memory session for the signed-in family member.
 * Set after login/signup; read from screens for role-based UI.
 *
 * Demo tip: call [setDemoParent] / [setDemoChild] to flip roles without re-auth.
 */
object SessionManager {

    private val _currentUser = MutableStateFlow<FamilyMember?>(null)
    val currentUser: StateFlow<FamilyMember?> = _currentUser.asStateFlow()

    fun setCurrentUser(user: FamilyMember) {
        _currentUser.value = user
    }

    fun clear() {
        _currentUser.value = null
    }

    /** Resolve a sample member by id from [FakeTaskRepository] and set session. */
    fun setCurrentUserById(memberId: String) {
        val member = runBlocking {
            FakeTaskRepository.observeMembers().first().find { it.id == memberId }
        }
        if (member != null) {
            _currentUser.value = member
        }
    }

    /** Parent/Guardian demo user (Alex, m1). */
    fun setDemoParent() = setCurrentUserById("m1")

    /** Child/Member demo user (Jordan, m3). */
    fun setDemoChild() = setCurrentUserById("m3")

    /**
     * Login placeholder: match email to a sample member, else default to parent.
     * Emails containing "child" or matching a child sample name resolve as CHILD.
     */
    fun setFromLoginEmail(email: String) {
        val members = runBlocking { FakeTaskRepository.observeMembers().first() }
        val normalized = email.trim().lowercase()
        val match = members.find { it.email?.equals(normalized, ignoreCase = true) == true }
            ?: members.find {
                normalized.contains(it.name.lowercase()) ||
                    (it.role == FamilyRole.CHILD &&
                        (normalized.contains("child") || normalized.contains("jordan") ||
                            normalized.contains("riley") || normalized.contains("casey")))
            }
            ?: members.find { it.role == FamilyRole.PARENT }
            ?: members.firstOrNull()
        if (match != null) {
            _currentUser.value = match
        }
    }

    /**
     * Signup placeholder: map form role/name/email onto an existing sample member
     * of that role, or build a temporary session profile.
     */
    fun setFromSignup(name: String, email: String, role: FamilyRole) {
        val members = runBlocking { FakeTaskRepository.observeMembers().first() }
        val match = members.find { it.role == role }
            ?: members.firstOrNull()
        if (match != null) {
            _currentUser.value = match.copy(
                name = name.ifBlank { match.name },
                email = email.ifBlank { match.email }
            )
        } else {
            _currentUser.value = FamilyMember(
                id = "session_user",
                name = name.ifBlank { "Member" },
                role = role,
                email = email.ifBlank { null },
                avatarColor = if (role == FamilyRole.PARENT) "#2F6B44" else "#4A90D9"
            )
        }
    }
}
