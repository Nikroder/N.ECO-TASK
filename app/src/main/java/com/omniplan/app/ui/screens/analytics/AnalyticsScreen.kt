package com.omniplan.app.ui.screens.analytics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.omniplan.app.BuildConfig
import com.omniplan.app.R
import com.omniplan.app.core.update.AppUpdateManager
import com.omniplan.app.ui.theme.AccentPillGradient
import com.omniplan.app.ui.theme.AmberAccent
import com.omniplan.app.ui.theme.CyanAccent
import com.omniplan.app.ui.theme.EmeraldAccent
import com.omniplan.app.ui.theme.FabGradient
import com.omniplan.app.ui.theme.PrimaryButtonGradient
import com.omniplan.app.ui.theme.PurpleLight
import com.omniplan.app.ui.theme.PurplePrimary
import com.omniplan.app.ui.theme.RoseAccent
import com.omniplan.app.ui.theme.Slate100
import com.omniplan.app.ui.theme.Slate400
import com.omniplan.app.ui.theme.Slate600
import com.omniplan.app.ui.theme.Slate700
import com.omniplan.app.ui.theme.Slate800
import com.omniplan.app.ui.theme.Slate850
import com.omniplan.app.ui.theme.Slate900
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel
) {
    val stats by viewModel.stats.collectAsState()
    var showRewindStories by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(bottom = 80.dp)
    ) {
        // Top Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.nav_analytics),
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = Slate100
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "N.ECO TASK • Метрики эффективности и баланс жизни",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = PurpleLight
            )
        }

        // 1. N.ECO REWIND STORIES BANNER
        RewindPromoBanner(
            titleBadge = stats.rewindStats.titleBadge,
            onClick = { showRewindStories = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Productivity Score Card (Gradient Banner)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Slate850),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(Slate700),
                width = 1.dp
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryButtonGradient)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "Индекс продуктивности дня",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${stats.completionRate}%",
                            fontSize = 46.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "задач завершено",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { stats.completionRate / 100f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = CyanAccent,
                        trackColor = Color.White.copy(alpha = 0.25f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 2x2 Grid Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Выполнено",
                value = stats.completedTasks.toString(),
                icon = Icons.Default.CheckCircle,
                tint = EmeraldAccent,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "В процессе",
                value = stats.activeTasks.toString(),
                icon = Icons.Default.PendingActions,
                tint = CyanAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                title = "Фокус сегодня",
                value = "${stats.totalFocusMinutes}м",
                icon = Icons.Default.Timer,
                tint = AmberAccent,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                title = "Привычек",
                value = stats.totalHabits.toString(),
                icon = Icons.Default.LocalFireDepartment,
                tint = RoseAccent,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. КОЛЕСО ЖИЗНЕННОГО БАЛАНСА (LIFE BALANCE WHEEL / RADAR CHART)
        LifeBalanceWheelCard(items = stats.lifeBalance)

        Spacer(modifier = Modifier.height(20.dp))

        // 5. ХРОНОТИП ПРОДУКТИВНОСТИ (ПИК ЭФФЕКТИВНОСТИ)
        ChronotypeCard(chronotype = stats.chronotype)

        Spacer(modifier = Modifier.height(20.dp))

        // 6. Недельная динамика активности
        WeeklyDynamicsCard()

        Spacer(modifier = Modifier.height(20.dp))

        // 7. App Version & Updates Card
        AppUpdateCard()
    }

    // FULLSCREEN REWIND STORIES DIALOG
    if (showRewindStories) {
        RewindStoriesDialog(
            stats = stats.rewindStats,
            onDismiss = { showRewindStories = false }
        )
    }
}

