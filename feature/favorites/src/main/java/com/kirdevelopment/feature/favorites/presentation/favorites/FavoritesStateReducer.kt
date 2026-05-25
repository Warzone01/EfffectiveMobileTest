package com.kirdevelopment.feature.favorites.presentation.favorites

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesStateReducer @Inject constructor() {

    fun loading(state: FavoritesUiState): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Loading, items = emptyList(), isRefreshing = false)
    }

    fun content(state: FavoritesUiState, items: List<FavoritesAdapterItem>): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Content, items = items, isRefreshing = false)
    }

    fun empty(state: FavoritesUiState, items: List<FavoritesAdapterItem>): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Empty, items = items, isRefreshing = false)
    }

    fun error(state: FavoritesUiState, message: UiText): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Error(message), items = emptyList(), isRefreshing = false)
    }

    fun refreshing(state: FavoritesUiState, enabled: Boolean): FavoritesUiState {
        return state.copy(isRefreshing = enabled)
    }
}
