package com.kirdevelopment.feature.home.presentation.home

import com.kirdevelopment.core.common.ui.UiText

sealed interface HomeUiEffect {
    data class NavigateToCourseDetails(val courseId: Long) : HomeUiEffect
    data class ShowMessage(val message: UiText) : HomeUiEffect
}
