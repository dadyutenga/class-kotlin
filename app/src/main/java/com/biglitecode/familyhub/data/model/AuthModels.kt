package com.biglitecode.familyhub.data.model

/**
 * Role chosen during sign-up. Maps later to Supabase profile claims.
 */
enum class FamilyRole {
    PARENT,
    CHILD
}

/**
 * Whether the user is creating a new family group or joining with a code.
 */
enum class FamilyGroupOption {
    CREATE,
    JOIN
}
