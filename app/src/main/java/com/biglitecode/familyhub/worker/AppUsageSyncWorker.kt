package com.biglitecode.familyhub.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.biglitecode.familyhub.data.local.SessionStore
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.repository.AppUsageRepository
import com.biglitecode.familyhub.utils.UsageStatsPermissionHelper
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

/**
 * Periodic worker that syncs today's app usage for the currently signed-in
 * CHILD family member.
 *
 * It only runs when the device has a network connection and the usage-stats
 * permission has been granted.
 */
class AppUsageSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val sessionStore = SessionStore(context)
    private val repository = AppUsageRepository(context)

    override suspend fun doWork(): Result {
        if (!UsageStatsPermissionHelper.hasUsageStatsPermission(applicationContext)) {
            return Result.success()
        }

        val memberId = sessionStore.familyMemberId.firstOrNull()
        val groupId = sessionStore.familyGroupId.firstOrNull()
        val role = sessionStore.role.firstOrNull()

        if (memberId == null || groupId == null || role != FamilyRole.CHILD) {
            return Result.success()
        }

        return repository.syncTodayUsage(memberId, groupId).fold(
            onSuccess = { Result.success() },
            onFailure = { Result.retry() }
        )
    }

    companion object {
        private const val WORK_NAME = "app_usage_sync"

        /**
         * Schedules a single ~45 minute periodic sync if one is not already
         * scheduled.
         */
        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(false)
                .build()

            val request = PeriodicWorkRequestBuilder<AppUsageSyncWorker>(
                repeatInterval = 45,
                repeatIntervalTimeUnit = TimeUnit.MINUTES
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}
