package com.biglitecode.familyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.biglitecode.familyhub.ui.resetpassword.ResetPasswordScreen
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Password-reset entry point. UI is [ResetPasswordScreen]; will later call Supabase.
 */
class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyHubTheme {
                var isLoading by remember { mutableStateOf(false) }
                var isSuccess by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf<String?>(null) }
                val scope = rememberCoroutineScope()

                ResetPasswordScreen(
                    isLoading = isLoading,
                    isSuccess = isSuccess,
                    errorMessage = errorMessage,
                    onSendClick = { _email ->
                        errorMessage = null
                        isLoading = true
                        // TODO: replace with real Supabase Auth resetPasswordForEmail(email)
                        scope.launch {
                            delay(1_000)
                            isLoading = false
                            isSuccess = true
                        }
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }
}
