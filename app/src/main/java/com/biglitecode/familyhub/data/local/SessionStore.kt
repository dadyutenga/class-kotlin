package com.biglitecode.familyhub.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.biglitecode.familyhub.data.model.FamilyRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "family_hub_session")

/**
 * Lightweight local session store for the currently authenticated family member.
 *
 * A real sign-in flow should populate these values after the family_member row
 * is fetched from Supabase.
 */
class SessionStore(private val context: Context) {

    private val dataStore = context.dataStore

    val familyMemberId: Flow<String?> = dataStore.data
        .map { it[KEY_FAMILY_MEMBER_ID] }

    val familyGroupId: Flow<String?> = dataStore.data
        .map { it[KEY_FAMILY_GROUP_ID] }

    val role: Flow<FamilyRole?> = dataStore.data
        .map { prefs ->
            prefs[KEY_ROLE]?.let { runCatching { FamilyRole.valueOf(it) }.getOrNull() }
        }

    val usageStatsConsentShown: Flow<Boolean> = dataStore.data
        .map { it[KEY_USAGE_STATS_CONSENT_SHOWN] ?: false }

    suspend fun saveSession(
        familyMemberId: String,
        familyGroupId: String,
        role: FamilyRole
    ) {
        dataStore.edit { prefs ->
            prefs[KEY_FAMILY_MEMBER_ID] = familyMemberId
            prefs[KEY_FAMILY_GROUP_ID] = familyGroupId
            prefs[KEY_ROLE] = role.name
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    suspend fun markUsageStatsConsentShown() {
        dataStore.edit { prefs ->
            prefs[KEY_USAGE_STATS_CONSENT_SHOWN] = true
        }
    }

    companion object {
        private val KEY_FAMILY_MEMBER_ID = stringPreferencesKey("family_member_id")
        private val KEY_FAMILY_GROUP_ID = stringPreferencesKey("family_group_id")
        private val KEY_ROLE = stringPreferencesKey("role")
        private val KEY_USAGE_STATS_CONSENT_SHOWN = booleanPreferencesKey("usage_stats_consent_shown")
    }
}
