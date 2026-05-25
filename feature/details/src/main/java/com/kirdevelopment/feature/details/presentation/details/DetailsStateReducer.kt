package com.kirdevelopment.feature.details.presentation.details

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.details.presentation.details.model.DetailsCourseUiModel
import javax.inject.Inject

class DetailsStateReducer @Inject constructor() {

    fun loading(state: DetailsUiState): DetailsUiState {
        return state.copy(screenState = DetailsScreenState.Loading, isRefreshing = false)
    }

    fun content(state: DetailsUiState, course: DetailsCourseUiModel): DetailsUiState {
        return state.copy(screenState = DetailsScreenState.Content(course), course = course, isRefreshing = false)
    }

    fun error(state: DetailsUiState, message: UiText): DetailsUiState {
        return state.copy(screenState = DetailsScreenState.Error(message), isRefreshing = false)
    }

    fun refreshing(state: DetailsUiState, enabled: Boolean): DetailsUiState {
        return state.copy(isRefreshing = enabled)
    }
}
