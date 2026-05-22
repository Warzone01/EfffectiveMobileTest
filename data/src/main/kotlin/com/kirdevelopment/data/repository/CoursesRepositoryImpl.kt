package com.kirdevelopment.data.repository

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.network.model.NetworkResult
import com.kirdevelopment.data.local.FavoritesLocalDataSource
import com.kirdevelopment.data.mapper.CourseMergeMapper
import com.kirdevelopment.data.remote.CoursesRemoteDataSource
import com.kirdevelopment.domain.repository.CoursesRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class CoursesRepositoryImpl @Inject constructor(
    private val coursesRemoteDataSource: CoursesRemoteDataSource,
    private val favoritesLocalDataSource: FavoritesLocalDataSource,
    private val courseMergeMapper: CourseMergeMapper
) : CoursesRepository {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val cacheMutex = Mutex()
    private var cachedCourses: List<Course> = emptyList()

    override suspend fun getCourses(): AppResult<List<Course>> {
        val favoriteIds = favoritesLocalDataSource.observeFavoriteIds().first()

        return when (val remoteResult = coursesRemoteDataSource.getCourses()) {
            is NetworkResult.Success -> {
                val mergedCourses = courseMergeMapper.mergeWithFavorites(remoteResult.data, favoriteIds)
                val sortedCourses = sortByPublishDateDesc(mergedCourses)
                cacheMutex.withLock {
                    cachedCourses = sortedCourses
                }
                AppResult.Success(sortedCourses)
            }

            is NetworkResult.Error -> {
                val cachedSnapshot = cacheMutex.withLock { cachedCourses }
                if (cachedSnapshot.isNotEmpty()) {
                    val mergedCached = courseMergeMapper.mergeWithFavorites(cachedSnapshot, favoriteIds)
                    AppResult.Success(sortByPublishDateDesc(mergedCached))
                } else {
                    AppResult.Error(remoteResult.error)
                }
            }
        }
    }

    private fun sortByPublishDateDesc(courses: List<Course>): List<Course> {
        return courses.sortedByDescending { course ->
            runCatching { LocalDate.parse(course.publishDate, dateFormatter) }.getOrDefault(LocalDate.MIN)
        }
    }
}
