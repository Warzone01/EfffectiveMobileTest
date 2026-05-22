package com.kirdevelopment.data.remote.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.data.remote.dto.CourseDto
import com.kirdevelopment.data.remote.dto.CoursesResponseDto
import javax.inject.Inject

class CourseDtoMapper @Inject constructor() : Mapper<CourseDto, Course> {
    override fun map(input: CourseDto): Course {
        return Course(
            id = input.id,
            title = input.title,
            text = input.text,
            price = input.price,
            rate = input.rate,
            startDate = input.startDate,
            hasLike = input.hasLike,
            publishDate = input.publishDate
        )
    }
}

/**
 * Маппер отделяет DTO от domain-моделей и не допускает протекания API-слоя выше data.
 */
class CoursesResponseDtoMapper @Inject constructor(
    private val courseDtoMapper: CourseDtoMapper
) : Mapper<CoursesResponseDto, List<Course>> {
    override fun map(input: CoursesResponseDto): List<Course> {
        return input.courses.map(courseDtoMapper::map)
    }
}
