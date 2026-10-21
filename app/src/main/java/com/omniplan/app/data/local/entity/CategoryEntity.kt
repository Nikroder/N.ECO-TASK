package com.omniplan.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omniplan.app.core.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val colorHex: String,
    val iconName: String = "folder",
    val isDefault: Boolean = false
) {
    fun toDomain(): Category = Category(
        id = id,
        name = name,
        colorHex = colorHex,
        iconName = iconName,
        isDefault = isDefault
    )

    companion object {
        fun fromDomain(category: Category): CategoryEntity = CategoryEntity(
            id = category.id,
            name = category.name,
            colorHex = category.colorHex,
            iconName = category.iconName,
            isDefault = category.isDefault
        )
    }
}
