package com.biglitecode.familyhub.data.model

import kotlinx.serialization.Serializable

/**
 * App usage row synced between the device and the Supabase `app_usage` table.
 *
 * The property names match the database column names so kotlinx.serialization
 * maps them directly without @SerialName annotations.
 */
@Serializable
data class AppUsage(
    val id: String,
    val family_member_id: String,
    val family_group_id: String,
    val package_name: String,
    val app_name: String,
    val usage_date: String,       // "YYYY-MM-DD"
    val total_time_ms: Long,
    val last_time_used: Long,
    val synced_at: Long = System.currentTimeMillis()
)
