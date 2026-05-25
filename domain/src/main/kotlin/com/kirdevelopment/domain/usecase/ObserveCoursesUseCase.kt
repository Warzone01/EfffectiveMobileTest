package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.usecase.FlowUseCase
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.repository.CoursesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCoursesUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) : FlowUseCase<NoParams, List<Course>> {
    override fun invoke(params: NoParams): Flow<List<Course>> {
        return coursesRepository.observeCourses()
    }
}
