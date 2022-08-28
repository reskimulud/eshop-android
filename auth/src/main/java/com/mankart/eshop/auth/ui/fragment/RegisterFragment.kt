package com.mankart.eshop.auth.ui.fragment

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
import com.mankart.eshop.auth.databinding.FragmentRegisterBinding
import com.mankart.eshop.core.data.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthenticationViewModel by viewModels()
    private var registerJob: Job = Job()

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
            !emailIsValid && email.isNotEmpty() -> binding.etEmail.error = getString(R.string.email_not_valid)
            !passwordIsValid && password.isNotEmpty() -> binding.etPassword.error = getString(R.string.password_not_valid)
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
            registerAction()
        }
    }

    private fun registerAction() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        lifecycleScope.launchWhenResumed {
            if (registerJob.isActive) registerJob.cancel()

            registerJob = launch {
                authViewModel.register(name, email, password).collect {
                    when (it) {
                        is Resource.Loading -> setLoading(true)
                        is Resource.Message -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                            Timber.i(it.message.toString())
                            moveToLoginFragment()
                        }
                        is Resource.Error -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                            Timber.e(it.message.toString())
                        }
                        is Resource.Success -> {
                            setLoading(false)
                            Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_LONG).show()
                            Timber.i(it.message.toString())
                            moveToLoginFragment()
                        }
                    }
                }
            }
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

    private fun setLoading(state: Boolean){
        binding.apply {
            etName.isEnabled = !state
            etEmail.isEnabled = !state
            etPassword.isEnabled = !state
            btnRegister.isEnabled = !state

            if (state) {
                viewLoading.visibility = View.VISIBLE

            }else {
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