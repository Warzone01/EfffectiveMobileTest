package com.kirdevelopment.domain.usecase

import com.kirdevelopment.core.model.course.Course
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class SortCoursesByPublishDateUseCase @Inject constructor() {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    fun execute(courses: List<Course>): List<Course> {
        return courses.sortedByDescending { course ->
            parseDate(course.publishDate)
        }
    }

    private fun parseDate(value: String): LocalDate {
        return runCatching { LocalDate.parse(value, formatter) }
            .getOrElse { LocalDate.MIN }
    }
}
