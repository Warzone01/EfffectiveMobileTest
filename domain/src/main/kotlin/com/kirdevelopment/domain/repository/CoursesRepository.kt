package com.kirdevelopment.domain.repository

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.model.course.Course

interface CoursesRepository {
    suspend fun getCourses(): AppResult<List<Course>>
}
