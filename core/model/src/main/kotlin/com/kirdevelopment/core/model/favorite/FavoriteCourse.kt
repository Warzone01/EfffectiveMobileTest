package com.kirdevelopment.core.model.favorite

/**
 * Модель локально сохраненного избранного.
 * Отделена от Entity, чтобы domain и data не зависели от Room API.
 */
data class FavoriteCourse(
    val courseId: Long,
    val updatedAt: Long
)
