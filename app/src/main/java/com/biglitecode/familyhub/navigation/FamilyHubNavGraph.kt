package com.biglitecode.familyhub.navigation

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.biglitecode.familyhub.LoginActivity
import com.biglitecode.familyhub.ui.account.AccountScreen
import com.biglitecode.familyhub.ui.complains.ComplainsScreen
import com.biglitecode.familyhub.ui.contact.ContactScreen
import com.biglitecode.familyhub.ui.dashboard.DashboardScreen
import com.biglitecode.familyhub.ui.feedback.FeedbackScreen
import com.biglitecode.familyhub.ui.help.HelpScreen
import com.biglitecode.familyhub.ui.privacy.PrivacyPolicyScreen
import com.biglitecode.familyhub.ui.report.ReportScreen
import com.biglitecode.familyhub.ui.settings.SettingsScreen
import com.biglitecode.familyhub.ui.tasks.TaskDetailScreen
import com.biglitecode.familyhub.ui.tasks.TasksScreen
import com.biglitecode.familyhub.ui.tasks.TasksViewModel
import com.biglitecode.familyhub.ui.theme.CardCream
import com.biglitecode.familyhub.ui.theme.CoralRed
import com.biglitecode.familyhub.ui.theme.ForestGreen
import com.biglitecode.familyhub.ui.theme.TextBrown
import com.biglitecode.familyhub.ui.theme.TextMutedBrown
import kotlinx.coroutines.launch

/**
 * Post-login shell: [ModalNavigationDrawer] wrapping bottom navigation + nested routes.
 * Hosted by [com.biglitecode.familyhub.DashboardActivity].
 */
@Composable
fun FamilyHubNavGraph(
    tasksViewModel: TasksViewModel = viewModel()
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    val showBottomBar = BottomNavItem.items.any { item ->
        currentDestination?.hierarchy?.any { it.route == item.route } == true
    }

    fun openDrawer() {
        scope.launch { drawerState.open() }
    }

    fun closeDrawer() {
        scope.launch { drawerState.close() }
    }

    fun navigateFromDrawer(route: String) {
        closeDrawer()
        navController.navigate(route) {
            launchSingleTop = true
            // Keep bottom-tab graph; drawer screens stack on top.
            restoreState = true
        }
    }

    fun logout() {
        closeDrawer()
        val intent = Intent(context, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CardCream,
                modifier = Modifier
                    .width(300.dp)
                    .fillMaxHeight()
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp)) {
                    Text(
                        text = "FamilyHub",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = ForestGreen,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Text(
                        text = "Menu",
                        fontSize = 13.sp,
                        color = TextMutedBrown,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    )
                    HorizontalDivider(color = TextMutedBrown.copy(alpha = 0.25f))
                    Spacer(modifier = Modifier.height(8.dp))

                    DrawerNavItem.items.forEach { item ->
                        val selected = item.route != null && currentRoute == item.route
                        val isLogout = item is DrawerNavItem.Logout
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = item.label,
                                    color = if (isLogout) CoralRed else TextBrown,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            selected = selected,
                            onClick = {
                                when (item) {
                                    DrawerNavItem.Logout -> logout()
                                    else -> item.route?.let { navigateFromDrawer(it) }
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = drawerIcon(item),
                                    contentDescription = item.label,
                                    tint = if (isLogout) CoralRed else if (selected) ForestGreen else TextMutedBrown
                                )
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = ForestGreen.copy(alpha = 0.12f),
                                unselectedContainerColor = CardCream
                            ),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = CardCream,
                        contentColor = ForestGreen
                    ) {
                        BottomNavItem.items.forEach { item ->
                            val selected = currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true
                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.label
                                    )
                                },
                                label = {
                                    Text(
                                        text = item.label,
                                        fontSize = 11.sp,
                                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = ForestGreen,
                                    selectedTextColor = ForestGreen,
                                    unselectedIconColor = TextMutedBrown,
                                    unselectedTextColor = TextMutedBrown,
                                    indicatorColor = ForestGreen.copy(alpha = 0.12f)
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Home.route) {
                    DashboardScreen(
                        viewModel = tasksViewModel,
                        onTaskClick = { taskId ->
                            navController.navigate(AppRoutes.taskDetail(taskId))
                        }
                    )
                }
                composable(BottomNavItem.Tasks.route) {
                    TasksScreen(
                        viewModel = tasksViewModel,
                        onTaskClick = { taskId ->
                            navController.navigate(AppRoutes.taskDetail(taskId))
                        }
                    )
                }
                composable(BottomNavItem.Report.route) {
                    ReportScreen(viewModel = tasksViewModel)
                }
                composable(BottomNavItem.Settings.route) {
                    SettingsScreen(
                        onOpenDrawer = { openDrawer() },
                        onLogout = { logout() }
                    )
                }
                composable(
                    route = AppRoutes.TASK_DETAIL,
                    arguments = listOf(navArgument("taskId") { type = NavType.StringType })
                ) { entry ->
                    val taskId = entry.arguments?.getString("taskId").orEmpty()
                    TaskDetailScreen(
                        taskId = taskId,
                        viewModel = tasksViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
                composable(AppRoutes.ACCOUNT) {
                    AccountScreen(
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { openDrawer() }
                    )
                }
                composable(AppRoutes.FEEDBACK) {
                    FeedbackScreen(
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { openDrawer() }
                    )
                }
                composable(AppRoutes.COMPLAINS) {
                    ComplainsScreen(
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { openDrawer() }
                    )
                }
                composable(AppRoutes.HELP) {
                    HelpScreen(
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { openDrawer() }
                    )
                }
                composable(AppRoutes.CONTACT) {
                    ContactScreen(
                        onBack = { navController.popBackStack() },
                        onOpenDrawer = { openDrawer() }
                    )
                }
                composable(AppRoutes.PRIVACY) {
                    PrivacyPolicyScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

private fun drawerIcon(item: DrawerNavItem): ImageVector = when (item) {
    DrawerNavItem.Account -> Icons.Filled.AccountCircle
    DrawerNavItem.Settings -> Icons.Filled.Settings
    DrawerNavItem.Help -> Icons.AutoMirrored.Filled.Help
    DrawerNavItem.Contact -> Icons.Filled.ContactPhone
    DrawerNavItem.Complains -> Icons.Filled.ReportProblem
    DrawerNavItem.Feedback -> Icons.Filled.Feedback
    DrawerNavItem.Privacy -> Icons.Filled.Policy
    DrawerNavItem.Logout -> Icons.AutoMirrored.Filled.Logout
}
