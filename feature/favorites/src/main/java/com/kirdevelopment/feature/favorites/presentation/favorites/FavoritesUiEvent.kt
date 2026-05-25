package com.kirdevelopment.feature.favorites.presentation.favorites

sealed interface FavoritesUiEvent {
    data object ScreenOpened : FavoritesUiEvent
}
