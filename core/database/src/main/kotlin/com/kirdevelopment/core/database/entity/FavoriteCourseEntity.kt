package com.kirdevelopment.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kirdevelopment.core.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_FAVORITE_COURSES)
data class FavoriteCourseEntity(
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstants.COLUMN_COURSE_ID)
    val courseId: Long,
    @ColumnInfo(name = DatabaseConstants.COLUMN_UPDATED_AT)
    val updatedAt: Long
)
