package com.kirdevelopment.feature.favorites.presentation.favorites.delegate

import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import javax.inject.Inject

class FavoriteCourseItemDelegate @Inject constructor() : FavoritesItemDelegate<FavoritesAdapterItem.CourseItem> {
    override val viewType: Int = FavoritesAdapterItem.VIEW_TYPE_COURSE

    override fun isForItem(item: FavoritesAdapterItem): Boolean {
        return item is FavoritesAdapterItem.CourseItem
    }
}
