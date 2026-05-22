package com.kirdevelopment.data.mapper

import com.kirdevelopment.core.model.course.Course
import javax.inject.Inject

/**
 * Отдельный mapper для объединения серверного состояния и локального избранного.
 * Правило из ТЗ: локальный флаг избранного должен сохраняться независимо от сети.
 */
class CourseMergeMapper @Inject constructor() {

    fun mergeWithFavorites(
        courses: List<Course>,
        favoriteIds: Set<Long>
    ): List<Course> {
        return courses.map { course ->
            val mergedHasLike = course.hasLike || favoriteIds.contains(course.id)
            if (mergedHasLike == course.hasLike) {
                course
            } else {
                course.copy(hasLike = true)
            }
        }
    }
}
