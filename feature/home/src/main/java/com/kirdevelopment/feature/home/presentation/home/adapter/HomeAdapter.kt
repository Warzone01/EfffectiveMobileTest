package com.kirdevelopment.feature.home.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.presentation.home.delegate.HomeAdapterCallbacks
import com.kirdevelopment.feature.home.presentation.home.delegate.HomeDelegatesRegistry
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem

class HomeAdapter(
    private val delegatesRegistry: HomeDelegatesRegistry,
    private val onCourseClick: (Long) -> Unit,
    private val onFavoriteClick: (Long) -> Unit,
    private val onRetryClick: () -> Unit
) : ListAdapter<HomeAdapterItem, RecyclerView.ViewHolder>(DiffCallback) {

    private val callbacks = HomeAdapterCallbacks(
        onCourseClick = onCourseClick,
        onFavoriteClick = onFavoriteClick,
        onRetryClick = onRetryClick
    )

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id.hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return delegatesRegistry.getViewType(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = delegatesRegistry.getDelegateByViewType(viewType)
        return delegate.createViewHolder(android.view.LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val delegate = delegatesRegistry.getDelegateByViewType(holder.itemViewType)
        delegate.bind(holder, item, callbacks)
    }

    private object DiffCallback : DiffUtil.ItemCallback<HomeAdapterItem>() {
        override fun areItemsTheSame(oldItem: HomeAdapterItem, newItem: HomeAdapterItem): Boolean {
            return oldItem.id == newItem.id && oldItem.viewType == newItem.viewType
        }

        override fun areContentsTheSame(oldItem: HomeAdapterItem, newItem: HomeAdapterItem): Boolean {
            return oldItem == newItem
        }
    }
}
