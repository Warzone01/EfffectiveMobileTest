package com.kirdevelopment.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoursesResponseDto(
    @SerialName("courses") val courses: List<CourseDto>
)

@Serializable
data class CourseDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("text") val text: String,
    @SerialName("price") val price: String,
    @SerialName("rate") val rate: String,
    @SerialName("startDate") val startDate: String,
    @SerialName("hasLike") val hasLike: Boolean,
    @SerialName("publishDate") val publishDate: String
)
