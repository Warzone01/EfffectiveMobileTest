package com.kirdevelopment.core.model.course

/**
 * Domain-модель курса.
 * Используется в use-case и data слое, не содержит зависимостей от API и UI.
 */
data class Course(
    val id: Long,
    val title: String,
    val text: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String
)
