package com.biglitecode.familyhub.ui.usage

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.biglitecode.familyhub.data.model.AppUsage
import com.biglitecode.familyhub.data.model.FamilyMember
import com.biglitecode.familyhub.data.model.FamilyRole
import com.biglitecode.familyhub.ui.theme.BackgroundLight
import com.biglitecode.familyhub.ui.theme.FamilyBlue
import com.biglitecode.familyhub.ui.theme.FamilyHubTheme
import com.biglitecode.familyhub.ui.theme.TextDark
import com.biglitecode.familyhub.ui.theme.TextGray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppUsageScreen(
    viewModel: AppUsageViewModel = viewModel(factory = AppUsageViewModel.Factory(LocalContext.current)),
    onNavigateBack: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val dateOptions = remember { viewModel.getDateOptions() }

    AppUsageScreenContent(
        uiState = uiState,
        dateOptions = dateOptions,
        onNavigateBack = onNavigateBack,
        onRefresh = { viewModel.refresh() },
        onDateSelected = { viewModel.selectDate(it) },
        onChildSelected = { viewModel.selectChild(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppUsageScreenContent(
    uiState: AppUsageUiState,
    dateOptions: List<String>,
    onNavigateBack: () -> Unit,
    onRefresh: () -> Unit,
    onDateSelected: (String) -> Unit,
    onChildSelected: (String) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("App Usage") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundLight)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                DateChipRow(
                    dates = dateOptions,
                    selectedDate = uiState.selectedDate,
                    onDateSelected = onDateSelected
                )

                if (uiState.isParent) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ChildSelector(
                        children = uiState.children,
                        selectedChildId = uiState.selectedChildId,
                        onChildSelected = onChildSelected
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.isLoading && uiState.usage.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = FamilyBlue)
                    }
                } else if (uiState.errorMessage != null && uiState.usage.isEmpty()) {
                    EmptyState(
                        message = uiState.errorMessage ?: "Something went wrong."
                    )
                } else if (uiState.usage.isEmpty()) {
                    EmptyState(
                        message = "No usage data yet for this day."
                    )
                } else {
                    UsageList(
                        usage = uiState.usage,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateChipRow(
    dates: List<String>,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        dates.forEach { date ->
            FilterChip(
                selected = date == selectedDate,
                onClick = { onDateSelected(date) },
                label = { Text(formatDateLabel(date)) },
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChildSelector(
    children: List<FamilyMember>,
    selectedChildId: String?,
    onChildSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedChild = children.find { it.id == selectedChildId }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedChild?.name ?: "Select child",
            onValueChange = {},
            readOnly = true,
            label = { Text("Child") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            children.forEach { child ->
                DropdownMenuItem(
                    text = { Text(child.name) },
                    onClick = {
                        onChildSelected(child.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun UsageList(
    usage: List<AppUsage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(usage, key = { it.id }) { item ->
            UsageRow(item = item)
        }
    }
}

@Composable
private fun UsageRow(item: AppUsage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            AppIcon(
                packageName = item.package_name,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.app_name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextDark
                )
                Text(
                    text = item.package_name,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 1
                )
            }
            Text(
                text = formatDuration(item.total_time_ms),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = FamilyBlue
            )
        }
    }
}

@Composable
private fun AppIcon(
    packageName: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var drawable by remember(packageName) { mutableStateOf<Drawable?>(null) }

    LaunchedEffect(packageName) {
        drawable = withContext(Dispatchers.IO) {
            runCatching { context.packageManager.getApplicationIcon(packageName) }.getOrNull()
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        if (drawable != null) {
            Image(
                painter = BitmapPainter(drawable!!.toBitmap().asImageBitmap()),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = null,
                tint = TextGray,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = null,
                tint = FamilyBlue,
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = TextGray
            )
        }
    }
}

@Composable
fun UsageStatsConsentDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Allow app usage access?") },
        text = {
            Text(
                "FamilyHub needs usage access so your parent can see how much time " +
                    "you spend on apps each day. This data is only shared with your family group."
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        }
    )
}

private fun formatDuration(ms: Long): String {
    val totalMinutes = ms / 1000 / 60
    val hours = totalMinutes / 60
    val minutes = totalMinutes % 60
    return when {
        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
        hours > 0 -> "${hours}h"
        else -> "${minutes}m"
    }
}

private fun formatDateLabel(date: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val parsed = inputFormat.parse(date) ?: return date
    val today = Calendar.getInstance()
    val parsedCal = Calendar.getInstance().apply { time = parsed }

    return when {
        isSameDay(today, parsedCal) -> "Today"
        isSameDay(today.apply { add(Calendar.DAY_OF_YEAR, -1) }, parsedCal) -> "Yesterday"
        else -> SimpleDateFormat("EEE", Locale.getDefault()).format(parsed)
    }
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean {
    return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) &&
        a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppUsageScreenPreview() {
    FamilyHubTheme {
        AppUsageScreenContent(
            uiState = AppUsageUiState(
                isLoading = false,
                isParent = true,
                children = listOf(
                    FamilyMember(
                        id = "m1",
                        name = "Wanjiku",
                        role = FamilyRole.CHILD,
                        email = "wanjiku@test.com",
                        family_group_id = "fg1"
                    )
                ),
                selectedChildId = "m1",
                usage = listOf(
                    AppUsage(
                        id = "m1_com.example.app_2026-07-19",
                        family_member_id = "m1",
                        family_group_id = "fg1",
                        package_name = "com.example.app",
                        app_name = "Example App",
                        usage_date = "2026-07-19",
                        total_time_ms = 5_040_000,
                        last_time_used = System.currentTimeMillis()
                    )
                )
            ),
            dateOptions = lastSevenDaysStatic(),
            onNavigateBack = {},
            onRefresh = {},
            onDateSelected = {},
            onChildSelected = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Empty state")
@Composable
private fun AppUsageScreenEmptyPreview() {
    FamilyHubTheme {
        AppUsageScreenContent(
            uiState = AppUsageUiState(
                isLoading = false,
                isParent = false,
                currentMemberId = "m1",
                selectedChildId = "m1"
            ),
            dateOptions = lastSevenDaysStatic(),
            onNavigateBack = {},
            onRefresh = {},
            onDateSelected = {},
            onChildSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun UsageStatsConsentDialogPreview() {
    FamilyHubTheme {
        UsageStatsConsentDialog(onConfirm = {}, onDismiss = {})
    }
}

private fun lastSevenDaysStatic(): List<String> {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
        timeZone = TimeZone.getDefault()
    }
    val calendar = Calendar.getInstance()
    return buildList {
        for (i in 0 until 7) {
            add(formatter.format(calendar.timeInMillis))
            calendar.add(Calendar.DAY_OF_YEAR, -1)
        }
    }
}
