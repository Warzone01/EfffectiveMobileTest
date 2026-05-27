package com.kirdevelopment.feature.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.text.toSpannable
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kirdevelopment.feature.auth.databinding.FragmentLoginBinding
import com.kirdevelopment.feature.auth.presentation.login.LoginUiEffect
import com.kirdevelopment.feature.auth.presentation.login.LoginUiEvent
import com.kirdevelopment.feature.auth.presentation.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

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
        setupObservers()
        setupClickListeners()
        setupInputListeners()
        setupHighlightedText()
    }

    private fun setupObservers() {
        viewModel.uiState.onEach { state ->
            binding.inputEmail.error = if (state.email.isError) state.email.errorMessage else null
            binding.inputPassword.error = if (state.password.isError) state.password.errorMessage else null

            binding.buttonLogin.isEnabled = state.isLoginButtonEnabled
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.uiEffect.onEach { effect ->
            when (effect) {
                is LoginUiEffect.NavigateToMain -> navigateToMain()
                is LoginUiEffect.ShowError -> showToast(effect.message)
                is LoginUiEffect.OpenBrowser -> openBrowser(effect.url)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupClickListeners() {
        binding.buttonLogin.setOnClickListener {
            viewModel.onEvent(LoginUiEvent.LoginClicked)
        }
        binding.buttonVk.setOnClickListener {
            viewModel.onEvent(LoginUiEvent.VkClicked)
        }
        binding.buttonOk.setOnClickListener {
            viewModel.onEvent(LoginUiEvent.OkClicked)
        }
    }

    private fun setupInputListeners() {
        binding.inputEmail.doAfterTextChanged { editable ->
            viewModel.onEvent(LoginUiEvent.EmailChanged(editable?.toString().orEmpty()))
        }
        binding.inputPassword.doAfterTextChanged { editable ->
            viewModel.onEvent(LoginUiEvent.PasswordChanged(editable?.toString().orEmpty()))
        }
    }

    private fun setupHighlightedText() {
        val fullText = getString(R.string.login_action_signup)
        val registrationText = getString(R.string.registration)
        val spannable = fullText.toSpannable()

        val startIndex = fullText.indexOf(registrationText)
        val endIndex = startIndex + registrationText.length

        if (startIndex != -1) {
            spannable.setSpan(
                ForegroundColorSpan(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.auth_primary_green
                    )
                ),
                startIndex,
                endIndex,
                0
            )
        }

        binding.textActionSignup.text = spannable
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}