package com.kirdevelopment.feature.home.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.R
import com.kirdevelopment.feature.home.presentation.home.delegate.HomeDelegatesRegistry
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem

class HomeAdapter(
    private val delegatesRegistry: HomeDelegatesRegistry,
    private val onCourseClick: (Long) -> Unit,
    private val onFavoriteClick: (Long) -> Unit,
    private val onRetryClick: () -> Unit
) : ListAdapter<HomeAdapterItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return delegatesRegistry.getViewType(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HomeAdapterItem.VIEW_TYPE_COURSE -> CourseViewHolder(inflater.inflate(R.layout.item_home_course, parent, false))
            HomeAdapterItem.VIEW_TYPE_LOADING -> LoadingViewHolder(inflater.inflate(R.layout.item_home_loading, parent, false))
            HomeAdapterItem.VIEW_TYPE_ERROR -> ErrorViewHolder(inflater.inflate(R.layout.item_home_error, parent, false))
            HomeAdapterItem.VIEW_TYPE_EMPTY -> EmptyViewHolder(inflater.inflate(R.layout.item_home_empty, parent, false))
            else -> error("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is HomeAdapterItem.CourseItem -> (holder as CourseViewHolder).bind(item, onCourseClick, onFavoriteClick)
            is HomeAdapterItem.ErrorItem -> (holder as ErrorViewHolder).bind(item, onRetryClick)
            is HomeAdapterItem.EmptyItem -> (holder as EmptyViewHolder).bind(item)
            HomeAdapterItem.LoadingItem -> Unit
        }
    }

    private class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.course_title)
        private val description: TextView = itemView.findViewById(R.id.course_description)
        private val price: TextView = itemView.findViewById(R.id.course_price)
        private val date: TextView = itemView.findViewById(R.id.course_date)
        private val rate: TextView = itemView.findViewById(R.id.course_rate)
        private val favorite: ImageButton = itemView.findViewById(R.id.course_favorite)

        fun bind(
            item: HomeAdapterItem.CourseItem,
            onCourseClick: (Long) -> Unit,
            onFavoriteClick: (Long) -> Unit
        ) {
            val course = item.course
            title.text = course.title
            description.text = course.description
            price.text = course.price
            date.text = course.startDate
            rate.text = course.rate
            favorite.setImageResource(
                if (course.isFavorite) android.R.drawable.btn_star_big_on
                else android.R.drawable.btn_star_big_off
            )

            itemView.setOnClickListener { onCourseClick(course.id) }
            favorite.setOnClickListener { onFavoriteClick(course.id) }
        }
    }

    private class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.error_message)
        private val retry: TextView = itemView.findViewById(R.id.error_retry)

        fun bind(item: HomeAdapterItem.ErrorItem, onRetryClick: () -> Unit) {
            message.text = item.message
            retry.setOnClickListener { onRetryClick() }
        }
    }

    private class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val message: TextView = itemView.findViewById(R.id.empty_message)

        fun bind(item: HomeAdapterItem.EmptyItem) {
            message.text = item.message
        }
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
