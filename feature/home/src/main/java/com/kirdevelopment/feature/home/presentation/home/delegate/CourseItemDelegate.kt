package com.kirdevelopment.feature.home.presentation.home.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirdevelopment.feature.home.databinding.ItemCourseBinding
import com.kirdevelopment.feature.home.presentation.home.model.HomeAdapterItem
import javax.inject.Inject

class CourseItemDelegate @Inject constructor() : HomeItemDelegate {
    override val viewType: Int = HomeAdapterItem.VIEW_TYPE_COURSE

    override fun isForItem(item: HomeAdapterItem): Boolean {
        return item is HomeAdapterItem.CourseItem
    }

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder {
        val binding = ItemCourseBinding.inflate(inflater, parent, false)
        return CourseViewHolder(binding)
    }

    override fun bind(holder: RecyclerView.ViewHolder, item: HomeAdapterItem, callbacks: HomeAdapterCallbacks) {
        val courseItem = item as HomeAdapterItem.CourseItem
        (holder as CourseViewHolder).bind(courseItem, callbacks)
    }

    private class CourseViewHolder(
        private val binding: ItemCourseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: HomeAdapterItem.CourseItem, callbacks: HomeAdapterCallbacks) {
            val course = item.course
            binding.courseTitle.text = course.title
            binding.courseDescription.text = course.description
            binding.coursePrice.text = course.price
            binding.courseDate.text = course.startDate
            binding.courseRate.text = course.rate
            binding.courseFavorite.isSelected = course.isFavorite

            binding.courseCardRoot.setOnClickListener { callbacks.onCourseClick(course.id) }
            binding.courseFavorite.setOnClickListener { callbacks.onFavoriteClick(course.id) }
        }
    }
}
