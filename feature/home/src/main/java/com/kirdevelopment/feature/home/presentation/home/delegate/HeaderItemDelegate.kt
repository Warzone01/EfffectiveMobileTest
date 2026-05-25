package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class HeaderItemDelegate @Inject constructor() : HomeItemDelegate<HomeAdapterItem.HeaderItem> {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_HEADER

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.HeaderItem
    }
}
