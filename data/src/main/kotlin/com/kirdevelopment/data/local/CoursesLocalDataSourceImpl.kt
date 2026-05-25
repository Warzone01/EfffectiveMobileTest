package com.kirdevelopment.data.local

import com.kirdevelopment.core.database.dao.CoursesDao
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import com.kirdevelopment.data.local.mapper.CourseEntityMapper
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoursesLocalDataSourceImpl @Inject constructor(
    private val coursesDao: CoursesDao,
    private val courseEntityMapper: CourseEntityMapper
) : CoursesLocalDataSource {

    override fun observeCourses(): Flow<List<Course>> {
        return coursesDao.observeCourses().map { entities ->
            entities.map(courseEntityMapper::toModel)
        }
    }

    override fun observeCourseById(courseId: Long): Flow<Course?> {
        return coursesDao.observeCourseById(courseId).map { entity ->
            entity?.let(courseEntityMapper::toModel)
        }
    }

    override fun observeFavoriteCourses(): Flow<List<Course>> {
        return coursesDao.observeFavoriteCourses().map { entities ->
            entities.map(courseEntityMapper::toModel)
        }
    }

    override fun observeFavorites(): Flow<List<FavoriteCourse>> {
        return coursesDao.observeFavoriteCourses().map { entities ->
            entities.map(courseEntityMapper::toFavorite)
        }
    }

    override suspend fun getFavoriteIds(): Set<Long> {
        return coursesDao.getFavoriteIds().toSet()
    }

    override suspend fun upsertCourses(courses: List<Course>) {
        val timestamp = System.currentTimeMillis()
        val entities = courses.map { course ->
            courseEntityMapper.toEntity(course, timestamp)
        }
        coursesDao.upsertCourses(entities)
    }

    override suspend fun toggleFavorite(courseId: Long): Boolean {
        val existing = coursesDao.getCourseById(courseId) ?: return false
        val nextState = !existing.hasLike
        coursesDao.updateFavoriteState(courseId = courseId, isFavorite = nextState, updatedAt = System.currentTimeMillis())
        return true
    }

    override suspend fun replaceFavoriteStates(favoriteIds: Set<Long>) {
        val now = System.currentTimeMillis()
        coursesDao.getAllCourses().forEach { course ->
            val isFavorite = favoriteIds.contains(course.id)
            if (course.hasLike != isFavorite) {
                coursesDao.updateFavoriteState(course.id, isFavorite, now)
            }
        }
    }
}
