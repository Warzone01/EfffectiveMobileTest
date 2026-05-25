package com.kirdevelopment.feature.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.usecase.GetCoursesUseCase
import com.kirdevelopment.domain.usecase.ObserveFavoritesUseCase
import com.kirdevelopment.domain.usecase.SortCoursesByPublishDateUseCase
import com.kirdevelopment.domain.usecase.ToggleFavoriteUseCase
import com.kirdevelopment.feature.home.presentation.home.mapper.CoursesToAdapterItemsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val sortCoursesUseCase: SortCoursesByPublishDateUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val itemsMapper: CoursesToAdapterItemsMapper,
    private val reducer: HomeStateReducer
) : ViewModel() {

    private var cachedCourses: List<Course> = emptyList()
    private var favoriteIds: Set<Long> = emptySet()

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        observeFavorites()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.ScreenOpened -> loadCourses()
            HomeUiEvent.RetryClicked -> loadCourses()
            HomeUiEvent.SortClicked -> toggleSort()

            is HomeUiEvent.CourseClicked -> openCourse(event.courseId)
            is HomeUiEvent.FavoriteClicked -> toggleFavorite(event.courseId)
        }
    }

    private fun toggleSort() {
        _uiState.value = _uiState.value.copy(isSortDescending = !_uiState.value.isSortDescending)
        if (cachedCourses.isNotEmpty()) {
            publishCourses()
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            reduce { reducer.loading(it) }

            when (val result = getCoursesUseCase(NoParams)) {
                is AppResult.Success -> {
                    cachedCourses = sortCoursesUseCase.execute(result.data)
                    publishCourses()
                }

                is AppResult.Error -> {
                    reduce { reducer.error(it, mapError(result.error)) }
                }
            }
        }
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            observeFavoritesUseCase(NoParams).collectLatest { favorites ->
                favoriteIds = favorites.map { it.courseId }.toSet()
                if (cachedCourses.isNotEmpty()) {
                    publishCourses()
                }
            }
        }
    }

    private fun toggleFavorite(courseId: Long) {
        viewModelScope.launch {
            reduce { reducer.refreshing(it, enabled = true) }

            when (val result = toggleFavoriteUseCase(courseId)) {
                is AppResult.Success -> {
                    reduce { reducer.refreshing(it, enabled = false) }
                }

                is AppResult.Error -> {
                    reduce { reducer.refreshing(it, enabled = false) }
                    _uiEffect.send(HomeUiEffect.ShowMessage(mapError(result.error)))
                }
            }
        }
    }

    private fun openCourse(courseId: Long) {
        viewModelScope.launch {
            _uiEffect.send(HomeUiEffect.NavigateToCourseDetails(courseId))
        }
    }

    private fun reduce(block: (HomeUiState) -> HomeUiState) {
        _uiState.value = block(_uiState.value)
    }

    private fun publishCourses() {
        val sortedCourses = applySort(cachedCourses)
        val mergedCourses = mergeFavoriteState(sortedCourses, favoriteIds)
        val items = itemsMapper.map(mergedCourses)

        if (items.isEmpty()) {
            reduce { reducer.empty(it) }
        } else {
            reduce { reducer.content(it, items) }
        }
    }

    private fun mergeFavoriteState(courses: List<Course>, favorites: Set<Long>): List<Course> {
        return courses.map { course ->
            course.copy(hasLike = course.hasLike || favorites.contains(course.id))
        }
    }

    private fun applySort(courses: List<Course>): List<Course> {
        val sortedDesc = sortCoursesUseCase.execute(courses)
        return if (_uiState.value.isSortDescending) sortedDesc else sortedDesc.reversed()
    }

    private fun mapError(error: AppError): UiText {
        val message = when (error) {
            is AppError.Network -> error.message ?: "Ошибка сети"
            is AppError.Server -> error.message ?: "Ошибка сервера"
            is AppError.Validation -> error.message ?: "Ошибка данных"
            is AppError.Database -> error.message ?: "Ошибка базы данных"
            is AppError.Unknown -> error.throwable?.message ?: "Неизвестная ошибка"
        }
        return UiText.Dynamic(message)
    }
}
