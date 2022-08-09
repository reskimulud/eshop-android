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
import com.mankart.eshop.auth.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthenticationViewModel by viewModels()
    private var registerJob = Job()

    // state flow
    private val nameState = MutableStateFlow("")
    private val emailState = MutableStateFlow("")
    private val passwordState = MutableStateFlow("")

    private val isFormValid = combine(nameState, emailState, passwordState) { name, email, password ->
        val nameIsValid = name.isNotEmpty()
        val emailIsValid = email.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
        val passwordIsValid = password.isNotEmpty() && password.length in 6..15

        when {
            !emailIsValid && email.isNotEmpty() -> binding.etEmail.error = "Email is not valid!"
            !passwordIsValid && password.isNotEmpty() -> binding.etPassword.error = "Password must have length between 6 - 15"
        }

        nameIsValid && emailIsValid && passwordIsValid
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        validateUserInput()

        binding.tvSignIn.setOnClickListener {
            moveToLoginFragment()
        }
        binding.btnRegister.setOnClickListener {
            moveToLoginFragment()
        }
    }

    private fun validateUserInput() {
        binding.apply {
            etName.doOnTextChanged { text, _, _, _ ->
                nameState.value = text.toString()
            }
            etEmail.doOnTextChanged { text, _, _, _ ->
                emailState.value = text.toString()
            }
            etPassword.doOnTextChanged { text, _, _, _ ->
                passwordState.value = text.toString()
            }
        }

        lifecycleScope.launch {
            isFormValid.collect {
                binding.btnRegister.isEnabled = it
            }
        }
    }

    private fun moveToLoginFragment() {
        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}