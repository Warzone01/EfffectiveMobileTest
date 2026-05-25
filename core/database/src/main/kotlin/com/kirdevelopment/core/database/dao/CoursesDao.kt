package com.kirdevelopment.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kirdevelopment.core.database.DatabaseConstants
import com.kirdevelopment.core.database.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao {

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_COURSES}")
    fun observeCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_COURSES} WHERE ${DatabaseConstants.COLUMN_COURSE_ID} = :courseId")
    fun observeCourseById(courseId: Long): Flow<CourseEntity?>

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_COURSES} WHERE ${DatabaseConstants.COLUMN_HAS_LIKE} = 1")
    fun observeFavoriteCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCourses(courses: List<CourseEntity>)

    @Query("UPDATE ${DatabaseConstants.TABLE_COURSES} SET ${DatabaseConstants.COLUMN_HAS_LIKE} = :isFavorite, ${DatabaseConstants.COLUMN_UPDATED_AT} = :updatedAt WHERE ${DatabaseConstants.COLUMN_COURSE_ID} = :courseId")
    suspend fun updateFavoriteState(courseId: Long, isFavorite: Boolean, updatedAt: Long)

    @Query("SELECT ${DatabaseConstants.COLUMN_COURSE_ID} FROM ${DatabaseConstants.TABLE_COURSES} WHERE ${DatabaseConstants.COLUMN_HAS_LIKE} = 1")
    suspend fun getFavoriteIds(): List<Long>

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_COURSES}")
    suspend fun getAllCourses(): List<CourseEntity>

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_COURSES} WHERE ${DatabaseConstants.COLUMN_COURSE_ID} = :courseId")
    suspend fun getCourseById(courseId: Long): CourseEntity?
}
