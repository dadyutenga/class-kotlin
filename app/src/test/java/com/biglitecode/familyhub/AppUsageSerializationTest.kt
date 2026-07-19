package com.biglitecode.familyhub

import com.biglitecode.familyhub.data.model.AppUsage
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AppUsageSerializationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `serializes to snake_case keys matching Supabase columns`() {
        val usage = AppUsage(
            id = "m1_com.example.app_2026-07-19",
            family_member_id = "m1",
            family_group_id = "fg1",
            package_name = "com.example.app",
            app_name = "Example App",
            usage_date = "2026-07-19",
            total_time_ms = 5_040_000,
            last_time_used = 1_000_000,
            synced_at = 2_000_000
        )

        val encoded = json.encodeToString(AppUsage.serializer(), usage)

        assertTrue(encoded.contains(""""family_member_id":"m1""""))
        assertTrue(encoded.contains(""""package_name":"com.example.app""""))
        assertTrue(encoded.contains(""""usage_date":"2026-07-19""""))
    }

    @Test
    fun `default synced_at uses current time`() {
        val before = System.currentTimeMillis()
        val usage = AppUsage(
            id = "id",
            family_member_id = "m1",
            family_group_id = "fg1",
            package_name = "com.example.app",
            app_name = "Example",
            usage_date = "2026-07-19",
            total_time_ms = 0,
            last_time_used = 0
        )
        val after = System.currentTimeMillis()

        assertTrue(usage.synced_at in before..after)
    }

    @Test
    fun `deserializes from Supabase JSON`() {
        val payload = """
            {
                "id": "m1_com.example.app_2026-07-19",
                "family_member_id": "m1",
                "family_group_id": "fg1",
                "package_name": "com.example.app",
                "app_name": "Example App",
                "usage_date": "2026-07-19",
                "total_time_ms": 5040000,
                "last_time_used": 1000000,
                "synced_at": 2000000
            }
        """.trimIndent()

        val usage = json.decodeFromString(AppUsage.serializer(), payload)

        assertEquals("m1", usage.family_member_id)
        assertEquals("com.example.app", usage.package_name)
        assertEquals(5_040_000, usage.total_time_ms)
    }
}
