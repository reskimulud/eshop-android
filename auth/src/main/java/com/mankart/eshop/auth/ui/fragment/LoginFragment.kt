package com.mankart.eshop.auth.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.mankart.eshop.auth.utils.Helpers.isVisible
import com.mankart.eshop.core.data.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

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
            !emailIsValid && email.isNotEmpty() -> binding.etEmail.error = "Email is not valid!"
            !passwordIsValid && password.isNotEmpty() -> binding.etPassword.error = "Password must have length between 6 - 15"
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
                            Log.e("LoginFragment", it.message.toString())
                        }
                        is Resource.Error -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), "Login Failed, Wrong Email or Password", Toast.LENGTH_SHORT).show()
                            Log.e("LoginFragment", it.message.toString())
                        }
                        is Resource.Success -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
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
                viewLoading.isVisible(true)

            }else {
                viewLoading.isVisible(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}