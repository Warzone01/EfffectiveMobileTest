package com.kirdevelopment.feature.favorites.presentation.favorites.model

data class FavoriteCourseUiModel(
    val courseId: Long,
    val title: String,
    val description: String,
    val price: String,
    val startDate: String,
    val rate: String
)
