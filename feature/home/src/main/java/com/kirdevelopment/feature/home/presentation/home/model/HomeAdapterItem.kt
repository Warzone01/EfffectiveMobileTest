package com.kirdevelopment.feature.home.presentation.home.model

sealed interface HomeAdapterItem {
    val id: String
    val viewType: Int

    data class HeaderItem(
        override val id: String = "header",
        val title: String
    ) : HomeAdapterItem {
        override val viewType: Int = VIEW_TYPE_HEADER
    }

    data class CourseItem(
        val course: HomeCourseUiModel
    ) : HomeAdapterItem {
        override val id: String = "course_${course.id}"
        override val viewType: Int = VIEW_TYPE_COURSE
    }

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_COURSE = 2
    }
}
