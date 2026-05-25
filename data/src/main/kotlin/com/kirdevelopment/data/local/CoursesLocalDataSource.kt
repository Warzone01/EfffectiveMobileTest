package com.kirdevelopment.data.local

import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import kotlinx.coroutines.flow.Flow

interface CoursesLocalDataSource {
    fun observeCourses(): Flow<List<Course>>
    fun observeCourseById(courseId: Long): Flow<Course?>
    fun observeFavoriteCourses(): Flow<List<Course>>
    fun observeFavorites(): Flow<List<FavoriteCourse>>
    suspend fun getFavoriteIds(): Set<Long>
    suspend fun upsertCourses(courses: List<Course>)
    suspend fun toggleFavorite(courseId: Long): Boolean
    suspend fun replaceFavoriteStates(favoriteIds: Set<Long>)
}
