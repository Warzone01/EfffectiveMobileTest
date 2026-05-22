package com.kirdevelopment.core.database

object DatabaseConstants {
    // Названия собраны в одном месте для безопасного рефакторинга и миграций.
    const val DATABASE_NAME = "effective_mobile.db"
    const val DATABASE_VERSION = 1

    const val TABLE_FAVORITE_COURSES = "favorite_courses"
    const val COLUMN_COURSE_ID = "course_id"
    const val COLUMN_UPDATED_AT = "updated_at"
}
