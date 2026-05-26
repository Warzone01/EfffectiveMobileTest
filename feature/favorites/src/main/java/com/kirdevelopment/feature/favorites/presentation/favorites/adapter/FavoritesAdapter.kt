package com.kirdevelopment.feature.favorites.presentation.favorites.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.favorites.R
import com.kirdevelopment.feature.favorites.presentation.favorites.delegate.FavoritesDelegatesRegistry
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem

class FavoritesAdapter(
    private val delegatesRegistry: FavoritesDelegatesRegistry,
    private val onCourseClick: (Long) -> Unit,
    private val onRemoveClick: (Long) -> Unit,
    private val onRetryClick: () -> Unit
) : ListAdapter<FavoritesAdapterItem, RecyclerView.ViewHolder>(DiffCallback) {

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
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            FavoritesAdapterItem.VIEW_TYPE_COURSE -> CourseViewHolder(inflater.inflate(R.layout.item_favorites_course, parent, false))
            FavoritesAdapterItem.VIEW_TYPE_LOADING -> LoadingViewHolder(inflater.inflate(R.layout.item_favorites_loading, parent, false))
            FavoritesAdapterItem.VIEW_TYPE_EMPTY -> EmptyViewHolder(inflater.inflate(R.layout.item_favorites_empty, parent, false))
            FavoritesAdapterItem.VIEW_TYPE_ERROR -> ErrorViewHolder(inflater.inflate(R.layout.item_favorites_error, parent, false))
            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is FavoritesAdapterItem.CourseItem -> (holder as CourseViewHolder).bind(item, onCourseClick, onRemoveClick)
            is FavoritesAdapterItem.EmptyItem -> (holder as EmptyViewHolder).bind(item)
            is FavoritesAdapterItem.ErrorItem -> (holder as ErrorViewHolder).bind(item, onRetryClick)
            FavoritesAdapterItem.LoadingItem -> Unit
        }
    }

    private class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.favorite_course_title)
        private val description: TextView = itemView.findViewById(R.id.favorite_course_description)
        private val price: TextView = itemView.findViewById(R.id.favorite_course_price)
        private val date: TextView = itemView.findViewById(R.id.favorite_course_date)
        private val rate: TextView = itemView.findViewById(R.id.favorite_course_rate)
        private val removeButton: ImageButton = itemView.findViewById(R.id.favorite_course_remove)

        fun bind(
            item: FavoritesAdapterItem.CourseItem,
            onCourseClick: (Long) -> Unit,
            onRemoveClick: (Long) -> Unit
        ) {
            val course = item.course
            title.text = course.title
            description.text = course.description
            price.text = course.price
            date.text = course.startDate
            rate.text = course.rate

            itemView.setOnClickListener { onCourseClick(course.courseId) }
            removeButton.setOnClickListener { onRemoveClick(course.courseId) }
        }
    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.favorite_empty_message)

        fun bind(item: FavoritesAdapterItem.EmptyItem) {
            message.text = item.message
        }
    }

    private class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.favorite_error_message)
        private val retry: TextView = itemView.findViewById(R.id.favorite_error_retry)

        fun bind(item: FavoritesAdapterItem.ErrorItem, onRetryClick: () -> Unit) {
            message.text = item.message
            retry.setOnClickListener { onRetryClick() }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<FavoritesAdapterItem>() {
        override fun areItemsTheSame(oldItem: FavoritesAdapterItem, newItem: FavoritesAdapterItem): Boolean {
            return oldItem.id == newItem.id && oldItem.viewType == newItem.viewType
        }

        override fun areContentsTheSame(oldItem: FavoritesAdapterItem, newItem: FavoritesAdapterItem): Boolean {
            return oldItem == newItem
        }
    }
}
