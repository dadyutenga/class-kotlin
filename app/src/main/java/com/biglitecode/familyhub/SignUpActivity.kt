package com.biglitecode.familyhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.components.FamilyLogoBadge
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

// TODO: implement the real sign-up flow (Supabase auth).
class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FamilyHubTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PlaceholderScreen(title = "Reset Password")
                }
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Info card — cream surface with a green border (active/primary card).
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .width(280.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                // Logo badge inside a gold ring — circular avatar chip style.
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(GoldYellowLight)
                        .border(width = 2.dp, color = GoldYellow, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    FamilyLogoBadge(
                        badgeSize = 96.dp,
                        logoSize = 70.dp,
                        cornerRadius = 24.dp,
                        surfaceColor = Color.Transparent
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextBrown,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "coming soon",
                    color = TextMutedBrown,
                    fontSize = 14.sp
                )
            }
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