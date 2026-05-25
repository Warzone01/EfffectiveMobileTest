package com.kirdevelopment.feature.home.presentation.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.databinding.ItemEmptyBinding
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class EmptyItemDelegate @Inject constructor() : HomeItemDelegate {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_EMPTY

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.EmptyItem
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return EmptyViewHolder(ItemEmptyBinding.inflate(inflater, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, item: HomeAdapterItem, callbacks: HomeAdapterCallbacks) {
        val emptyItem = item as HomeAdapterItem.EmptyItem
        (holder as EmptyViewHolder).bind(emptyItem)
    }

    private class EmptyViewHolder(
        private val binding: ItemEmptyBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeAdapterItem.EmptyItem) {
            binding.emptyMessage.text = item.message
        }
    }
}
