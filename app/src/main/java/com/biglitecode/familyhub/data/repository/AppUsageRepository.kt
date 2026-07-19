package com.biglitecode.familyhub.data.repository

import android.content.Context
import com.biglitecode.familyhub.data.model.AppUsage
import com.biglitecode.familyhub.data.remote.SupabaseClientProvider
import com.biglitecode.familyhub.data.usage.UsageStatsCollector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository responsible for collecting local app usage and syncing it to the
 * Supabase `app_usage` table.
 */
class AppUsageRepository(private val context: Context) {

    private val postgrest = SupabaseClientProvider.postgrest

    /**
     * Collects today's usage on this device and upserts it into Supabase.
     *
     * The upsert is idempotent because of the unique constraint on
     * (family_member_id, package_name, usage_date).
     */
    suspend fun syncTodayUsage(
        familyMemberId: String,
        familyGroupId: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            val usage = UsageStatsCollector.collectTodayUsage(
                context = context,
                familyMemberId = familyMemberId,
                familyGroupId = familyGroupId
            )
            if (usage.isNotEmpty()) {
                postgrest.from("app_usage").upsert(usage) {
                    onConflict = "family_member_id,package_name,usage_date"
                }
            }
        }
    }

    /**
     * Returns app usage for a single family member on the given date.
     */
    suspend fun getUsageForMember(
        familyMemberId: String,
        date: String
    ): Result<List<AppUsage>> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest.from("app_usage").select {
                filter {
                    eq("family_member_id", familyMemberId)
                    eq("usage_date", date)
                }
            }.decodeList<AppUsage>()
        }
    }

    /**
     * Returns all app usage for the current user's family group on the given date.
     *
     * RLS enforces the family group boundary server-side; the UI groups the rows
     * client-side by [family_member_id] when a parent selects a child.
     */
    suspend fun getUsageForGroup(
        familyGroupId: String,
        date: String
    ): Result<List<AppUsage>> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest.from("app_usage").select {
                filter {
                    eq("family_group_id", familyGroupId)
                    eq("usage_date", date)
                }
            }.decodeList<AppUsage>()
        }
    }
}
