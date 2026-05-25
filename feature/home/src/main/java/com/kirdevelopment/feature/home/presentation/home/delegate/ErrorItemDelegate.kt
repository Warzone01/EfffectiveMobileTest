package com.kirdevelopment.feature.home.presentation.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.databinding.ItemErrorBinding
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class ErrorItemDelegate @Inject constructor() : HomeItemDelegate {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_ERROR

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.ErrorItem
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        return ErrorViewHolder(ItemErrorBinding.inflate(inflater, parent, false))
    }

    override fun bind(holder: RecyclerView.ViewHolder, item: HomeAdapterItem, callbacks: HomeAdapterCallbacks) {
        val errorItem = item as HomeAdapterItem.ErrorItem
        (holder as ErrorViewHolder).bind(errorItem, callbacks)
    }

    private class ErrorViewHolder(
        private val binding: ItemErrorBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeAdapterItem.ErrorItem, callbacks: HomeAdapterCallbacks) {
            binding.errorMessage.text = item.message
            binding.errorRetry.setOnClickListener { callbacks.onRetryClick() }
        }
    }
}
