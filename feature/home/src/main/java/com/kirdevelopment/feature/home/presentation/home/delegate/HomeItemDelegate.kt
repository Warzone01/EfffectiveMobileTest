package com.kirdevelopment.feature.home.presentation.home.delegate

import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem

interface HomeItemDelegate<T : HomeAdapterItem> {
    val viewType: Int
    fun isForItem(item: HomeAdapterItem): Boolean
}
