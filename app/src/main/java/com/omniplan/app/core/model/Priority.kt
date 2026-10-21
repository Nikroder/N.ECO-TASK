package com.omniplan.app.core.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.omniplan.app.R

enum class Priority(
    val level: Int,
    @StringRes val titleRes: Int,
    val color: Color
) {
    NONE(0, R.string.priority_none, Color(0xFF64748B)),
    LOW(1, R.string.priority_low, Color(0xFF10B981)),
    MEDIUM(2, R.string.priority_medium, Color(0xFF3B82F6)),
    HIGH(3, R.string.priority_high, Color(0xFFF97316)),
    URGENT(4, R.string.priority_urgent, Color(0xFFEF4444));

    companion object {
        fun fromLevel(level: Int): Priority =
            entries.find { it.level == level } ?: NONE
    }
}
