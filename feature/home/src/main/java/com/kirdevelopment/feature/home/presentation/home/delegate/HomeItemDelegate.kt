package com.kirdevelopment.feature.home.presentation.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem

interface HomeItemDelegate {
    val viewType: Int
    fun isForItem(item: HomeAdapterItem): Boolean
    fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder
    fun bind(holder: RecyclerView.ViewHolder, item: HomeAdapterItem, callbacks: HomeAdapterCallbacks)

    fun isForViewType(inputViewType: Int): Boolean {
        return viewType == inputViewType
    }
}

data class HomeAdapterCallbacks(
    val onCourseClick: (Long) -> Unit,
    val onFavoriteClick: (Long) -> Unit,
    val onRetryClick: () -> Unit
)
