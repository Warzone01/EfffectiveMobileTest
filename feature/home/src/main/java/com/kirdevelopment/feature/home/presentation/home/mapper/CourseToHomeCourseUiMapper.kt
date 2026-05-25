package com.kirdevelopment.feature.home.presentation.home.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.feature.home.presentation.home.model.HomeCourseUiModel
import javax.inject.Inject

class CourseToHomeCourseUiMapper @Inject constructor() : Mapper<Course, HomeCourseUiModel> {
    override fun map(input: Course): HomeCourseUiModel {
        return HomeCourseUiModel(
            id = input.id,
            title = input.title,
            description = input.text,
            price = input.price,
            startDate = input.startDate,
            rate = input.rate,
            isFavorite = input.hasLike
        )
    }
}
