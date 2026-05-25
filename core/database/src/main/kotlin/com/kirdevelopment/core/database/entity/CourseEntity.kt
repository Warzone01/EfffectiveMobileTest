package com.kirdevelopment.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kirdevelopment.core.database.DatabaseConstants

@Entity(tableName = DatabaseConstants.TABLE_COURSES)
data class CourseEntity(
    @PrimaryKey
    @ColumnInfo(name = DatabaseConstants.COLUMN_COURSE_ID)
    val id: Long,
    @ColumnInfo(name = DatabaseConstants.COLUMN_TITLE)
    val title: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_TEXT)
    val text: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_PRICE)
    val price: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_RATE)
    val rate: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_START_DATE)
    val startDate: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_HAS_LIKE)
    val hasLike: Boolean,
    @ColumnInfo(name = DatabaseConstants.COLUMN_PUBLISH_DATE)
    val publishDate: String,
    @ColumnInfo(name = DatabaseConstants.COLUMN_UPDATED_AT)
    val updatedAt: Long
)
