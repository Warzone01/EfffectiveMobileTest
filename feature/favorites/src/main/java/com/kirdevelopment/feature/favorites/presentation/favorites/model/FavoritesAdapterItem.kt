package com.kirdevelopment.feature.favorites.presentation.favorites.model

sealed interface FavoritesAdapterItem {
    val id: String
    val viewType: Int

    data class CourseItem(
        val course: FavoriteCourseUiModel
    ) : FavoritesAdapterItem {
        override val id: String = "favorite_${course.courseId}"
        override val viewType: Int = VIEW_TYPE_COURSE
    }

    data object LoadingItem : FavoritesAdapterItem {
        override val id: String = "loading"
        override val viewType: Int = VIEW_TYPE_LOADING
    }

    data class EmptyItem(
        val message: String
    ) : FavoritesAdapterItem {
        override val id: String = "empty"
        override val viewType: Int = VIEW_TYPE_EMPTY
    }

    data class ErrorItem(
        val message: String
    ) : FavoritesAdapterItem {
        override val id: String = "error"
        override val viewType: Int = VIEW_TYPE_ERROR
    }

    companion object {
        const val VIEW_TYPE_COURSE = 1
        const val VIEW_TYPE_LOADING = 2
        const val VIEW_TYPE_EMPTY = 3
        const val VIEW_TYPE_ERROR = 4
    }
}