// -------------------------------------------------------------
// REWIND PROMO BANNER
// -------------------------------------------------------------
@Composable
private fun RewindPromoBanner(
    titleBadge: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .shadow(12.dp, RoundedCornerShape(18.dp), ambientColor = PurplePrimary)
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF3B0764),
                        Color(0xFF1E1B4B),
                        Color(0xFF0F172A)
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(listOf(PurplePrimary, CyanAccent)),
                RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(FabGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "N.ECO REWIND",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = CyanAccent,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RoseAccent.copy(alpha = 0.25f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text("ИТОГИ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = RoseAccent)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = titleBadge,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
                Text(
                    text = "Нажмите, чтобы открыть интерактивные сторис недели",
                    fontSize = 11.sp,
                    color = Slate400
                )
            }

            Icon(
                imageVector = Icons.Default.NavigateNext,
                contentDescription = null,
                tint = PurpleLight,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// -------------------------------------------------------------
// LIFE BALANCE WHEEL (RADAR CHART)
// -------------------------------------------------------------
@Composable
private fun LifeBalanceWheelCard(
    items: List<LifeBalanceItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PurplePrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = PurpleLight,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Колесо жизненного баланса",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = "Гармония 6 ключевых сфер жизни",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Radar Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                LifeBalanceRadarCanvas(
                    items = items,
                    modifier = Modifier.size(220.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Breakdown list by spheres
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items.forEach { item ->
                    val sphereColor = Color(item.sphere.hexColor)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Slate800)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(item.sphere.emoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = item.sphere.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate100,
                            modifier = Modifier.weight(1f)
                        )
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(Slate700)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(item.scorePercent / 100f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(sphereColor)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "${item.scorePercent}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = sphereColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LifeBalanceRadarCanvas(
    items: List<LifeBalanceItem>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val count = items.size
        if (count < 3) return@Canvas

        val center = Offset(size.width / 2f, size.height / 2f)
        val maxRadius = (size.minDimension / 2f) * 0.78f

        // 1. Draw Concentric Hexagon Grid Levels (25%, 50%, 75%, 100%)
        val levels = listOf(0.25f, 0.5f, 0.75f, 1.0f)
        levels.forEach { level ->
            val gridRadius = maxRadius * level
            val gridPath = Path()
            for (i in 0 until count) {
                val angle = (i * (2 * Math.PI / count) - Math.PI / 2).toFloat()
                val x = center.x + gridRadius * cos(angle)
                val y = center.y + gridRadius * sin(angle)
                if (i == 0) gridPath.moveTo(x, y) else gridPath.lineTo(x, y)
            }
            gridPath.close()
            drawPath(
                path = gridPath,
                color = if (level == 1.0f) Color(0xFF334155) else Color(0xFF1E293B),
                style = Stroke(width = if (level == 1.0f) 1.5f else 1f)
            )
        }

        // 2. Draw Spokes / Axes from center
        for (i in 0 until count) {
            val angle = (i * (2 * Math.PI / count) - Math.PI / 2).toFloat()
            val endX = center.x + maxRadius * cos(angle)
            val endY = center.y + maxRadius * sin(angle)
            drawLine(
                color = Color(0xFF334155),
                start = center,
                end = Offset(endX, endY),
                strokeWidth = 1f
            )
        }

        // 3. Draw Data Polygon
        val dataPath = Path()
        val points = mutableListOf<Offset>()

        items.forEachIndexed { i, item ->
            val ratio = (item.scorePercent / 100f).coerceIn(0.15f, 1.0f)
            val r = maxRadius * ratio
            val angle = (i * (2 * Math.PI / count) - Math.PI / 2).toFloat()
            val x = center.x + r * cos(angle)
            val y = center.y + r * sin(angle)
            val pt = Offset(x, y)
            points.add(pt)
            if (i == 0) dataPath.moveTo(x, y) else dataPath.lineTo(x, y)
        }
        dataPath.close()

        // Fill with translucent Violet/Indigo gradient
        drawPath(
            path = dataPath,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF8B5CF6).copy(alpha = 0.5f),
                    Color(0xFF6366F1).copy(alpha = 0.25f)
                ),
                center = center,
                radius = maxRadius
            )
        )

        // Draw Polygon Outline Stroke
        drawPath(
            path = dataPath,
            color = Color(0xFFA78BFA),
            style = Stroke(width = 3f)
        )

        // 4. Draw Vertex Dots
        points.forEachIndexed { i, pt ->
            val dotColor = Color(items[i].sphere.hexColor)
            drawCircle(
                color = dotColor,
                radius = 5.5f,
                center = pt
            )
            drawCircle(
                color = Color.White,
                radius = 2.5f,
                center = pt
            )
        }
    }
}

// -------------------------------------------------------------
// CHRONOTYPE & PEAK EFFICIENCY CARD
// -------------------------------------------------------------
@Composable
private fun ChronotypeCard(
    chronotype: ChronotypeDistribution
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(AmberAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = AmberAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Хронотип и пик фокуса",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate100
                    )
                    Text(
                        text = "Анализ продуктивных часов суток",
                        fontSize = 11.sp,
                        color = Slate400
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Peak Summary Pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate800)
                    .border(1.dp, AmberAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(chronotype.peakEmoji, fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Твой хронотип: ${chronotype.peakLabel}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate100
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Пик активности приходится на эти часы. Планируй сложные задачи сюда!",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4-timesOfDay Bar distribution
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChronotypeColumn(
                    label = "Утро",
                    time = "06-12",
                    percent = chronotype.morningPercent,
                    color = AmberAccent,
                    modifier = Modifier.weight(1f)
                )
                ChronotypeColumn(
                    label = "День",
                    time = "12-18",
                    percent = chronotype.dayPercent,
                    color = EmeraldAccent,
                    modifier = Modifier.weight(1f)
                )
                ChronotypeColumn(
                    label = "Вечер",
                    time = "18-23",
                    percent = chronotype.eveningPercent,
                    color = PurplePrimary,
                    modifier = Modifier.weight(1f)
                )
                ChronotypeColumn(
                    label = "Ночь",
                    time = "23-06",
                    percent = chronotype.nightPercent,
                    color = CyanAccent,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ChronotypeColumn(
    label: String,
    time: String,
    percent: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Slate800)
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Text(
            text = "$percent%",
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = color
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Slate100
        )
        Text(
            text = time,
            fontSize = 9.sp,
            color = Slate400
        )
    }
}

// -------------------------------------------------------------
// FULLSCREEN REWIND STORIES MODAL
// -------------------------------------------------------------
@Composable
private fun RewindStoriesDialog(
    stats: RewindStats,
    onDismiss: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(0) }
    val totalSlides = 5

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF030712),
                            Color(0xFF1E1B4B),
                            Color(0xFF0F172A)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Stories Progress Bars (5 Segments)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until totalSlides) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(3.5.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    when {
                                        i < currentSlide -> Color.White
                                        i == currentSlide -> PurpleLight
                                        else -> Color.White.copy(alpha = 0.2f)
                                    }
                                )
                        )
                    }
                }

                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "N.ECO REWIND",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp,
                            color = CyanAccent
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• Слайд ${currentSlide + 1} из $totalSlides",
                            fontSize = 11.sp,
                            color = Slate400
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Slide Content Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when (currentSlide) {
                        0 -> RewindSlideTitle(stats)
                        1 -> RewindSlideFocus(stats)
                        2 -> RewindSlideDominantSphere(stats)
                        3 -> RewindSlideHabit(stats)
                        4 -> RewindSlideFinal(stats, onDismiss)
                    }
                }

                // Bottom Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { if (currentSlide > 0) currentSlide-- },
                        enabled = currentSlide > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Slate800,
                            disabledContainerColor = Slate800.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.NavigateBefore, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Назад", color = Color.White)
                    }

                    Button(
                        onClick = {
                            if (currentSlide < totalSlides - 1) currentSlide++ else onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (currentSlide < totalSlides - 1) "Далее" else "Готово ✓",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        if (currentSlide < totalSlides - 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.NavigateNext, contentDescription = null, tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RewindSlideTitle(stats: RewindStats) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .shadow(24.dp, CircleShape, ambientColor = PurplePrimary)
                .background(FabGradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ТВОЙ ТИТУЛ НЕДЕЛИ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            color = AmberAccent
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stats.titleBadge,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stats.titleDescription,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            color = Slate400,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Slate800)
                .border(1.dp, PurplePrimary.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = "⚡ Процент завершения: ${stats.completionRate}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PurpleLight
            )
        }
    }
}

