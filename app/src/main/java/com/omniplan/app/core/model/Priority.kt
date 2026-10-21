package com.omniplan.app.core.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.omniplan.app.R

enum class Priority(
    val level: Int,
    @StringRes val titleRes: Int,
    val color: Color
) {
    NONE(0, R.string.priority_none, Color(0xFF9E9E9E)),
    LOW(1, R.string.priority_low, Color(0xFF388E3C)),
    MEDIUM(2, R.string.priority_medium, Color(0xFF1976D2)),
    HIGH(3, R.string.priority_high, Color(0xFFF57C00)),
    URGENT(4, R.string.priority_urgent, Color(0xFFD32F2F));

    companion object {
        fun fromLevel(level: Int): Priority =
            entries.find { it.level == level } ?: NONE
    }
}
