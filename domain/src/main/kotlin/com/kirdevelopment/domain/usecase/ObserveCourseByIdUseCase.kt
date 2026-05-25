package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.usecase.FlowUseCase
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.repository.CoursesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCourseByIdUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) : FlowUseCase<Long, Course?> {
    override fun invoke(params: Long): Flow<Course?> {
        return coursesRepository.observeCourseById(params)
    }
}
