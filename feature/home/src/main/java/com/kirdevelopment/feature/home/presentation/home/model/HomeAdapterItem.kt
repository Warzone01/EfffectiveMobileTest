package com.kirdevelopment.feature.home.presentation.home.model

sealed interface HomeAdapterItem {
    val id: String
    val viewType: Int

    data class CourseItem(
        val course: HomeCourseUiModel
    ) : HomeAdapterItem {
        override val id: String = "course_${course.id}"
        override val viewType: Int = VIEW_TYPE_COURSE
    }

    data object LoadingItem : HomeAdapterItem {
        override val id: String = "loading"
        override val viewType: Int = VIEW_TYPE_LOADING
    }

    data class ErrorItem(
        val message: String
    ) : HomeAdapterItem {
        override val id: String = "error"
        override val viewType: Int = VIEW_TYPE_ERROR
    }

    data class EmptyItem(
        val message: String
    ) : HomeAdapterItem {
        override val id: String = "empty"
        override val viewType: Int = VIEW_TYPE_EMPTY
    }

    companion object {
        const val VIEW_TYPE_COURSE = 1
        const val VIEW_TYPE_LOADING = 2
        const val VIEW_TYPE_ERROR = 3
        const val VIEW_TYPE_EMPTY = 4
    }
}
