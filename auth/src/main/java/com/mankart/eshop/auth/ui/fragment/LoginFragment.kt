package com.mankart.eshop.auth.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mankart.eshop.auth.ui.AuthenticationViewModel
import com.mankart.eshop.auth.R
import com.mankart.eshop.auth.databinding.FragmentLoginBinding
import com.mankart.eshop.core.data.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginJob: Job = Job()
    private val authViewModel: AuthenticationViewModel by viewModels()

    // state flow
    private val emailState = MutableStateFlow("")
    private val passwordState = MutableStateFlow("")

    private val formIsValid = combine(emailState, passwordState) { email, password ->
        val emailIsValid = email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordIsValid = password.isNotEmpty() && password.length in 6..15

        when {
            !emailIsValid && email.isNotEmpty() -> binding.etEmail.error = getString(R.string.email_not_valid)
            !passwordIsValid && password.isNotEmpty() -> binding.etPassword.error = getString(R.string.password_not_valid)
        }

        emailIsValid && passwordIsValid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateUserInput()

        binding.tvSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            loginAction()
        }
    }

    private fun loginAction() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()

            loginJob = launch {
                authViewModel.login(email, password).collect {
                    when (it) {
                        is Resource.Loading -> setLoading(true)
                        is Resource.Message -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                            Timber.i(it.message.toString())
                        }
                        is Resource.Error -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), getString(R.string.wrong_email_password), Toast.LENGTH_SHORT).show()
                            Timber.e(it.message.toString())
                        }
                        is Resource.Success -> {
                            setLoading(false)

                            profileSetup()

                            Toast.makeText(requireActivity(), getString(R.string.login_success), Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireActivity(), Class.forName(
                                "com.mankart.eshop.ui.MainActivity"
                            ))
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
    }

    private fun profileSetup() {
        lifecycleScope.launch {
            authViewModel.getProfileData().collect {
                if (it is Resource.Success) {
                    authViewModel.apply {
                        it.data?.name?.let { name -> saveUserName(name) }
                        it.data?.email?.let { email -> saveEmail(email) }
                    }
                }
            }
        }
    }

    private fun validateUserInput() {
        binding.apply {
            etEmail.doOnTextChanged { text, _, _, _ ->
                emailState.value = text.toString()
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                passwordState.value = text.toString()
            }
        }

        lifecycleScope.launch {
            formIsValid.collect {
                binding.btnLogin.isEnabled = it
            }
        }
    }

    private fun setLoading(state: Boolean){
        binding.apply {
            etEmail.isEnabled = !state
            etPassword.isEnabled = !state
            btnLogin.isEnabled = !state

            if (state) {
                viewLoading.visibility = View.VISIBLE

            } else {
                viewLoading.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}