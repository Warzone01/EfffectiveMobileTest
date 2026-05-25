package com.kirdevelopment.data.repository

import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.network.model.NetworkResult
import com.kirdevelopment.data.local.CoursesLocalDataSource
import com.kirdevelopment.data.remote.CoursesRemoteDataSource
import com.kirdevelopment.domain.repository.CoursesRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Singleton
class CoursesRepositoryImpl @Inject constructor(
    private val coursesRemoteDataSource: CoursesRemoteDataSource,
    private val coursesLocalDataSource: CoursesLocalDataSource
) : CoursesRepository {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override suspend fun getCourses(): AppResult<List<Course>> {
        val refreshResult = refreshCourses()
        val localCourses = observeCoursesSnapshot()

        if (localCourses.isNotEmpty()) {
            return AppResult.Success(localCourses)
        }

        return when (refreshResult) {
            is AppResult.Success -> AppResult.Success(localCourses)
            is AppResult.Error -> AppResult.Error(refreshResult.error)
        }
    }

    override suspend fun refreshCourses(): AppResult<Unit> {
        val localFavoriteIds = coursesLocalDataSource.getFavoriteIds()
        return when (val remoteResult = coursesRemoteDataSource.getCourses()) {
            is NetworkResult.Success -> {
                val mergedCourses = remoteResult.data.map { course ->
                    course.copy(hasLike = course.hasLike || localFavoriteIds.contains(course.id))
                }
                coursesLocalDataSource.upsertCourses(sortByPublishDateDesc(mergedCourses))
                AppResult.Success(Unit)
            }

            is NetworkResult.Error -> {
                AppResult.Error(remoteResult.error)
            }
        }
    }

    override fun observeCourses(): Flow<List<Course>> {
        return coursesLocalDataSource.observeCourses()
    }

    override fun observeCourseById(courseId: Long): Flow<Course?> {
        return coursesLocalDataSource.observeCourseById(courseId)
    }

    private suspend fun observeCoursesSnapshot(): List<Course> {
        return sortByPublishDateDesc(coursesLocalDataSource.observeCourses().first())
    }

    private fun sortByPublishDateDesc(courses: List<Course>): List<Course> {
        return courses.sortedByDescending { course ->
            runCatching { LocalDate.parse(course.publishDate, dateFormatter) }.getOrDefault(LocalDate.MIN)
        }
    }
}
