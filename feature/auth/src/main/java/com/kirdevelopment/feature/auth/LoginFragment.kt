package com.kirdevelopment.feature.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kirdevelopment.feature.auth.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            // TODO: валидация + вызов use case входа
            navigateToMain()
        }
    }

    /**
     * Навигация к главному экрану с очисткой back stack.
     * ID destinations получены через getIdentifier, т.к. LoginFragment
     * находится в модуле feature:auth, а nav_graph_root — в app.
     */
    private fun navigateToMain() {
        val navController = findNavController()
        val context = requireContext()
        val packageName = context.packageName
        val res = context.resources

        val mainContainerId = res.getIdentifier("mainContainerFragment", "id", packageName)
        val rootGraphId = res.getIdentifier("nav_graph_root", "id", packageName)

        if (mainContainerId == 0 || rootGraphId == 0) return

        val options = NavOptions.Builder()
            .setPopUpTo(rootGraphId, inclusive = true)
            .build()

        navController.navigate(mainContainerId, null, options)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
