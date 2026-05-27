package com.kirdevelopment.feature.favorites.presentation.favorites.mapper

import com.kirdevelopment.core.common.date.DateFormatter
import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoriteCourseUiModel
import javax.inject.Inject

class CourseToFavoriteCourseUiMapper @Inject constructor(
    private val dateFormatter: DateFormatter
) : Mapper<Course, FavoriteCourseUiModel> {

    override fun map(input: Course): FavoriteCourseUiModel {
        return FavoriteCourseUiModel(
            courseId = input.id,
            title = input.title,
            description = "${input.price} ₽",
            price = input.price,
            startDate = dateFormatter.format(input.startDate),
            rate = input.rate
        )
    }
}
