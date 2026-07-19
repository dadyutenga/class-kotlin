package com.biglitecode.familyhub.data.usage

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import com.biglitecode.familyhub.data.model.AppUsage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

/**
 * Collects today's device app usage via [UsageStatsManager].
 */
object UsageStatsCollector {

    /**
     * Queries today's usage stats and returns them as [AppUsage] rows.
     *
     * The [familyMemberId] and [familyGroupId] are stamped onto every row so the
     * resulting list is ready to upsert into Supabase.
     */
    fun collectTodayUsage(
        context: Context,
        familyMemberId: String,
        familyGroupId: String
    ): List<AppUsage> {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE)
            as? UsageStatsManager ?: return emptyList()

        val packageManager = context.packageManager
        val ownPackageName = context.packageName
        val (startOfDay, now) = dayBoundsMillis()
        val usageDate = formatUsageDate(startOfDay)

        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            startOfDay,
            now
        ) ?: return emptyList()

        return stats
            .asSequence()
            .filter { it.totalTimeInForeground > 0 }
            .filter { it.packageName != ownPackageName }
            .map { stat ->
                val appName = resolveAppName(packageManager, stat.packageName)
                AppUsage(
                    id = "${familyMemberId}_${stat.packageName}_${usageDate}",
                    family_member_id = familyMemberId,
                    family_group_id = familyGroupId,
                    package_name = stat.packageName,
                    app_name = appName,
                    usage_date = usageDate,
                    total_time_ms = stat.totalTimeInForeground,
                    last_time_used = stat.lastTimeUsed,
                    synced_at = System.currentTimeMillis()
                )
            }
            .sortedByDescending { it.total_time_ms }
            .toList()
    }

    private fun resolveAppName(packageManager: PackageManager, packageName: String): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (_: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    private fun dayBoundsMillis(): Pair<Long, Long> {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = calendar.timeInMillis
        val now = System.currentTimeMillis()
        return start to now
    }

    private fun formatUsageDate(timestamp: Long): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return formatter.format(timestamp)
    }
}
