package com.kirdevelopment.efffectivemobiletest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.kirdevelopment.efffectivemobiletest.databinding.FragmentMainContainerBinding

class MainContainerFragment : Fragment() {

    private var _binding: FragmentMainContainerBinding? = null
    private val binding: FragmentMainContainerBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Нижняя навигация привязана к вложенному графу: это масштабируемо для роста фич.
        val childNavHost = childFragmentManager.findFragmentById(R.id.main_tabs_nav_host) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(childNavHost.navController)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
