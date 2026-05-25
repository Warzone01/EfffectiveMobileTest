package com.kirdevelopment.feature.home.presentation.home.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class CoursesToAdapterItemsMapper @Inject constructor(
    private val courseMapper: CourseToHomeCourseUiMapper
) : Mapper<List<Course>, List<HomeAdapterItem>> {

    override fun map(input: List<Course>): List<HomeAdapterItem> {
        if (input.isEmpty()) return emptyList()

        val header = HomeAdapterItem.HeaderItem(title = "Курсы")
        val courses = input.map { course -> HomeAdapterItem.CourseItem(courseMapper.map(course)) }
        return listOf(header) + courses
    }
}
