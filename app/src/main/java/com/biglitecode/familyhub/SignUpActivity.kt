package com.biglitecode.familyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.components.FamilyLogoBadge
import com.biglitecode.familyhub.ui.theme.FamilyBlue
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.TextGray

// TODO: implement the real sign-up flow (Supabase auth).
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamilyHubTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlaceholderScreen(title = "Sign Up")
                }
            }
        }
    }
}

// TODO: implement the real password-reset flow.
class ResetPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamilyHubTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlaceholderScreen(title = "Reset Password")
                }
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            FamilyLogoBadge(
                badgeSize = 96.dp,
                logoSize = 70.dp,
                cornerRadius = 24.dp,
                surfaceColor = FamilyBlue.copy(alpha = 0.12f)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "coming soon",
                color = TextGray,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Sign Up placeholder")
@Composable
private fun PlaceholderScreenPreview() {
    FamilyHubTheme {
        PlaceholderScreen(title = "Sign Up")
    }
}