@Composable
private fun RewindSlideFocus(stats: RewindStats) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .shadow(24.dp, CircleShape, ambientColor = CyanAccent)
                .background(
                    Brush.linearGradient(listOf(CyanAccent, Color(0xFF0284C7)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Timer,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ЗОНА ГИПЕРФОКУСА",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            color = CyanAccent
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${stats.totalFocusMinutes} минут",
            fontSize = 38.sp,
            fontWeight = FontWeight.Black,
            color = Slate100
        )

        Text(
            text = "чистой продуктивности Pomodoro",
            fontSize = 14.sp,
            color = Slate400
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Без соцсетей и отвлечений. Это время, в которое создаются великие проекты и закрепляются навыки!",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = Slate400,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun RewindSlideDominantSphere(stats: RewindStats) {
    val sphere = stats.dominantSphere
    val sphereColor = Color(sphere.hexColor)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(sphereColor.copy(alpha = 0.2f))
                .border(2.dp, sphereColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(sphere.emoji, fontSize = 48.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ТВОЙ ФОКУС ЖИЗНИ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            color = sphereColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = sphere.title,
            fontSize = 30.sp,
            fontWeight = FontWeight.Black,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "В этой сфере жизни вы проявили наибольшую активность. Колесо жизненного баланса зафиксировало здесь максимальный прогресс!",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = Slate400,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun RewindSlideHabit(stats: RewindStats) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .shadow(24.dp, CircleShape, ambientColor = AmberAccent)
                .background(
                    Brush.linearGradient(listOf(AmberAccent, Color(0xFFEA580C)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalFireDepartment,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ОГОНЬ ДИСЦИПЛИНЫ",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            color = AmberAccent
        )

        Spacer(modifier = Modifier.height(8.dp))

        val habitName = stats.bestHabitTitle ?: "Ежедневная дисциплина"
        Text(
            text = habitName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Серия: ${stats.bestHabitStreak} дней подряд 🔥",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = AmberAccent
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Регулярность сильнее мотивации. Продолжай серию каждый день!",
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            color = Slate400
        )
    }
}

@Composable
private fun RewindSlideFinal(stats: RewindStats, onDismiss: () -> Unit) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Slate850)
            .border(1.5.dp, PurplePrimary, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Text(
            text = "N.ECO TASK REWIND",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            color = PurpleLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stats.titleBadge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center,
            color = Slate100
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mini stat pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate800)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${stats.completedTasksCount}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = EmeraldAccent)
                    Text("Задач", fontSize = 10.sp, color = Slate400)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate800)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${stats.totalFocusMinutes}м", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CyanAccent)
                    Text("Фокуса", fontSize = 10.sp, color = Slate400)
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate800)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${stats.bestHabitStreak}д", fontSize = 18.sp, fontWeight = FontWeight.Black, color = AmberAccent)
                    Text("Стрик", fontSize = 10.sp, color = Slate400)
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                android.widget.Toast.makeText(context, "Итоги недели сохранены в память!", android.widget.Toast.LENGTH_SHORT).show()
                onDismiss()
            },
            colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Share, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Поделиться итогами", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun WeeklyDynamicsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Недельная динамика",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Slate100
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val days = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                val heights = listOf(55.dp, 80.dp, 40.dp, 95.dp, 70.dp, 30.dp, 60.dp)

                days.forEachIndexed { index, day ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(26.dp)
                                .height(heights[index])
                                .clip(RoundedCornerShape(6.dp))
                                .then(
                                    if (index == 3) {
                                        Modifier.background(AccentPillGradient)
                                    } else {
                                        Modifier.background(PurplePrimary.copy(alpha = 0.35f))
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = day,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (index == 3) PurpleLight else Slate400
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AppUpdateCard() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = null,
                    tint = PurpleLight,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "О приложении N.ECO TASK",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Slate100
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Текущая версия: v${BuildConfig.VERSION_NAME} (Сборка с автообновлением)",
                fontSize = 12.sp,
                color = Slate400
            )
            Spacer(modifier = Modifier.height(14.dp))
            Button(
                onClick = {
                    isChecking = true
                    coroutineScope.launch {
                        val update = AppUpdateManager.checkForUpdates(BuildConfig.VERSION_NAME)
                        isChecking = false
                        if (update.hasUpdate) {
                            android.widget.Toast.makeText(
                                context,
                                "Найдено обновление v${update.latestVersion}! Перезапустите приложение для установки.",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        } else {
                            android.widget.Toast.makeText(
                                context,
                                "У вас установлена самая свежая версия N.ECO TASK!",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                enabled = !isChecking,
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isChecking) "Проверка обновлений..." else "Проверить обновления на GitHub",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate850),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(Slate700),
            width = 1.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Slate100
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Slate400
            )
        }
    }
}
