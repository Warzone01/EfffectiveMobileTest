package com.kirdevelopment.feature.favorites.presentation.favorites

import com.kirdevelopment.core.common.ui.UiText

sealed interface FavoritesUiEffect {
    data class ShowMessage(val message: UiText) : FavoritesUiEffect
    data class NavigateToCourseDetails(val courseId: Long) : FavoritesUiEffect
}
