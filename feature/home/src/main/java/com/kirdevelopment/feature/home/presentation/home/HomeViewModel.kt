package com.kirdevelopment.feature.home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirdevelopment.core.common.ui.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reducer: HomeStateReducer
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEffect = Channel<HomeUiEffect>(Channel.BUFFERED)
    val uiEffect = _uiEffect.receiveAsFlow()

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.ScreenOpened -> onScreenOpened()
            HomeUiEvent.RetryClicked -> onRetryClicked()

            is HomeUiEvent.CourseClicked -> openCourse(event.courseId)
            is HomeUiEvent.FavoriteClicked -> onFavoriteClicked(event.courseId)
        }
    }

    private fun onScreenOpened() {
        reduce { reducer.loading(it) }
        reduce { reducer.empty(it) }
    }

    private fun onRetryClicked() {
        reduce { reducer.loading(it) }
        reduce { reducer.error(it, UiText.Dynamic("Не удалось загрузить данные")) }
    }

    private fun onFavoriteClicked(courseId: Long) {
        viewModelScope.launch {
            _uiEffect.send(HomeUiEffect.ShowMessage(UiText.Dynamic("Избранное: $courseId")))
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
}
