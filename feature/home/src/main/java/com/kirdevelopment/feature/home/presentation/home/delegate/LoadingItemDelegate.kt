package com.kirdevelopment.feature.home.presentation.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.databinding.ItemLoadingBinding
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class LoadingItemDelegate @Inject constructor() : HomeItemDelegate {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_LOADING

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.LoadingItem
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(ItemLoadingBinding.inflate(inflater, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, item: HomeAdapterItem, callbacks: HomeAdapterCallbacks) = Unit

    private class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
}
