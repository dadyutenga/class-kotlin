package com.biglitecode.familyhub.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.ui.components.FamilyLogoBadge
import com.biglitecode.familyhub.ui.theme.BackgroundLight
import com.biglitecode.familyhub.ui.theme.FamilyAccent
import com.biglitecode.familyhub.ui.theme.FamilyBlue
import com.biglitecode.familyhub.ui.theme.FamilyBlueDark
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.TextDark
import com.biglitecode.familyhub.ui.theme.TextGray

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

    val fieldShape = RoundedCornerShape(16.dp)
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = FamilyBlue,
        unfocusedBorderColor = Color(0xFFE2E5EF),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color(0xFFF4F6FB),
        cursorColor = FamilyBlue,
        focusedLeadingIconColor = FamilyBlue,
        unfocusedLeadingIconColor = TextGray,
        focusedLabelColor = FamilyBlue,
        unfocusedLabelColor = TextGray
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Brand header ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FamilyBlueDark, FamilyBlue, Color(0xFF5B7CFF))
                    )
                )
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
                    .background(FamilyAccent.copy(alpha = 0.18f), CircleShape)
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
                FamilyLogoBadge(
                    badgeSize = 84.dp,
                    logoSize = 60.dp,
                    cornerRadius = 22.dp,
                    surfaceColor = Color.White.copy(alpha = 0.18f)
                )
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

        // ── Form card ─────────────────────────────────────────────────
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = (-28).dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    color = TextDark
                )
                Text(
                    text = "Sign in to manage your family tasks",
                    fontSize = 14.sp,
                    color = TextGray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = { Text("you@example.com", color = TextGray.copy(alpha = 0.6f)) },
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
                    placeholder = { Text("••••••••", color = TextGray.copy(alpha = 0.6f)) },
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
                                tint = TextGray
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
                        color = FamilyBlue,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    )
                }

                if (errorMessage != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Gradient login button
                Button(
                    onClick = { onLoginClick(email, password) },
                    enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    contentPadding = PaddingValues(),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = if (!isLoading && email.isNotBlank() && password.isNotBlank()) {
                                    Brush.horizontalGradient(
                                        listOf(FamilyBlueDark, FamilyBlue, Color(0xFF5B7CFF))
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        listOf(
                                            FamilyBlue.copy(alpha = 0.45f),
                                            FamilyBlue.copy(alpha = 0.45f)
                                        )
                                    )
                                },
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
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
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Divider with "or"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE2E5EF)
                    )
                    Text(
                        text = "  New here?  ",
                        color = TextGray,
                        fontSize = 12.sp
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE2E5EF)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                OutlinedButton(
                    onClick = onSignUpClick,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.5.dp, FamilyBlue.copy(alpha = 0.45f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = FamilyBlue
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
            color = TextGray.copy(alpha = 0.75f),
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
