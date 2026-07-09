package com.biglitecode.familyhub.data.model

/**
 * A member of the family group shown on the dashboard and leaderboard.
 * [FamilyRole] is defined in [AuthModels].
 *
 * [phoneNumber] is used by Contact for ACTION_DIAL / ACTION_SENDTO (SMS).
 * ACTION_DIAL does not require CALL_PHONE runtime permission (unlike ACTION_CALL).
 */
data class FamilyMember(
    val id: String,
    val name: String,
    val role: FamilyRole,
    val avatarColor: String? = null,
    val isActive: Boolean = true,
    val phoneNumber: String? = null,
    val email: String? = null
)
