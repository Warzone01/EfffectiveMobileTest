package com.kirdevelopment.data.remote

import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.network.call.SafeApiCall
import com.kirdevelopment.core.network.model.NetworkResult
import com.kirdevelopment.data.remote.api.CoursesApi
import com.kirdevelopment.data.remote.mapper.CoursesResponseDtoMapper
import javax.inject.Inject

interface CoursesRemoteDataSource {
    suspend fun getCourses(): NetworkResult<List<Course>>
}

/**
 * В этом слое нет UI-моделей: remote источник отдает только domain-модели через mapper.
 */
class CoursesRemoteDataSourceImpl @Inject constructor(
    private val coursesApi: CoursesApi,
    private val safeApiCall: SafeApiCall,
    private val coursesResponseDtoMapper: CoursesResponseDtoMapper
) : CoursesRemoteDataSource {

    override suspend fun getCourses(): NetworkResult<List<Course>> {
        return when (val result = safeApiCall.execute { coursesApi.getCourses() }) {
            is NetworkResult.Success -> NetworkResult.Success(coursesResponseDtoMapper.map(result.data))
            is NetworkResult.Error -> result
        }
    }
}
