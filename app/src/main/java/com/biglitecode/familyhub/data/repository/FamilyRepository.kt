package com.biglitecode.familyhub.data.repository

import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.data.remote.SupabaseClientProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching family member rows. RLS guarantees that only members
 * of the current user's family group are returned.
 */
class FamilyRepository {

    private val postgrest = SupabaseClientProvider.postgrest

    /**
     * Returns every member in the current user's family group.
     */
    suspend fun getFamilyMembers(): Result<List<FamilyMember>> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest.from("family_members").select().decodeList<FamilyMember>()
        }
    }

    /**
     * Returns the children of the current user's family group.
     */
    suspend fun getChildren(): Result<List<FamilyMember>> = withContext(Dispatchers.IO) {
        runCatching {
            postgrest.from("family_members").select {
                filter {
                    eq("role", FamilyRole.CHILD.name)
                }
            }.decodeList<FamilyMember>()
        }
    }
}
