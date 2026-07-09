package com.biglitecode.familyhub.data.model

/**
 * Lightweight family group metadata for Account / invite code.
 */
data class FamilyGroup(
    val id: String,
    val name: String,
    val inviteCode: String
)
