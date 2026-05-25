package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.common.usecase.UseCase
import com.kirdevelopment.domain.repository.CoursesRepository
import javax.inject.Inject

class RefreshCoursesUseCase @Inject constructor(
    private val coursesRepository: CoursesRepository
) : UseCase<NoParams, Unit> {
    override suspend fun invoke(params: NoParams): AppResult<Unit> {
        return coursesRepository.refreshCourses()
    }
}
