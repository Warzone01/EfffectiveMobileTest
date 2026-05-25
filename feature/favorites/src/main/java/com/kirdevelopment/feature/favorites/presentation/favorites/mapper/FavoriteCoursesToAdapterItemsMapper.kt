package com.kirdevelopment.feature.favorites.presentation.favorites.mapper

import com.kirdevelopment.core.common.mapper.Mapper
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoriteCourseUiModel
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoriteCoursesToAdapterItemsMapper @Inject constructor() : Mapper<List<FavoriteCourseUiModel>, List<FavoritesAdapterItem>> {
    override fun map(input: List<FavoriteCourseUiModel>): List<FavoritesAdapterItem> {
        if (input.isEmpty()) return emptyList()
        return input.map { course -> FavoritesAdapterItem.CourseItem(course) }
    }
}
