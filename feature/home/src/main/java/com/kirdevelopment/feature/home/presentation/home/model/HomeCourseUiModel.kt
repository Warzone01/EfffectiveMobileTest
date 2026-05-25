package com.kirdevelopment.feature.home.presentation.home.model

data class HomeCourseUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val startDate: String,
    val rate: String,
    val isFavorite: Boolean
)
