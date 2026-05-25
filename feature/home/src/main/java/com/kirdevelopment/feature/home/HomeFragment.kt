package com.kirdevelopment.feature.home

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
import com.kirdevelopment.feature.home.databinding.FragmentHomeBinding
import com.kirdevelopment.feature.home.presentation.home.HomeUiEffect
import com.kirdevelopment.feature.home.presentation.home.HomeUiEvent
import com.kirdevelopment.feature.home.presentation.home.HomeViewModel
import com.kirdevelopment.feature.home.presentation.home.adapter.HomeAdapter
import com.kirdevelopment.feature.home.presentation.home.delegate.HomeDelegatesRegistry
import com.kirdevelopment.feature.home.presentation.home.mapper.HomeStateToAdapterItemsMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var delegatesRegistry: HomeDelegatesRegistry

    @Inject
    lateinit var stateItemsMapper: HomeStateToAdapterItemsMapper

    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding)

    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter(
            delegatesRegistry = delegatesRegistry,
            onCourseClick = { courseId -> viewModel.onEvent(HomeUiEvent.CourseClicked(courseId)) },
            onFavoriteClick = { courseId -> viewModel.onEvent(HomeUiEvent.FavoriteClicked(courseId)) },
            onRetryClick = { viewModel.onEvent(HomeUiEvent.RetryClicked) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupListeners()
        observeState()
        observeEffects()
        viewModel.onEvent(HomeUiEvent.ScreenOpened)
    }

    private fun setupRecycler() {
        binding.recyclerHome.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHome.adapter = homeAdapter
    }

    private fun setupListeners() {
        binding.buttonSort.setOnClickListener {
            viewModel.onEvent(HomeUiEvent.SortClicked)
        }
    }

    private fun observeState() {
        viewModel.uiState.onEach { state ->
            val adapterItems = stateItemsMapper.map(state)
            homeAdapter.submitList(adapterItems)
            binding.buttonSort.text = if (state.isSortDescending) {
                getString(R.string.home_sort) + " ↓"
            } else {
                getString(R.string.home_sort) + " ↑"
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEffects() {
        viewModel.uiEffect.onEach { effect ->
            when (effect) {
                is HomeUiEffect.NavigateToCourseDetails -> openDetails(effect.courseId)
                is HomeUiEffect.ShowMessage -> showToast(effect.message.asPlainText())
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
        binding.recyclerHome.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
