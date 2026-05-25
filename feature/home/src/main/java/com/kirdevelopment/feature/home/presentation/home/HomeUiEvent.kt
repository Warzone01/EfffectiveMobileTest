package com.kirdevelopment.feature.home.presentation.home

sealed interface HomeUiEvent {
    data object ScreenOpened : HomeUiEvent
    data object RetryClicked : HomeUiEvent
    data object SortClicked : HomeUiEvent
    data class CourseClicked(val courseId: Long) : HomeUiEvent
    data class FavoriteClicked(val courseId: Long) : HomeUiEvent
}
