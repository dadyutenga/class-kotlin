package com.biglitecode.familyhub.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.components.FamilyLogoBadge
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.CreamBackground
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.ForestGreenLight
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

@Composable
fun LoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onLoginClick: (email: String, password: String) -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val cs = MaterialTheme.colorScheme

    // Field shape follows the theme's small shape scale.
    val fieldShape = MaterialTheme.shapes.small
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ForestGreen,
        unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
        cursorColor = ForestGreen,
        focusedLeadingIconColor = ForestGreen,
        unfocusedLeadingIconColor = TextMutedBrown,
        focusedLabelColor = ForestGreen,
        unfocusedLabelColor = TextMutedBrown
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Brand header (rounded pill on cream) ───────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(ForestGreen)
        ) {
            // Soft decorative circles
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .offset(x = (-40).dp, y = (-50).dp)
                    .background(Color.White.copy(alpha = 0.08f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = 40.dp)
                    .background(GoldYellow.copy(alpha = 0.35f), CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = 50.dp, y = 10.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 28.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo badge inside a gold ring — circular avatar chip style.
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                        .border(
                            width = 2.dp,
                            color = GoldYellow,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    FamilyLogoBadge(
                        badgeSize = 84.dp,
                        logoSize = 60.dp,
                        cornerRadius = 22.dp,
                        surfaceColor = Color.Transparent
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "FamilyHub",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "Stay connected, stay organized",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // ── Form card (cream card with green border) ────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-28).dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Text(
                    text = "Welcome back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBrown
                )
                Text(
                    text = "Sign in to manage your family tasks",
                    fontSize = 14.sp,
                    color = TextMutedBrown,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("you@example.com", color = TextMutedBrown.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(Icons.Filled.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = { Text("••••••••", color = TextMutedBrown.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(Icons.Filled.Lock, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                },
                                tint = TextMutedBrown
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                TextButton(
                    onClick = onForgotPasswordClick,
                    modifier = Modifier.align(Alignment.End),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Forgot password?",
                        color = ForestGreen,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }

                if (errorMessage != null) {
                    Surface(
                        color = cs.errorContainer,
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.5.dp, cs.error),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = cs.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Login button — forest green, large rounded pill.
                Button(
                    onClick = { onLoginClick(email, password) },
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForestGreen,
                        disabledContainerColor = ForestGreen.copy(alpha = 0.4f),
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text(
                            text = "Login",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Divider with "or"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = TextMutedBrown.copy(alpha = 0.3f)
                    )
                    Text(
                        text = "  New here?  ",
                        color = TextMutedBrown,
                        fontSize = 12.sp
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = TextMutedBrown.copy(alpha = 0.3f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Secondary action — gold outlined pill.
                OutlinedButton(
                    onClick = onSignUpClick,
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.5.dp, GoldYellow),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = GoldYellowLight,
                        contentColor = TextBrown
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Text(
                        text = "Create an account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Footer
        Text(
            text = "Family tasks · Shared calendars · Together",
            color = TextMutedBrown,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-12).dp)
                .padding(bottom = 28.dp)
                .navigationBarsPadding()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    FamilyHubTheme {
        LoginScreen(
            isLoading = false,
            errorMessage = null,
            onLoginClick = { _, _ -> },
            onSignUpClick = {},
            onForgotPasswordClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Login with error")
@Composable
private fun LoginScreenErrorPreview() {
    FamilyHubTheme {
        LoginScreen(
            isLoading = false,
            errorMessage = "No internet connection. Check your network and try again.",
            onLoginClick = { _, _ -> },
            onSignUpClick = {},
            onForgotPasswordClick = {}
        )
    }
}