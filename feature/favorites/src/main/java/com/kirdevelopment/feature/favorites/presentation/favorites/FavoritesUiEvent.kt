package com.kirdevelopment.feature.favorites.presentation.favorites

sealed interface FavoritesUiEvent {
    data object ScreenOpened : FavoritesUiEvent
    data object RetryClicked : FavoritesUiEvent
    data class RemoveClicked(val courseId: Long) : FavoritesUiEvent
    data class CourseClicked(val courseId: Long) : FavoritesUiEvent
}
