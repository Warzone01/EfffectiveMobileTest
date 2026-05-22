package com.kirdevelopment.data.local

import com.kirdevelopment.core.database.dao.FavoritesDao
import com.kirdevelopment.core.database.entity.FavoriteCourseEntity
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import com.kirdevelopment.data.local.mapper.FavoriteEntityMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Локальный источник хранит состояние избранного независимо от сети,
 * поэтому избранное переживает перезапуск приложения и работает офлайн.
 */
class FavoritesLocalDataSourceImpl @Inject constructor(
    private val favoritesDao: FavoritesDao,
    private val favoriteEntityMapper: FavoriteEntityMapper
) : FavoritesLocalDataSource {

    override fun observeFavorites(): Flow<List<FavoriteCourse>> {
        return favoritesDao.observeFavorites().map { entities ->
            entities.map(favoriteEntityMapper::toModel)
        }
    }

    override fun observeFavoriteIds(): Flow<Set<Long>> {
        return favoritesDao.observeFavoriteIds().map(List<Long>::toSet)
    }

    override suspend fun isFavorite(courseId: Long): Boolean {
        return favoritesDao.isFavorite(courseId)
    }

    override suspend fun addFavorite(courseId: Long) {
        favoritesDao.upsertFavorite(
            FavoriteCourseEntity(
                courseId = courseId,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun removeFavorite(courseId: Long) {
        favoritesDao.deleteFavorite(courseId)
    }

    override suspend fun replaceFavorites(courseIds: Set<Long>) {
        favoritesDao.clearFavorites()
        courseIds.forEach { addFavorite(it) }
    }
}
