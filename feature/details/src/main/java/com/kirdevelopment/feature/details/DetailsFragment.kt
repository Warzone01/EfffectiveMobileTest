package com.kirdevelopment.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.details.databinding.FragmentDetailsBinding
import com.kirdevelopment.feature.details.presentation.details.DetailsScreenState
import com.kirdevelopment.feature.details.presentation.details.DetailsUiEffect
import com.kirdevelopment.feature.details.presentation.details.DetailsUiEvent
import com.kirdevelopment.feature.details.presentation.details.DetailsViewModel
import com.kirdevelopment.feature.details.presentation.details.model.DetailsCourseUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val viewModel: DetailsViewModel by viewModels()

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = requireNotNull(_binding)

    private val courseId: Long by lazy {
        requireArguments().getLong(ARG_COURSE_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeState()
        observeEffects()
        viewModel.onEvent(DetailsUiEvent.ScreenOpened(courseId))
    }

    private fun setupListeners() {
        binding.buttonBack.setOnClickListener { viewModel.onEvent(DetailsUiEvent.BackClicked) }
        binding.buttonRetry.setOnClickListener { viewModel.onEvent(DetailsUiEvent.RetryClicked) }
        binding.buttonToggleFavorite.setOnClickListener { viewModel.onEvent(DetailsUiEvent.FavoriteClicked) }
    }

    private fun observeState() {
        viewModel.uiState.onEach { state ->
            binding.progressDetails.visibility = if (state.screenState is DetailsScreenState.Loading) View.VISIBLE else View.GONE
            binding.layoutError.visibility = if (state.screenState is DetailsScreenState.Error) View.VISIBLE else View.GONE
            binding.layoutContent.visibility = if (state.screenState is DetailsScreenState.Content) View.VISIBLE else View.GONE
            binding.buttonToggleFavorite.isEnabled = !state.isRefreshing

            when (val screenState = state.screenState) {
                is DetailsScreenState.Content -> bindContent(screenState.course)
                is DetailsScreenState.Error -> binding.textError.text = screenState.message.asPlainText()
                DetailsScreenState.Loading -> Unit
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEffects() {
        viewModel.uiEffect.onEach { effect ->
            when (effect) {
                DetailsUiEffect.NavigateBack -> findNavController().navigateUp()
                is DetailsUiEffect.ShowMessage -> showToast(effect.message.asPlainText())
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun bindContent(course: DetailsCourseUiModel) {
        binding.textTitle.text = course.title
        binding.textDescription.text = course.description
        binding.textDate.text = course.startDate
        binding.textRate.text = course.rate
        binding.buttonToggleFavorite.isSelected = course.isFavorite
        binding.buttonToggleFavorite.contentDescription = if (course.isFavorite) {
            getString(R.string.details_remove_from_favorites)
        } else {
            getString(R.string.details_add_to_favorites)
        }
    }

    private fun UiText.asPlainText(): String {
        return when (this) {
            is UiText.Dynamic -> value
            is UiText.Resource -> getString(resId)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        private const val ARG_COURSE_ID = "courseId"
    }
}
