package com.omniplan.app.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omniplan.app.ui.components.TimeBlockCard
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
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val timeBlocks by viewModel.timeBlocks.collectAsState()
    val dayTasks by viewModel.dayTasks.collectAsState()

    var showAddTimeBlockDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Slate900,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = PurplePrimary, spotColor = PurplePrimary)
                    .clip(RoundedCornerShape(18.dp))
                    .background(FabGradient)
                    .clickable { showAddTimeBlockDialog = true }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Забронировать время",
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
        ) {
            // Header: Month & Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Календарь",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        color = Slate100
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(PurplePrimary.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${selectedDate.month.getDisplayName(TextStyle.FULL_STANDALONE, Locale("ru")).replaceFirstChar { it.uppercase() }} ${selectedDate.year}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PurpleLight
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Slate800)
                        .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                ) {
                    IconButton(
                        onClick = { viewModel.setSelectedDate(selectedDate.minusDays(1)) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Назад",
                            tint = PurpleLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(
                        onClick = { viewModel.setSelectedDate(selectedDate.plusDays(1)) },
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Вперед",
                            tint = PurpleLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Horizontal Days Strip
            DaysStrip(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.setSelectedDate(it) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = Slate900,
                contentColor = PurpleLight,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                        color = PurplePrimary,
                        height = 3.dp
                    )
                },
                divider = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Slate800)
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == CalendarTab.DAY_TIMELINE,
                    onClick = { viewModel.setSelectedTab(CalendarTab.DAY_TIMELINE) },
                    text = {
                        Text(
                            "Тайм-блокинг (${timeBlocks.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == CalendarTab.DAY_TIMELINE) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == CalendarTab.DAY_TIMELINE) Slate100 else Slate400
                        )
                    }
                )
                Tab(
                    selected = selectedTab == CalendarTab.MONTH_VIEW,
                    onClick = { viewModel.setSelectedTab(CalendarTab.MONTH_VIEW) },
                    text = {
                        Text(
                            "Задачи на день (${dayTasks.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == CalendarTab.MONTH_VIEW) FontWeight.Bold else FontWeight.Medium,
                            color = if (selectedTab == CalendarTab.MONTH_VIEW) Slate100 else Slate400
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                CalendarTab.DAY_TIMELINE -> {
                    // Timeline of blocks
                    if (timeBlocks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Slate800)
                                        .border(1.5.dp, PurplePrimary.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = null,
                                        tint = PurpleLight,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "Нет забронированных слотов",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Нажмите '+', чтобы спланировать время фокуса или глубокой работы.",
                                    fontSize = 13.sp,
                                    color = Slate400,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(timeBlocks, key = { it.id }) { block ->
                                TimeBlockCard(
                                    block = block,
                                    onDelete = { viewModel.deleteTimeBlock(block) }
                                )
                            }
                        }
                    }
                }

                CalendarTab.MONTH_VIEW -> {
                    // Day tasks
                    if (dayTasks.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Slate800)
                                        .border(1.5.dp, PurplePrimary.copy(alpha = 0.5f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = PurpleLight,
                                        modifier = Modifier.size(34.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Text(
                                    text = "На этот день задач нет",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Slate100
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Все спокойно! Можно отдохнуть или добавить новую задачу.",
                                    fontSize = 13.sp,
                                    color = Slate400,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(dayTasks, key = { it.id }) { task ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Slate850)
                                        .border(1.dp, Slate700, RoundedCornerShape(12.dp))
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(task.priority.color)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = task.title,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Slate100
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add Time Block Dialog
    if (showAddTimeBlockDialog) {
        var blockTitle by remember { mutableStateOf("") }
        var startHour by remember { mutableIntStateOf(10) }
        var endHour by remember { mutableIntStateOf(11) }

        AlertDialog(
            onDismissRequest = { showAddTimeBlockDialog = false },
            containerColor = Slate850,
            title = {
                Text(
                    "Забронировать время",
                    color = Slate100,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = blockTitle,
                        onValueChange = { blockTitle = it },
                        placeholder = { Text("Название (например: Глубокая работа)", color = Slate600) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Slate100,
                            unfocusedTextColor = Slate100,
                            focusedContainerColor = Slate800,
                            unfocusedContainerColor = Slate800,
                            focusedIndicatorColor = PurplePrimary,
                            unfocusedIndicatorColor = Slate700
                        )
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Интервал: $startHour:00 - $endHour:30",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurpleLight
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (blockTitle.isNotBlank()) {
                            viewModel.addTimeBlock(blockTitle, startHour, 0, endHour, 30)
                            showAddTimeBlockDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Создать", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTimeBlockDialog = false }) {
                    Text("Отмена", color = Slate400)
                }
            }
        )
    }
}

@Composable
private fun DaysStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val startOfWeek = selectedDate.minusDays(3)
    val days = (0..14).map { startOfWeek.plusDays(it.toLong()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { date ->
            val isSelected = date == selectedDate
            val isToday = date == LocalDate.now()

            Column(
                modifier = Modifier
                    .width(52.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .then(
                        if (isSelected) {
                            Modifier
                                .shadow(6.dp, RoundedCornerShape(14.dp), ambientColor = PurplePrimary)
                                .background(AccentPillGradient)
                        } else if (isToday) {
                            Modifier
                                .background(Slate800)
                                .border(1.5.dp, PurplePrimary, RoundedCornerShape(14.dp))
                        } else {
                            Modifier
                                .background(Slate850)
                                .border(1.dp, Slate700, RoundedCornerShape(14.dp))
                        }
                    )
                    .clickable { onDateSelected(date) }
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("ru")),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White else Slate400
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = date.dayOfMonth.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else Slate100
                )
            }
        }
    }
}
