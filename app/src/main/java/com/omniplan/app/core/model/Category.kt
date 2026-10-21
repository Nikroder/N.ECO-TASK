package com.omniplan.app.core.model

data class Category(
    val id: Long = 0,
    val name: String,
    val colorHex: String,
    val iconName: String = "folder",
    val isDefault: Boolean = false
) {
    companion object {
        val DEFAULT_CATEGORIES = listOf(
            Category(id = 1, name = "Личное", colorHex = "#4CAF50", iconName = "person", isDefault = true),
            Category(id = 2, name = "Работа", colorHex = "#2196F3", iconName = "work", isDefault = true),
            Category(id = 3, name = "Учёба", colorHex = "#9C27B0", iconName = "school", isDefault = true),
            Category(id = 4, name = "Здоровье", colorHex = "#F44336", iconName = "fitness", isDefault = true),
            Category(id = 5, name = "Финансы", colorHex = "#FF9800", iconName = "attach_money", isDefault = true)
        )
    }
}
