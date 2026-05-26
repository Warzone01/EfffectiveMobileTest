package com.kirdevelopment.feature.favorites

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.kirdevelopment.core.common.ui.UiText
import com.kirdevelopment.feature.favorites.databinding.FragmentFavoritesBinding
import com.kirdevelopment.feature.favorites.presentation.favorites.ui.FavoritesItemSpacingDecoration
import com.kirdevelopment.feature.favorites.presentation.favorites.FavoritesUiEffect
import com.kirdevelopment.feature.favorites.presentation.favorites.FavoritesUiEvent
import com.kirdevelopment.feature.favorites.presentation.favorites.FavoritesViewModel
import com.kirdevelopment.feature.favorites.presentation.favorites.adapter.FavoritesAdapter
import com.kirdevelopment.feature.favorites.presentation.favorites.delegate.FavoritesDelegatesRegistry
import com.kirdevelopment.feature.favorites.presentation.favorites.mapper.FavoritesStateToAdapterItemsMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    @Inject
    lateinit var delegatesRegistry: FavoritesDelegatesRegistry

    @Inject
    lateinit var stateItemsMapper: FavoritesStateToAdapterItemsMapper

    private val viewModel: FavoritesViewModel by viewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = requireNotNull(_binding)

    private val favoritesAdapter: FavoritesAdapter by lazy {
        FavoritesAdapter(
            delegatesRegistry = delegatesRegistry,
            onCourseClick = { courseId -> viewModel.onEvent(FavoritesUiEvent.CourseClicked(courseId)) },
            onRemoveClick = { courseId -> viewModel.onEvent(FavoritesUiEvent.RemoveClicked(courseId)) },
            onRetryClick = { viewModel.onEvent(FavoritesUiEvent.RetryClicked) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observeState()
        observeEffects()
        viewModel.onEvent(FavoritesUiEvent.ScreenOpened)
    }

    private fun setupRecycler() {
        binding.recyclerFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavorites.setHasFixedSize(true)
        binding.recyclerFavorites.itemAnimator = null
        binding.recyclerFavorites.adapter = favoritesAdapter
        if (binding.recyclerFavorites.itemDecorationCount == 0) {
            val spacing = resources.getDimensionPixelSize(R.dimen.favorites_item_spacing)
            binding.recyclerFavorites.addItemDecoration(FavoritesItemSpacingDecoration(spacing))
        }
    }

    private fun observeState() {
        viewModel.uiState.onEach { state ->
            val adapterItems = stateItemsMapper.map(state)
            favoritesAdapter.submitList(adapterItems)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEffects() {
        viewModel.uiEffect.onEach { effect ->
            when (effect) {
                is FavoritesUiEffect.NavigateToCourseDetails -> openDetails(effect.courseId)
                is FavoritesUiEffect.ShowMessage -> showToast(effect.message.asPlainText())
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun openDetails(courseId: Long) {
        findNavController().navigate(Uri.parse("emtest://details/$courseId"))
    }

    private fun UiText.asPlainText(): String {
        return when (this) {
            is UiText.Dynamic -> value
            is UiText.Resource -> "Сообщение"
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        binding.recyclerFavorites.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
