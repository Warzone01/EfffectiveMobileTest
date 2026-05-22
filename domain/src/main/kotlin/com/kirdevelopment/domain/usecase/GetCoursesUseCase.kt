package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.common.usecase.UseCase
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.repository.CoursesRepository
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) : UseCase<NoParams, List<Course>> {
    override suspend fun invoke(params: NoParams): AppResult<List<Course>> {
        return coursesRepository.getCourses()
    }
}
