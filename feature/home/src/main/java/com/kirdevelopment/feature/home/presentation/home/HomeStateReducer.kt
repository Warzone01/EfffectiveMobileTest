package com.kirdevelopment.feature.home.presentation.home

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class HomeStateReducer @Inject constructor() {

    fun loading(state: HomeUiState): HomeUiState {
        return state.copy(screenState = ScreenState.Loading, isRefreshing = false)
    }

    fun content(state: HomeUiState, items: List<HomeAdapterItem>): HomeUiState {
        return state.copy(screenState = ScreenState.Content, items = items, isRefreshing = false)
    }

    fun empty(state: HomeUiState): HomeUiState {
        return state.copy(screenState = ScreenState.Empty, items = emptyList(), isRefreshing = false)
    }

    fun error(state: HomeUiState, message: UiText): HomeUiState {
        return state.copy(screenState = ScreenState.Error(message), isRefreshing = false)
    }

    fun refreshing(state: HomeUiState, enabled: Boolean): HomeUiState {
        return state.copy(isRefreshing = enabled)
    }
}
