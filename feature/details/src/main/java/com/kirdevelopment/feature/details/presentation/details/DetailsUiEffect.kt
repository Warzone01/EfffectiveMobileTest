package com.kirdevelopment.feature.details.presentation.details

import com.kirdevelopment.core.common.ui.UiText

sealed interface DetailsUiEffect {
    data class ShowMessage(val message: UiText) : DetailsUiEffect
    data object NavigateBack : DetailsUiEffect
}
