package com.kirdevelopment.feature.details.presentation.details.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.feature.details.presentation.details.model.DetailsCourseUiModel
import javax.inject.Inject

class CourseToDetailsCourseUiMapper @Inject constructor() : Mapper<Course, DetailsCourseUiModel> {
    override fun map(input: Course): DetailsCourseUiModel {
        return DetailsCourseUiModel(
            id = input.id,
            title = input.title,
            description = input.text,
            price = input.price,
            rate = input.rate,
            startDate = input.startDate,
            isFavorite = input.hasLike
        )
    }
}
