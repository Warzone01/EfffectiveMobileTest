package com.kirdevelopment.feature.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.usecase.GetCoursesUseCase
import com.kirdevelopment.domain.usecase.ObserveFavoritesUseCase
import com.kirdevelopment.feature.favorites.presentation.favorites.mapper.CourseToFavoriteCourseUiMapper
import com.kirdevelopment.feature.favorites.presentation.favorites.mapper.FavoriteCoursesToAdapterItemsMapper
import com.kirdevelopment.feature.favorites.presentation.favorites.model.FavoritesAdapterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val courseMapper: CourseToFavoriteCourseUiMapper,
    private val itemsMapper: FavoriteCoursesToAdapterItemsMapper,
    private val reducer: FavoritesStateReducer
) : ViewModel() {

    private var cachedCourses: List<Course> = emptyList()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            FavoritesUiEvent.ScreenOpened -> loadFavoritesData()
        }
    }

    private fun loadFavoritesData() {
        viewModelScope.launch {
            reduce { reducer.loading(it) }

            when (val result = getCoursesUseCase(NoParams)) {
                is AppResult.Success -> {
                    cachedCourses = result.data
                    observeFavorites()
                }

                is AppResult.Error -> {
                    reduce {
                        reducer.error(
                            state = it,
                            items = listOf(FavoritesAdapterItem.ErrorItem(message = "Не удалось загрузить избранное"))
                        )
                    }
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoritesUseCase(NoParams).collectLatest { favorites ->
                val favoriteIds = favorites.map { it.courseId }.toSet()
                val favoriteCourses = cachedCourses
                    .filter { course -> favoriteIds.contains(course.id) }
                    .map(courseMapper::map)
                val items = itemsMapper.map(favoriteCourses)

                if (items.isEmpty()) {
                    reduce { reducer.empty(it, listOf(FavoritesAdapterItem.EmptyItem(message = "Пока нет избранных курсов"))) }
                } else {
                    reduce { reducer.content(it, items) }
                }
            }
        }
    }

    private fun reduce(block: (FavoritesUiState) -> FavoritesUiState) {
        _uiState.value = block(_uiState.value)
    }
}
