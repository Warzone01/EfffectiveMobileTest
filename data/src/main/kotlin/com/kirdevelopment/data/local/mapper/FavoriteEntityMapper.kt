package com.kirdevelopment.data.local.mapper

import com.kirdevelopment.core.database.entity.FavoriteCourseEntity
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import javax.inject.Inject

class FavoriteEntityMapper @Inject constructor() {
    fun toModel(entity: FavoriteCourseEntity): FavoriteCourse {
        return FavoriteCourse(
            courseId = entity.courseId,
            updatedAt = entity.updatedAt
        )
    }

    fun toEntity(model: FavoriteCourse): FavoriteCourseEntity {
        return FavoriteCourseEntity(
            courseId = model.courseId,
            updatedAt = model.updatedAt
        )
    }
}
