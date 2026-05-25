package com.kirdevelopment.feature.favorites.presentation.favorites

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem

data class FavoritesUiState(
    val screenState: FavoritesScreenState = FavoritesScreenState.Loading,
    val items: List<FavoritesAdapterItem> = emptyList(),
    val isRefreshing: Boolean = false
)

sealed interface FavoritesScreenState {
    data object Loading : FavoritesScreenState
    data object Content : FavoritesScreenState
    data object Empty : FavoritesScreenState
    data class Error(val message: UiText) : FavoritesScreenState
}
