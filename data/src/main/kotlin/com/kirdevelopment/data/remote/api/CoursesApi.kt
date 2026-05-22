package com.kirdevelopment.data.remote.api

import com.kirdevelopment.data.remote.dto.CoursesResponseDto
import com.kirdevelopment.core.network.NetworkConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface CoursesApi {
    @GET("u/0/uc")
    suspend fun getCourses(
        @Query("id") fileId: String = NetworkConstants.COURSES_FILE_ID,
        @Query("export") export: String = "download"
    ): CoursesResponseDto
}
