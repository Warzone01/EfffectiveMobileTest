package com.kirdevelopment.feature.details.presentation.details

import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.details.presentation.details.model.DetailsCourseUiModel

data class DetailsUiState(
    val screenState: DetailsScreenState = DetailsScreenState.Loading,
    val course: DetailsCourseUiModel? = null,
    val isRefreshing: Boolean = false
)

sealed interface DetailsScreenState {
    data object Loading : DetailsScreenState
    data class Content(val course: DetailsCourseUiModel) : DetailsScreenState
    data class Error(val message: UiText) : DetailsScreenState
}
