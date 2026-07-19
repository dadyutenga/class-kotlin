package com.biglitecode.familyhub.data.remote

import com.biglitecode.familyhub.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Singleton Supabase client used by repositories.
 *
 * Replace the BuildConfig placeholders with your real Supabase project URL and
 * anon key before shipping (see app/build.gradle.kts defaultConfig).
 */
object SupabaseClientProvider {

    val client: SupabaseClient by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY
        ) {
            install(Postgrest)
            install(Auth)
        }
    }

    val postgrest: Postgrest
        get() = client.pluginManager.getPlugin(Postgrest)
}
