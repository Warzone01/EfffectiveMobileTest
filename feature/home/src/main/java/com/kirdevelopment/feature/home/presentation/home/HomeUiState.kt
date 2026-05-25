package com.kirdevelopment.feature.home.presentation.home

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem

data class HomeUiState(
    val screenState: ScreenState = ScreenState.Loading,
    val items: List<HomeAdapterItem> = emptyList(),
    val isRefreshing: Boolean = false,
    val isSortDescending: Boolean = true
)

sealed interface ScreenState {
    data object Loading : ScreenState
    data object Content : ScreenState
    data object Empty : ScreenState
    data class Error(val message: UiText) : ScreenState
}
