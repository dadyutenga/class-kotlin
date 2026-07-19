package com.biglitecode.familyhub.data.model

import kotlinx.serialization.Serializable

/**
 * A member of a family group, matching the Supabase `family_members` table.
 */
@Serializable
data class FamilyMember(
    val id: String,
    val user_id: String? = null,
    val name: String,
    val role: FamilyRole,
    val avatar_color: String? = null,
    val phone_number: String? = null,
    val email: String,
    val family_group_id: String
)

enum class FamilyRole {
    PARENT,
    CHILD
}
