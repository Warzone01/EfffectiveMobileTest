package com.kirdevelopment.core.database.converter

import androidx.room.TypeConverter
import java.time.Instant

/**
 * Конвертеры вынесены отдельно, чтобы при расширении БД не смешивать
 * преобразования типов с логикой сущностей и DAO.
 */
class DatabaseConverters {

    @TypeConverter
    fun fromInstant(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)
}
