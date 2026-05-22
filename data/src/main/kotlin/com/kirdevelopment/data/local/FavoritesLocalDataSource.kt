package com.kirdevelopment.data.local

import com.kirdevelopment.core.model.favorite.FavoriteCourse
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {
    fun observeFavorites(): Flow<List<FavoriteCourse>>
    fun observeFavoriteIds(): Flow<Set<Long>>
    suspend fun isFavorite(courseId: Long): Boolean
    suspend fun addFavorite(courseId: Long)
    suspend fun removeFavorite(courseId: Long)
    suspend fun replaceFavorites(courseIds: Set<Long>)
}
