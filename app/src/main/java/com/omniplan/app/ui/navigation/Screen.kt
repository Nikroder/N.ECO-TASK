package com.omniplan.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.ui.graphics.vector.ImageVector
import com.omniplan.app.R

sealed class Screen(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    data object Tasks : Screen("tasks", R.string.nav_tasks, Icons.Default.CheckCircle)
    data object Calendar : Screen("calendar", R.string.nav_calendar, Icons.Default.CalendarMonth)
    data object Habits : Screen("habits", R.string.nav_habits, Icons.Default.LocalFireDepartment)
    data object Focus : Screen("focus", R.string.nav_focus, Icons.Default.Timer)
    data object Analytics : Screen("analytics", R.string.nav_analytics, Icons.Default.BarChart)

    companion object {
        val bottomNavScreens = listOf(Tasks, Calendar, Habits, Focus, Analytics)
    }
}
