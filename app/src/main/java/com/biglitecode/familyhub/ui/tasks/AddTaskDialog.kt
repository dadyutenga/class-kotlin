package com.biglitecode.familyhub.ui.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.GoldYellowLight
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    members: List<FamilyMember>,
    onDismiss: () -> Unit,
    onConfirm: (
        title: String,
        description: String,
        assignee: FamilyMember,
        dueDate: Long,
        rewardPoints: Int
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var pointsText by remember { mutableStateOf("10") }
    var expanded by remember { mutableStateOf(false) }
    var selectedMember by remember { mutableStateOf(members.firstOrNull()) }
    // Simple due-date options instead of a full date picker for reliability.
    var dueOption by remember { mutableStateOf(DueOption.TOMORROW) }
    var dueExpanded by remember { mutableStateOf(false) }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = ForestGreen,
        unfocusedBorderColor = TextMutedBrown.copy(alpha = 0.45f),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = GoldYellowLight.copy(alpha = 0.35f),
        cursorColor = ForestGreen,
        focusedLabelColor = ForestGreen,
        unfocusedLabelColor = TextMutedBrown
    )
    val shape = MaterialTheme.shapes.small

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Add Task",
                fontWeight = FontWeight.Bold,
                color = TextBrown
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    shape = shape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    minLines = 2,
                    shape = shape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedMember?.name ?: "Select assignee",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Assignee") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        shape = shape,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name) },
                                onClick = {
                                    selectedMember = member
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                ExposedDropdownMenuBox(
                    expanded = dueExpanded,
                    onExpandedChange = { dueExpanded = it }
                ) {
                    OutlinedTextField(
                        value = dueOption.label,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Due date") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(dueExpanded) },
                        shape = shape,
                        colors = fieldColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(
                        expanded = dueExpanded,
                        onDismissRequest = { dueExpanded = false }
                    ) {
                        DueOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = {
                                    dueOption = option
                                    dueExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = pointsText,
                    onValueChange = { pointsText = it.filter { ch -> ch.isDigit() }.take(4) },
                    label = { Text("Reward points") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = shape,
                    colors = fieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val member = selectedMember ?: return@Button
                    if (title.isBlank()) return@Button
                    onConfirm(
                        title,
                        description,
                        member,
                        dueOption.toEpochMillis(),
                        pointsText.toIntOrNull() ?: 10
                    )
                },
                enabled = title.isNotBlank() && selectedMember != null,
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Add", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMutedBrown)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

private enum class DueOption(val label: String, private val daysFromNow: Long) {
    TODAY("Today", 0),
    TOMORROW("Tomorrow", 1),
    IN_3_DAYS("In 3 days", 3),
    NEXT_WEEK("Next week", 7);

    fun toEpochMillis(): Long =
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(daysFromNow)
}
