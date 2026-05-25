package com.kirdevelopment.feature.favorites.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.core.common.usecase.NoParams
import com.kirdevelopment.core.model.course.Course
import com.kirdevelopment.domain.usecase.GetCoursesUseCase
import com.kirdevelopment.domain.usecase.ObserveFavoritesUseCase
import com.kirdevelopment.domain.usecase.ToggleFavoriteUseCase
import com.kirdevelopment.feature.favorites.presentation.favorites.mapper.CourseToFavoriteCourseUiMapper
import com.kirdevelopment.feature.favorites.presentation.favorites.mapper.FavoriteCoursesToAdapterItemsMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val courseMapper: CourseToFavoriteCourseUiMapper,
    private val itemsMapper: FavoriteCoursesToAdapterItemsMapper,
    private val reducer: FavoritesStateReducer
) : ViewModel() {

    private var cachedCourses: List<Course> = emptyList()
    private var observeJob: Job? = null

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<FavoritesUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: FavoritesUiEvent) {
        when (event) {
            FavoritesUiEvent.ScreenOpened -> loadFavoritesData()
            FavoritesUiEvent.RetryClicked -> loadFavoritesData()
            is FavoritesUiEvent.RemoveClicked -> removeFromFavorites(event.courseId)
            is FavoritesUiEvent.CourseClicked -> openCourse(event.courseId)
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
                    reduce { reducer.error(it, mapError(result.error)) }
                }
            }
        }
    }

    private fun observeFavorites() {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeFavoritesUseCase(NoParams)
                .catch { error ->
                    reduce { reducer.error(it, mapError(AppError.Unknown(error))) }
                }
                .collectLatest { favorites ->
                    val favoriteIds = favorites.map { it.courseId }.toSet()
                    val favoriteCourses = cachedCourses
                        .filter { course -> favoriteIds.contains(course.id) }
                        .map(courseMapper::map)
                    val items = itemsMapper.map(favoriteCourses)

                    if (items.isEmpty()) {
                        reduce { reducer.empty(it, items) }
                    } else {
                        reduce { reducer.content(it, items) }
                    }
                }
        }
    }

    private fun removeFromFavorites(courseId: Long) {
        viewModelScope.launch {
            reduce { reducer.refreshing(it, enabled = true) }

            when (val result = toggleFavoriteUseCase(courseId)) {
                is AppResult.Success -> {
                    reduce { reducer.refreshing(it, enabled = false) }
                }

                is AppResult.Error -> {
                    reduce { reducer.refreshing(it, enabled = false) }
                    _uiEffect.send(FavoritesUiEffect.ShowMessage(mapError(result.error)))
                }
            }
        }
    }

    private fun openCourse(courseId: Long) {
        viewModelScope.launch {
            _uiEffect.send(FavoritesUiEffect.NavigateToCourseDetails(courseId))
        }
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

    private fun reduce(block: (FavoritesUiState) -> FavoritesUiState) {
        _uiState.value = block(_uiState.value)
    }
}
