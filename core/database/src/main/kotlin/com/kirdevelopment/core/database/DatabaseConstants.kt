package com.kirdevelopment.core.database

object DatabaseConstants {
    // Названия собраны в одном месте для безопасного рефакторинга и миграций.
    const val DATABASE_NAME = "effective_mobile.db"
    const val DATABASE_VERSION = 2

    const val TABLE_FAVORITE_COURSES = "favorite_courses"
    const val TABLE_COURSES = "courses"

    const val COLUMN_COURSE_ID = "course_id"
    const val COLUMN_UPDATED_AT = "updated_at"
    const val COLUMN_TITLE = "title"
    const val COLUMN_TEXT = "text"
    const val COLUMN_PRICE = "price"
    const val COLUMN_RATE = "rate"
    const val COLUMN_START_DATE = "start_date"
    const val COLUMN_HAS_LIKE = "has_like"
    const val COLUMN_PUBLISH_DATE = "publish_date"
}
