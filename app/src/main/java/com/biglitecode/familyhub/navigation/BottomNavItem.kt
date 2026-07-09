package com.biglitecode.familyhub.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Bottom-navigation destinations for the post-login shell.
 */
sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        route = "dashboard",
        label = "Home",
        icon = Icons.Filled.Home
    )

    data object Tasks : BottomNavItem(
        route = "tasks",
        label = "Tasks",
        icon = Icons.Filled.CheckCircle
    )

    data object Report : BottomNavItem(
        route = "report",
        label = "Progress",
        icon = Icons.Filled.Leaderboard
    )

    data object Settings : BottomNavItem(
        route = "settings",
        label = "Settings",
        icon = Icons.Filled.Settings
    )

    companion object {
        val items: List<BottomNavItem> = listOf(Home, Tasks, Report, Settings)
    }
}

/** Nested routes outside the bottom bar (still inside the same NavHost). */
object AppRoutes {
    const val TASK_DETAIL = "task_detail/{taskId}"
    fun taskDetail(taskId: String) = "task_detail/$taskId"

    // Drawer destinations (no bottom bar)
    const val ACCOUNT = "account"
    const val FEEDBACK = "feedback"
    const val COMPLAINS = "complains"
    const val HELP = "help"
    const val CONTACT = "contact"
    const val PRIVACY = "privacy"
}

/**
 * Items shown in the [androidx.compose.material3.ModalNavigationDrawer] sheet.
 * [route] null means a special action (e.g. logout) handled by the host.
 */
sealed class DrawerNavItem(
    val label: String,
    val route: String?
) {
    data object Account : DrawerNavItem("Account", AppRoutes.ACCOUNT)
    data object Settings : DrawerNavItem("Settings", BottomNavItem.Settings.route)
    data object Help : DrawerNavItem("Help", AppRoutes.HELP)
    data object Contact : DrawerNavItem("Contact", AppRoutes.CONTACT)
    data object Complains : DrawerNavItem("Complains", AppRoutes.COMPLAINS)
    data object Feedback : DrawerNavItem("Task Feedback", AppRoutes.FEEDBACK)
    data object Privacy : DrawerNavItem("Privacy Policy", AppRoutes.PRIVACY)
    data object Logout : DrawerNavItem("Logout", route = null)

    companion object {
        val items: List<DrawerNavItem> = listOf(
            Account,
            Settings,
            Help,
            Contact,
            Complains,
            Feedback,
            Privacy,
            Logout
        )
    }
}
