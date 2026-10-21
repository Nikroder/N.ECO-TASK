package com.omniplan.app.ui.screens.focus

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omniplan.app.R
import com.omniplan.app.core.model.SessionType
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

@Composable
fun FocusScreen(
    viewModel: FocusViewModel
) {
    val currentType by viewModel.currentSessionType.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val timeLeft by viewModel.timeLeftSeconds.collectAsState()
    val totalTime by viewModel.totalTimeSeconds.collectAsState()
    val ambientSound by viewModel.selectedAmbientSound.collectAsState()
    val todayFocusMinutes by viewModel.todayFocusMinutes.collectAsState()
    val todaySessions by viewModel.todaySessions.collectAsState()

    val progress by animateFloatAsState(
        targetValue = if (totalTime > 0) timeLeft.toFloat() / totalTime.toFloat() else 0f,
        label = "timer_progress"
    )

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeFormatted = String.format(java.util.Locale.getDefault(), "%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 14.dp)
        ) {
            Text(
                text = stringResource(R.string.focus_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = Slate100
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "N.ECO TASK • Таймер глубокой концентрации (Pomodoro)",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = PurpleLight
            )
        }

        // Session Type Selector
        TabRow(
            selectedTabIndex = currentType.ordinal,
            containerColor = Slate850,
            contentColor = PurpleLight,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[currentType.ordinal]),
                    color = PurplePrimary,
                    height = 3.dp
                )
            },
            divider = {},
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Slate700, RoundedCornerShape(14.dp))
        ) {
            Tab(
                selected = currentType == SessionType.WORK,
                onClick = { viewModel.setSessionType(SessionType.WORK) },
                text = {
                    Text(
                        "Фокус (25м)",
                        fontSize = 12.sp,
                        fontWeight = if (currentType == SessionType.WORK) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentType == SessionType.WORK) Slate100 else Slate400
                    )
                }
            )
            Tab(
                selected = currentType == SessionType.SHORT_BREAK,
                onClick = { viewModel.setSessionType(SessionType.SHORT_BREAK) },
                text = {
                    Text(
                        "Отдых (5м)",
                        fontSize = 12.sp,
                        fontWeight = if (currentType == SessionType.SHORT_BREAK) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentType == SessionType.SHORT_BREAK) Slate100 else Slate400
                    )
                }
            )
            Tab(
                selected = currentType == SessionType.LONG_BREAK,
                onClick = { viewModel.setSessionType(SessionType.LONG_BREAK) },
                text = {
                    Text(
                        "Пауза (15м)",
                        fontSize = 12.sp,
                        fontWeight = if (currentType == SessionType.LONG_BREAK) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentType == SessionType.LONG_BREAK) Slate100 else Slate400
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Circular Timer Display
        Box(
            modifier = Modifier.size(260.dp),
            contentAlignment = Alignment.Center
        ) {
            // Background ring
            CircularProgressIndicator(
                progress = { 1f },
                modifier = Modifier.fillMaxSize(),
                color = Slate800,
                strokeWidth = 14.dp
            )

            // Animated progress ring
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(),
                color = if (currentType == SessionType.WORK) PurplePrimary else EmeraldAccent,
                strokeWidth = 14.dp,
                strokeCap = StrokeCap.Round
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = timeFormatted,
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-1).sp,
                    color = Slate100
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isRunning) PurplePrimary.copy(alpha = 0.2f) else Slate800)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (isRunning) "• Идёт сессия..." else "Пауза",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isRunning) PurpleLight else Slate400
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Timer Controls
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Slate800)
                    .border(1.dp, Slate700, CircleShape)
                    .clickable { viewModel.resetTimer() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Сброс",
                    tint = Slate400,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Neon Gradient Play/Pause Button
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(16.dp, CircleShape, ambientColor = PurplePrimary, spotColor = PurplePrimary)
                    .clip(CircleShape)
                    .background(FabGradient)
                    .clickable {
                        if (isRunning) viewModel.pauseTimer() else viewModel.startTimer()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Пауза" else "Старт",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Ambient Sounds Bar
        Text(
            text = "Звуки окружения",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Slate100
        )

        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AmbientSound.entries.forEach { sound ->
                val isSelected = ambientSound == sound
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) PurplePrimary.copy(alpha = 0.2f) else Slate850)
                        .border(
                            1.dp,
                            if (isSelected) PurplePrimary else Slate700,
                            RoundedCornerShape(10.dp)
                        )
                        .clickable { viewModel.setAmbientSound(sound) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isSelected && sound != AmbientSound.NONE) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = null,
                                tint = PurpleLight,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                        }
                        Text(
                            text = sound.title,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) PurpleLight else Slate400
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Stats Footer Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 72.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Slate850),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Slate700),
                width = 1.dp
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$todayFocusMinutes мин",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = PurpleLight
                    )
                    Text(
                        text = "Фокус сегодня",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${todaySessions.size}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = EmeraldAccent
                    )
                    Text(
                        text = "Сессий завершено",
                        fontSize = 12.sp,
                        color = Slate400
                    )
                }
            }
        }
    }
}
