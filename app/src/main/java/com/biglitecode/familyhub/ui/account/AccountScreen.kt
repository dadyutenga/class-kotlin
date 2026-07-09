package com.biglitecode.familyhub.ui.account

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.ui.components.MemberAvatar
import com.biglitecode.familyhub.ui.theme.BorderGreen
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellow
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
    onBack: (() -> Unit)? = null,
    onOpenDrawer: (() -> Unit)? = null
) {
    val user by viewModel.currentUser.collectAsStateWithLifecycle()
    val members by viewModel.members.collectAsStateWithLifecycle()
    val familyGroup by viewModel.familyGroup.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var editing by remember { mutableStateOf(false) }
    var editName by remember(user?.name) { mutableStateOf(user?.name.orEmpty()) }
    var editEmail by remember(user?.email) { mutableStateOf(user?.email.orEmpty()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                when {
                    onOpenDrawer != null -> {
                        IconButton(onClick = onOpenDrawer) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = TextBrown)
                        }
                    }
                    onBack != null -> {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextBrown
                            )
                        }
                    }
                    else -> Spacer(modifier = Modifier.width(48.dp))
                }
                Text(
                    text = "Account",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = TextBrown
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            MemberAvatar(
                name = user?.name ?: "?",
                avatarColorHex = user?.avatarColor,
                isActive = true,
                size = 96.dp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = user?.name ?: "—",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = TextBrown
            )
            Text(
                text = user?.email ?: "—",
                fontSize = 14.sp,
                color = TextMutedBrown,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            val isParent = user?.role == FamilyRole.PARENT
            Surface(
                shape = MaterialTheme.shapes.small,
                color = if (isParent) ForestGreen else GoldYellow
            ) {
                Text(
                    text = if (isParent) "Parent/Guardian" else "Child/Member",
                    color = if (isParent) CardCream else TextBrown,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
        }

        item {
            Card(
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = CardCream),
                border = BorderStroke(1.5.dp, BorderGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Family group",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = TextMutedBrown
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = familyGroup?.name ?: "My Family",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextBrown
                    )
                    Text(
                        text = "${members.size} members",
                        fontSize = 13.sp,
                        color = TextMutedBrown,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Family Code",
                        fontSize = 12.sp,
                        color = TextMutedBrown
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = familyGroup?.inviteCode ?: "—",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = ForestGreen,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = {
                                val code = familyGroup?.inviteCode.orEmpty()
                                if (code.isNotBlank()) {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE)
                                        as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Family Code", code))
                                    Toast.makeText(context, "Code copied", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Filled.ContentCopy,
                                contentDescription = "Copy family code",
                                tint = ForestGreen
                            )
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Family members",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextBrown,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(members, key = { it.id }) { member ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        MemberAvatar(
                            name = member.name,
                            avatarColorHex = member.avatarColor,
                            isActive = member.isActive
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = member.name,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (member.isActive) TextBrown else TextMutedBrown
                        )
                        Text(
                            text = if (member.role == FamilyRole.PARENT) "Parent" else "Child",
                            fontSize = 11.sp,
                            color = TextMutedBrown
                        )
                    }
                }
            }
        }

        item {
            if (editing) {
                val fieldColors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ForestGreen,
                    unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
                    cursorColor = ForestGreen,
                    focusedLabelColor = ForestGreen,
                    unfocusedLabelColor = TextMutedBrown
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Name") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = editEmail,
                        onValueChange = { editEmail = it },
                        label = { Text("Email") },
                        singleLine = true,
                        shape = MaterialTheme.shapes.small,
                        colors = fieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = {
                            // TODO: persist profile via Supabase
                            editing = false
                        },
                        border = BorderStroke(1.5.dp, ForestGreen),
                        shape = MaterialTheme.shapes.large,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save (placeholder)", color = ForestGreen, fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                OutlinedButton(
                    onClick = {
                        editName = user?.name.orEmpty()
                        editEmail = user?.email.orEmpty()
                        editing = true
                    },
                    border = BorderStroke(1.5.dp, ForestGreen),
                    shape = MaterialTheme.shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text("Edit Profile", color = ForestGreen, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Account")
@Composable
private fun AccountScreenPreview() {
    FamilyHubTheme {
        AccountScreen()
    }
}
