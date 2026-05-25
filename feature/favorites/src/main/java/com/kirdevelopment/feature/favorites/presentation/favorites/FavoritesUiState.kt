package com.kirdevelopment.feature.favorites.presentation.favorites

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem

data class FavoritesUiState(
    val screenState: FavoritesScreenState = FavoritesScreenState.Loading,
    val items: List<FavoritesAdapterItem> = emptyList()
)

sealed interface FavoritesScreenState {
    data object Loading : FavoritesScreenState
    data object Content : FavoritesScreenState
    data object Empty : FavoritesScreenState
    data object Error : FavoritesScreenState
}
