package com.kirdevelopment.feature.details.presentation.details

sealed interface DetailsUiEvent {
    data class ScreenOpened(val courseId: Long) : DetailsUiEvent
    data object RetryClicked : DetailsUiEvent
    data object FavoriteClicked : DetailsUiEvent
    data object BackClicked : DetailsUiEvent
}
