package com.kirdevelopment.domain.repository

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.model.course.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    suspend fun getCourses(): AppResult<List<Course>>
    suspend fun refreshCourses(): AppResult<Unit>
    fun observeCourses(): Flow<List<Course>>
    fun observeCourseById(courseId: Long): Flow<Course?>
}
