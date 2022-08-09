package com.mankart.eshop.auth.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mankart.eshop.auth.AuthenticationViewModel
import com.mankart.eshop.auth.R
import com.mankart.eshop.auth.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var loginJob = Job()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}