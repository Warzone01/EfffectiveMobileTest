package com.kirdevelopment.data.repository

import com.kirdevelopment.core.model.favorite.FavoriteCourse
import com.kirdevelopment.data.local.CoursesLocalDataSource
import com.kirdevelopment.domain.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl @Inject constructor(
    private val coursesLocalDataSource: CoursesLocalDataSource
) : FavoritesRepository {

    override fun observeFavorites(): Flow<List<FavoriteCourse>> {
        return coursesLocalDataSource.observeFavorites()
    }

    override fun observeFavoriteIds(): Flow<Set<Long>> {
        return coursesLocalDataSource.observeFavorites().map { favorites ->
            favorites.map { it.courseId }.toSet()
        }
    }

    override suspend fun toggleFavorite(courseId: Long) {
        coursesLocalDataSource.toggleFavorite(courseId)
    }

    override suspend fun syncFavorites(actualFavoriteIds: Set<Long>) {
        coursesLocalDataSource.replaceFavoriteStates(actualFavoriteIds)
    }
}
