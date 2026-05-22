package com.kirdevelopment.data.repository

import com.kirdevelopment.core.model.favorite.FavoriteCourse
import com.kirdevelopment.data.local.FavoritesLocalDataSource
import com.kirdevelopment.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<FavoriteCourse>> {
        return favoritesLocalDataSource.observeFavorites()
    }

    override fun observeFavoriteIds(): Flow<Set<Long>> {
        return favoritesLocalDataSource.observeFavoriteIds()
    }

    override suspend fun toggleFavorite(courseId: Long) {
        if (favoritesLocalDataSource.isFavorite(courseId)) {
            favoritesLocalDataSource.removeFavorite(courseId)
        } else {
            favoritesLocalDataSource.addFavorite(courseId)
        }
    }

    override suspend fun syncFavorites(actualFavoriteIds: Set<Long>) {
        favoritesLocalDataSource.replaceFavorites(actualFavoriteIds)
    }
}
