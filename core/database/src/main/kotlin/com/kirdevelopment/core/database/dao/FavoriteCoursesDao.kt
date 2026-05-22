package com.kirdevelopment.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kirdevelopment.core.database.DatabaseConstants
import com.kirdevelopment.core.database.entity.FavoriteCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteCoursesDao {

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_FAVORITE_COURSES}")
    fun observeFavorites(): Flow<List<FavoriteCourseEntity>>

    @Query("SELECT ${DatabaseConstants.COLUMN_COURSE_ID} FROM ${DatabaseConstants.TABLE_FAVORITE_COURSES}")
    fun observeFavoriteIds(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertFavorite(favorite: FavoriteCourseEntity)

    @Query("DELETE FROM ${DatabaseConstants.TABLE_FAVORITE_COURSES} WHERE ${DatabaseConstants.COLUMN_COURSE_ID} = :courseId")
    suspend fun deleteFavorite(courseId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM ${DatabaseConstants.TABLE_FAVORITE_COURSES} WHERE ${DatabaseConstants.COLUMN_COURSE_ID} = :courseId)")
    suspend fun isFavorite(courseId: Long): Boolean
}
