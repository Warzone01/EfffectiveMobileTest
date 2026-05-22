package com.kirdevelopment.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kirdevelopment.core.database.converter.DatabaseConverters
import com.kirdevelopment.core.database.dao.FavoriteCoursesDao
import com.kirdevelopment.core.database.entity.FavoriteCourseEntity

/**
 * Центральная точка подключения сущностей и DAO.
 * Новые таблицы добавляются в entities и через отдельные DAO,
 * что упрощает масштабирование и контроль миграций.
 */
@Database(
    entities = [
        FavoriteCourseEntity::class
    ],
    version = DatabaseConstants.DATABASE_VERSION,
    exportSchema = true
)
@TypeConverters(DatabaseConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCoursesDao(): FavoriteCoursesDao
}
