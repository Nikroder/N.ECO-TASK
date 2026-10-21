package com.omniplan.app.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omniplan.app.BuildConfig
import com.omniplan.app.OmniPlanApp
import com.omniplan.app.core.update.AppUpdateManager
import com.omniplan.app.core.update.UpdateInfo
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
import com.omniplan.app.ui.theme.PurpleDark
import com.omniplan.app.ui.theme.PurpleLight
import com.omniplan.app.ui.theme.Slate400
import com.omniplan.app.ui.theme.Slate800
import com.omniplan.app.ui.theme.Slate900
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.parseColor("#0F172A")
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.parseColor("#0F172A")
            )
        )
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.parseColor("#0F172A")
        window.navigationBarColor = android.graphics.Color.parseColor("#0F172A")
        WindowCompat.getInsetsController(window, window.decorView)?.let { controller ->
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }

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
    val coroutineScope = rememberCoroutineScope()
    val navController = rememberNavController()

    // Auto-update state
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    var isDownloadingUpdate by remember { mutableStateOf(false) }
    var updateProgress by remember { mutableIntStateOf(0) }

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

        // Check for app updates from GitHub
        val update = AppUpdateManager.checkForUpdates(BuildConfig.VERSION_NAME)
        if (update.hasUpdate) {
            updateInfo = update
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
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900),
        containerColor = Slate900,
        bottomBar = {
            NavigationBar(
                containerColor = Slate900,
                tonalElevation = 0.dp,
                modifier = Modifier.drawBehind {
                    drawLine(
                        color = Slate800,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            ) {
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
                            Text(
                                text = stringResource(screen.titleRes),
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PurpleLight,
                            selectedTextColor = PurpleLight,
                            indicatorColor = PurpleDark.copy(alpha = 0.25f),
                            unselectedIconColor = Slate400,
                            unselectedTextColor = Slate400
                        )
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

    // Auto-update dialog
    updateInfo?.let { update ->
        AlertDialog(
            onDismissRequest = {
                if (!isDownloadingUpdate) updateInfo = null
            },
            title = {
                Text(text = "Доступно обновление N.ECO TASK v${update.latestVersion}")
            },
            text = {
                Column {
                    if (update.releaseNotes.isNotBlank()) {
                        Text(
                            text = update.releaseNotes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (isDownloadingUpdate) {
                        Text(
                            text = "Загрузка обновления: $updateProgress%",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { updateProgress / 100f },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val downloadUrl = update.downloadUrl
                        if (downloadUrl != null && !isDownloadingUpdate) {
                            isDownloadingUpdate = true
                            coroutineScope.launch {
                                AppUpdateManager.downloadAndInstallApk(
                                    context = context,
                                    downloadUrl = downloadUrl,
                                    onProgress = { progress ->
                                        updateProgress = progress
                                    },
                                    onError = { error ->
                                        isDownloadingUpdate = false
                                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                                    }
                                )
                            }
                        }
                    },
                    enabled = !isDownloadingUpdate
                ) {
                    Text(text = if (isDownloadingUpdate) "Скачивание..." else "Скачать и обновить")
                }
            },
            dismissButton = {
                if (!isDownloadingUpdate) {
                    TextButton(onClick = { updateInfo = null }) {
                        Text(text = "Позже")
                    }
                }
            }
        )
    }
}
