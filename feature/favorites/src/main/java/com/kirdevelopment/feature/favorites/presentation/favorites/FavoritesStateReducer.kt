package com.kirdevelopment.feature.favorites.presentation.favorites

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoritesStateReducer @Inject constructor() {

    fun loading(state: FavoritesUiState): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Loading, items = emptyList())
    }

    fun content(state: FavoritesUiState, items: List<FavoritesAdapterItem>): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Content, items = items)
    }

    fun empty(state: FavoritesUiState, items: List<FavoritesAdapterItem>): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Empty, items = items)
    }

    fun error(state: FavoritesUiState, items: List<FavoritesAdapterItem>): FavoritesUiState {
        return state.copy(screenState = FavoritesScreenState.Error, items = items)
    }
}
