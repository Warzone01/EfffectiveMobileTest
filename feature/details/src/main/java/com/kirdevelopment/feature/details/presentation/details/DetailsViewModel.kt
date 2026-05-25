package com.kirdevelopment.feature.details.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.error.AppError
import com.kirdevelopment.core.common.result.AppResult
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.domain.usecase.ObserveCourseByIdUseCase
import com.kirdevelopment.domain.usecase.ToggleFavoriteUseCase
import com.kirdevelopment.feature.details.presentation.details.mapper.CourseToDetailsCourseUiMapper
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
class DetailsViewModel @Inject constructor(
    private val observeCourseByIdUseCase: ObserveCourseByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val mapper: CourseToDetailsCourseUiMapper,
    private val reducer: DetailsStateReducer
) : ViewModel() {

    private var courseId: Long? = null
    private var observeJob: Job? = null

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<DetailsUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: DetailsUiEvent) {
        when (event) {
            is DetailsUiEvent.ScreenOpened -> openScreen(event.courseId)
            DetailsUiEvent.RetryClicked -> retry()
            DetailsUiEvent.FavoriteClicked -> toggleFavorite()
            DetailsUiEvent.BackClicked -> navigateBack()
        }
    }

    private fun openScreen(inputCourseId: Long) {
        courseId = inputCourseId
        reduce { reducer.loading(it) }
        observeCourse(inputCourseId)
    }

    private fun retry() {
        val currentId = courseId ?: return
        openScreen(currentId)
    }

    private fun observeCourse(inputCourseId: Long) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            observeCourseByIdUseCase(inputCourseId)
                .catch { error ->
                    reduce { reducer.error(it, mapError(AppError.Unknown(error))) }
                }
                .collectLatest { course ->
                    if (course == null) {
                        reduce { reducer.error(it, UiText.Dynamic("Курс не найден в локальном хранилище")) }
                    } else {
                        reduce { reducer.content(it, mapper.map(course)) }
                    }
                }
        }
    }

    private fun toggleFavorite() {
        val currentCourseId = courseId ?: return
        viewModelScope.launch {
            reduce { reducer.refreshing(it, enabled = true) }
            when (val result = toggleFavoriteUseCase(currentCourseId)) {
                is AppResult.Success -> reduce { reducer.refreshing(it, enabled = false) }
                is AppResult.Error -> {
                    reduce { reducer.refreshing(it, enabled = false) }
                    _uiEffect.send(DetailsUiEffect.ShowMessage(mapError(result.error)))
                }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _uiEffect.send(DetailsUiEffect.NavigateBack)
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

    private fun reduce(block: (DetailsUiState) -> DetailsUiState) {
        _uiState.value = block(_uiState.value)
    }
}
