package com.biglitecode.familyhub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.biglitecode.familyhub.data.session.SessionManager
import com.biglitecode.familyhub.ui.signup.SignUpScreen
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme

/**
 * Sign-up flow entry point. UI is [SignUpScreen]; auth will later call Supabase.
 */
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilyHubTheme {
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                SignUpScreen(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onSignUpClick = { _name, _email, _password, _confirmPassword, _role, _familyGroupOption, _familyGroupCode ->
                        errorMessage = null
                        isLoading = true
                        // TODO: replace with real Supabase Auth signUp
                        //   - create user with email/password
                        //   - store profile: name, role, family group create/join (familyGroupCode)
                        //   - on failure: isLoading = false; errorMessage = ...
                        // Placeholder success so navigation can be exercised:
                        SessionManager.setFromSignup(_name, _email, _role)
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    },
                    onLoginClick = {
                        // LoginActivity started us — pop back to it.
                        finish()
                    }
                )
            }
        }
    }
}
