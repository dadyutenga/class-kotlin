package com.biglitecode.familyhub.ui.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.data.model.FamilyGroupOption
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

@Composable
fun SignUpScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onSignUpClick: (
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: FamilyRole,
        familyGroupOption: FamilyGroupOption,
        familyGroupCode: String
    ) -> Unit,
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var role by remember { mutableStateOf(FamilyRole.PARENT) }
    var familyGroupOption by remember { mutableStateOf(FamilyGroupOption.CREATE) }
    var familyGroupName by remember { mutableStateOf("") }
    var familyGroupCode by remember { mutableStateOf("") }

    val cs = MaterialTheme.colorScheme
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

    val passwordsMatch = password.isNotBlank() && password == confirmPassword
    val groupFieldValid = when (familyGroupOption) {
        FamilyGroupOption.CREATE -> familyGroupName.isNotBlank()
        FamilyGroupOption.JOIN -> familyGroupCode.isNotBlank()
    }
    val formValid = name.isNotBlank() &&
        email.isNotBlank() &&
        password.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        passwordsMatch &&
        groupFieldValid

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Create account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextBrown
        )
        Text(
            text = "Join your family and start tracking chores",
            fontSize = 14.sp,
            color = TextMutedBrown,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = CardCream),
            border = BorderStroke(1.5.dp, BorderGreen),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    placeholder = {
                        Text("Jordan Smith", color = TextMutedBrown.copy(alpha = 0.6f))
                    },
                    leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    placeholder = {
                        Text("you@example.com", color = TextMutedBrown.copy(alpha = 0.6f))
                    },
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    placeholder = {
                        Text("••••••••", color = TextMutedBrown.copy(alpha = 0.6f))
                    },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
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

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    placeholder = {
                        Text("••••••••", color = TextMutedBrown.copy(alpha = 0.6f))
                    },
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) {
                                    Icons.Filled.VisibilityOff
                                } else {
                                    Icons.Filled.Visibility
                                },
                                contentDescription = if (confirmPasswordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                },
                                tint = TextMutedBrown
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    singleLine = true,
                    shape = fieldShape,
                    colors = fieldColors,
                    isError = confirmPassword.isNotBlank() && !passwordsMatch,
                    supportingText = if (confirmPassword.isNotBlank() && !passwordsMatch) {
                        {
                            Text("Passwords do not match", color = cs.error, fontSize = 12.sp)
                        }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "I am a…",
                    fontWeight = FontWeight.SemiBold,
                    color = TextBrown,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RoleSelectCard(
                        title = "Parent / Guardian",
                        selected = role == FamilyRole.PARENT,
                        onClick = { role = FamilyRole.PARENT },
                        modifier = Modifier.weight(1f)
                    )
                    RoleSelectCard(
                        title = "Child / Member",
                        selected = role == FamilyRole.CHILD,
                        onClick = { role = FamilyRole.CHILD },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Family group",
                    fontWeight = FontWeight.SemiBold,
                    color = TextBrown,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                FamilyGroupOptionRow(
                    title = "Create a new family group",
                    icon = Icons.Filled.GroupAdd,
                    selected = familyGroupOption == FamilyGroupOption.CREATE,
                    onClick = { familyGroupOption = FamilyGroupOption.CREATE }
                )
                Spacer(modifier = Modifier.height(8.dp))
                FamilyGroupOptionRow(
                    title = "Join existing family group",
                    icon = Icons.Filled.Group,
                    selected = familyGroupOption == FamilyGroupOption.JOIN,
                    onClick = { familyGroupOption = FamilyGroupOption.JOIN }
                )

                Spacer(modifier = Modifier.height(12.dp))

                when (familyGroupOption) {
                    FamilyGroupOption.CREATE -> {
                        OutlinedTextField(
                            value = familyGroupName,
                            onValueChange = { familyGroupName = it },
                            label = { Text("Family group name") },
                            placeholder = {
                                Text(
                                    "The Smiths",
                                    color = TextMutedBrown.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.GroupAdd, contentDescription = null)
                            },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    FamilyGroupOption.JOIN -> {
                        OutlinedTextField(
                            value = familyGroupCode,
                            onValueChange = { familyGroupCode = it },
                            label = { Text("Invite code") },
                            placeholder = {
                                Text(
                                    "ABC-1234",
                                    color = TextMutedBrown.copy(alpha = 0.6f)
                                )
                            },
                            leadingIcon = {
                                Icon(Icons.Filled.Group, contentDescription = null)
                            },
                            singleLine = true,
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                }

                Button(
                    onClick = {
                        val groupValue = when (familyGroupOption) {
                            FamilyGroupOption.CREATE -> familyGroupName
                            FamilyGroupOption.JOIN -> familyGroupCode
                        }
                        onSignUpClick(
                            name,
                            email,
                            password,
                            confirmPassword,
                            role,
                            familyGroupOption,
                            groupValue
                        )
                    },
                    enabled = !isLoading && formValid,
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
                            text = "Sign Up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = TextMutedBrown.copy(alpha = 0.3f)
            )
            Text(
                text = "  Already registered?  ",
                color = TextMutedBrown,
                fontSize = 12.sp
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = TextMutedBrown.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? ", color = TextMutedBrown, fontSize = 14.sp)
            Text(
                text = "Login",
                color = ForestGreen,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Family chores · Rewards · Together",
            color = TextMutedBrown,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun RoleSelectCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) GoldYellowLight else CardCream
        ),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (selected) GoldYellow else TextMutedBrown.copy(alpha = 0.35f)
        ),
        modifier = modifier
    ) {
        Text(
            text = title,
            color = TextBrown,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 14.dp)
        )
    }
}

@Composable
private fun FamilyGroupOptionRow(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (selected) GoldYellowLight else CardCream,
        border = BorderStroke(
            width = 1.5.dp,
            color = if (selected) GoldYellow else TextMutedBrown.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = ForestGreen,
                    unselectedColor = TextMutedBrown
                )
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) ForestGreen else TextMutedBrown,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp)
            )
            Text(
                text = title,
                color = TextBrown,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Sign Up")
@Composable
private fun SignUpScreenPreview() {
    FamilyHubTheme {
        SignUpScreen(
            isLoading = false,
            errorMessage = null,
            onSignUpClick = { _, _, _, _, _, _, _ -> },
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Sign Up error")
@Composable
private fun SignUpScreenErrorPreview() {
    FamilyHubTheme {
        SignUpScreen(
            isLoading = false,
            errorMessage = "Could not create account. Please try again.",
            onSignUpClick = { _, _, _, _, _, _, _ -> },
            onLoginClick = {}
        )
    }
}
