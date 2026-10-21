package com.omniplan.app.ui.screens.habits

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omniplan.app.R
import com.omniplan.app.ui.components.HabitCard
import com.omniplan.app.ui.theme.AmberAccent
import com.omniplan.app.ui.theme.EmeraldAccent
import com.omniplan.app.ui.theme.FabGradient
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

@Composable
fun HabitsScreen(
    viewModel: HabitsViewModel
) {
    val habits by viewModel.habits.collectAsState()
    var showAddHabitDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Slate900,
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .shadow(16.dp, RoundedCornerShape(18.dp), ambientColor = PurplePrimary, spotColor = PurplePrimary)
                    .clip(RoundedCornerShape(18.dp))
                    .background(FabGradient)
                    .clickable { showAddHabitDialog = true }
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_habit),
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.habits_title),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp,
                        color = Slate100
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "N.ECO TASK • Трекер дисциплины и привычек",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurpleLight
                    )
                }
            }

            // Consistency Heatmap Card
            HabitHeatmapCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Habits List
            if (habits.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Slate800)
                                .border(1.5.dp, AmberAccent.copy(alpha = 0.5f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = AmberAccent,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Создайте свою первую привычку!",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Маленькие регулярные шаги каждый день ведут к большим результатам.",
                            fontSize = 13.sp,
                            color = Slate400,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(bottom = 88.dp)
                ) {
                    items(habits, key = { it.id }) { habit ->
                        HabitCard(
                            habit = habit,
                            onToggleCheckIn = { viewModel.toggleHabitCheckIn(habit.id) }
                        )
                    }
                }
            }
        }
    }

    // Add Habit Dialog
    if (showAddHabitDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var targetDays by remember { mutableFloatStateOf(7f) }

        AlertDialog(
            onDismissRequest = { showAddHabitDialog = false },
            containerColor = Slate850,
            title = {
                Text(
                    stringResource(R.string.add_habit),
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Название (например: Чтение 20 мин)", color = Slate600) },
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
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("Мотивация или заметка...", color = Slate600) },
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
                        text = "Цель: ${targetDays.toInt()} дней в неделю",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PurpleLight
                    )
                    Slider(
                        value = targetDays,
                        onValueChange = { targetDays = it },
                        valueRange = 1f..7f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = PurplePrimary,
                            activeTrackColor = PurplePrimary,
                            inactiveTrackColor = Slate700
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addHabit(title, description, targetDays.toInt())
                            showAddHabitDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary)
                ) {
                    Text("Создать", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddHabitDialog = false }) {
                    Text("Отмена", color = Slate400)
                }
            }
        )
    }
}

@Composable
private fun HabitHeatmapCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Карта дисциплины (Heatmap)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100
            )
            Text(
                text = "Активность за последние недели",
                fontSize = 12.sp,
                color = Slate400
            )

            Spacer(modifier = Modifier.height(12.dp))

            // GitHub-style grid: 16 weeks columns x 7 days rows
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (week in 0 until 16) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        for (day in 0 until 7) {
                            val isFilled = (week * 7 + day) % 3 != 0
                            val boxColor = if (isFilled) {
                                EmeraldAccent.copy(alpha = if ((week + day) % 2 == 0) 0.85f else 0.45f)
                            } else {
                                Slate800
                            }

                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(boxColor)
                            )
                        }
                    }
                }
            }
        }
    }
}
