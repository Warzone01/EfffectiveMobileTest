package com.kirdevelopment.feature.details.presentation.details.model

data class DetailsCourseUiModel(
    val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val isFavorite: Boolean
)
