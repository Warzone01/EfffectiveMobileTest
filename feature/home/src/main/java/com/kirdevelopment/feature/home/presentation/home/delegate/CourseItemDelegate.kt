package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class CourseItemDelegate @Inject constructor() : HomeItemDelegate<HomeAdapterItem.CourseItem> {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_COURSE

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.CourseItem
    }
}
