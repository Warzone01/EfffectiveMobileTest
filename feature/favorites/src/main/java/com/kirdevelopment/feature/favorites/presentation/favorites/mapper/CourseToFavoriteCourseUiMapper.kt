package com.kirdevelopment.feature.favorites.presentation.favorites.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoriteCourseUiModel
import javax.inject.Inject

class CourseToFavoriteCourseUiMapper @Inject constructor() : Mapper<Course, FavoriteCourseUiModel> {
    override fun map(input: Course): FavoriteCourseUiModel {
        return FavoriteCourseUiModel(
            courseId = input.id,
            title = input.title,
            description = input.text,
            price = input.price,
            startDate = input.startDate,
            rate = input.rate
        )
    }
}
