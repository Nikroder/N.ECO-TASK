package com.omniplan.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omniplan.app.OmniPlanApp
import com.omniplan.app.ui.navigation.Screen
import com.omniplan.app.ui.screens.analytics.AnalyticsScreen
import com.omniplan.app.ui.screens.analytics.AnalyticsViewModel
import com.omniplan.app.ui.screens.calendar.CalendarScreen
import com.omniplan.app.ui.screens.calendar.CalendarViewModel
import com.omniplan.app.ui.screens.focus.FocusScreen
import com.omniplan.app.ui.screens.focus.FocusViewModel
import com.omniplan.app.ui.screens.habits.HabitsScreen
import com.omniplan.app.ui.screens.habits.HabitsViewModel
import com.omniplan.app.ui.screens.tasks.TasksScreen
import com.omniplan.app.ui.screens.tasks.TasksViewModel
import com.omniplan.app.ui.theme.OmniPlanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OmniPlanTheme {
                OmniPlanMainApp()
            }
        }
    }
}

@Composable
fun OmniPlanMainApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Request notification permission on Android 13+
    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val app = OmniPlanApp.instance
    val tasksViewModel = remember { TasksViewModel(app.taskRepository) }
    val calendarViewModel = remember { CalendarViewModel(app.calendarRepository, app.taskRepository) }
    val habitsViewModel = remember { HabitsViewModel(app.habitRepository) }
    val focusViewModel = remember { FocusViewModel(app.calendarRepository) }
    val analyticsViewModel = remember {
        AnalyticsViewModel(app.taskRepository, app.habitRepository, app.calendarRepository)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                Screen.bottomNavScreens.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = stringResource(screen.titleRes)
                            )
                        },
                        label = {
                            Text(text = stringResource(screen.titleRes))
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tasks.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Tasks.route) {
                TasksScreen(viewModel = tasksViewModel)
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(viewModel = calendarViewModel)
            }
            composable(Screen.Habits.route) {
                HabitsScreen(viewModel = habitsViewModel)
            }
            composable(Screen.Focus.route) {
                FocusScreen(viewModel = focusViewModel)
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(viewModel = analyticsViewModel)
            }
        }
    }
}
