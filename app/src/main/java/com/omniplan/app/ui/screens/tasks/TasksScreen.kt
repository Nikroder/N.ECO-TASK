package com.omniplan.app.ui.screens.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omniplan.app.R
import com.omniplan.app.ui.components.AddTaskBottomSheet
import com.omniplan.app.ui.components.TaskCard
import com.omniplan.app.ui.theme.AccentPillGradient
import com.omniplan.app.ui.theme.FabGradient
import com.omniplan.app.ui.theme.PrimaryButtonGradient
import com.omniplan.app.ui.theme.PurpleLight
import com.omniplan.app.ui.theme.PurplePrimary
import com.omniplan.app.ui.theme.Slate100
import com.omniplan.app.ui.theme.Slate400
import com.omniplan.app.ui.theme.Slate600
import com.omniplan.app.ui.theme.Slate700
import com.omniplan.app.ui.theme.Slate800
import com.omniplan.app.ui.theme.Slate850
import com.omniplan.app.ui.theme.Slate900
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel
) {
    val context = LocalContext.current
    val tasks by viewModel.tasks.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isKanbanView by viewModel.isKanbanView.collectAsState()

    var showAddBottomSheet by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val todayDateFormatted = remember {
        LocalDate.now().format(DateTimeFormatter.ofPattern("d MMMM", Locale("ru")))
    }

    Scaffold(
        containerColor = Slate900,
        floatingActionButton = {
            // Neon Violet/Indigo Gradient FAB
            Box(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = PurplePrimary, spotColor = PurplePrimary)
                    .clip(RoundedCornerShape(18.dp))
                    .background(FabGradient)
                    .clickable { showAddBottomSheet = true }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // Top Header: Title & Subtitle + View Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.nav_tasks),
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-0.5).sp,
                            color = Slate100
                        )
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "N.ECO TASK • Сегодня, $todayDateFormatted",
                        style = androidx.compose.ui.text.TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PurpleLight
                        )
                    )
                }

                // Kanban / List toggle button in sleek dark slate container
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate800)
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                        .clickable { viewModel.toggleKanbanView() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isKanbanView) Icons.Default.ViewList else Icons.Default.ViewKanban,
                        contentDescription = "Переключить вид",
                        tint = PurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Modern Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        "Поиск задач или #тегов...",
                        fontSize = 13.sp,
                        color = Slate600
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = Slate400,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Очистить",
                                tint = Slate400,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Slate100,
                    unfocusedTextColor = Slate100,
                    focusedContainerColor = Slate850,
                    unfocusedContainerColor = Slate850,
                    focusedIndicatorColor = PurplePrimary,
                    unfocusedIndicatorColor = Slate700
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Chips Row (Cyber Slate / Violet Pills)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChipItem(
                    label = stringResource(R.string.filter_all),
                    selected = selectedFilter == TaskFilter.ALL,
                    onClick = { viewModel.setFilter(TaskFilter.ALL) }
                )
                FilterChipItem(
                    label = stringResource(R.string.filter_today),
                    selected = selectedFilter == TaskFilter.TODAY,
                    onClick = { viewModel.setFilter(TaskFilter.TODAY) }
                )
                FilterChipItem(
                    label = stringResource(R.string.filter_upcoming),
                    selected = selectedFilter == TaskFilter.UPCOMING,
                    onClick = { viewModel.setFilter(TaskFilter.UPCOMING) }
                )
                FilterChipItem(
                    label = stringResource(R.string.filter_important),
                    selected = selectedFilter == TaskFilter.IMPORTANT,
                    onClick = { viewModel.setFilter(TaskFilter.IMPORTANT) }
                )
                FilterChipItem(
                    label = stringResource(R.string.filter_completed),
                    selected = selectedFilter == TaskFilter.COMPLETED,
                    onClick = { viewModel.setFilter(TaskFilter.COMPLETED) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Tasks List or Aesthetic Empty State
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        // Glowing Icon Container
                        Box(
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(Slate800)
                                .border(1.5.dp, PurplePrimary.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TaskAlt,
                                contentDescription = null,
                                tint = PurpleLight,
                                modifier = Modifier.size(38.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Все задачи выполнены!",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Отличный момент для отдыха или планирования новых целей на сегодня.",
                            fontSize = 13.sp,
                            color = Slate400,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Quick Add Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(PrimaryButtonGradient)
                                .clickable { showAddBottomSheet = true }
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Добавить первую задачу",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(tasks, key = { it.id }) { task ->
                        TaskCard(
                            task = task,
                            onToggleComplete = { viewModel.toggleTaskCompletion(context, task) },
                            onDelete = { viewModel.deleteTask(context, task) }
                        )
                    }
                }
            }
        }
    }

    // Add Task Bottom Sheet
    if (showAddBottomSheet) {
        AddTaskBottomSheet(
            sheetState = bottomSheetState,
            onDismiss = { showAddBottomSheet = false },
            onSaveTask = { task ->
                viewModel.addTask(context, task)
            }
        )
    }
}

@Composable
fun FilterChipItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (selected) {
                    Modifier
                        .shadow(4.dp, RoundedCornerShape(12.dp), ambientColor = PurplePrimary)
                        .background(AccentPillGradient)
                } else {
                    Modifier
                        .background(Slate850)
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                }
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else Slate400
        )
    }
}
