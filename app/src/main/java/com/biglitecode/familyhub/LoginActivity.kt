package com.biglitecode.familyhub

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.biglitecode.familyhub.ui.login.LoginScreen
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme

class LoginActivity : ComponentActivity() {

    private fun isInternetAvailable(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val capabilities = cm.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamilyHubTheme {
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                LoginScreen(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onLoginClick = { email, password ->
                        if (!isInternetAvailable()) {
                            errorMessage = "No internet connection. Check your network and try again."
                        } else {
                            errorMessage = null
                            isLoading = true
                            // TODO: replace with real Supabase auth call.
                            // For now, placeholder success that routes to Home.
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    },
                    onSignUpClick = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    onForgotPasswordClick = {
                        startActivity(Intent(this, ResetPasswordActivity::class.java))
                    }
                )
            }
        }
    }
}
