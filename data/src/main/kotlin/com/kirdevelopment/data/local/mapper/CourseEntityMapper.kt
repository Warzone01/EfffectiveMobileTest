package com.kirdevelopment.data.local.mapper

import com.kirdevelopment.core.database.entity.CourseEntity
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.core.model.favorite.FavoriteCourse
import javax.inject.Inject

class CourseEntityMapper @Inject constructor() {

    fun toModel(entity: CourseEntity): Course {
        return Course(
            id = entity.id,
            title = entity.title,
            text = entity.text,
            price = entity.price,
            rate = entity.rate,
            startDate = entity.startDate,
            hasLike = entity.hasLike,
            publishDate = entity.publishDate
        )
    }

    fun toEntity(model: Course, updatedAt: Long): CourseEntity {
        return CourseEntity(
            id = model.id,
            title = model.title,
            text = model.text,
            price = model.price,
            rate = model.rate,
            startDate = model.startDate,
            hasLike = model.hasLike,
            publishDate = model.publishDate,
            updatedAt = updatedAt
        )
    }

    fun toFavorite(entity: CourseEntity): FavoriteCourse {
        return FavoriteCourse(
            courseId = entity.id,
            updatedAt = entity.updatedAt
        )
    }
}
