package com.kirdevelopment.domain.repository

import com.kirdevelopment.core.model.favorite.FavoriteCourse
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<List<FavoriteCourse>>
    fun observeFavoriteIds(): Flow<Set<Long>>
    suspend fun toggleFavorite(courseId: Long)
    suspend fun syncFavorites(actualFavoriteIds: Set<Long>)
}
