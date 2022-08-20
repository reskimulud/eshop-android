package com.mankart.eshop.setting.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mankart.eshop.setting.BuildConfig
import com.mankart.eshop.setting.R
import com.mankart.eshop.setting.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment: Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvVersionApp.text = "v${BuildConfig.VERSION_NAME}"

        binding.setLanguage.setOnClickListener {
            showUnderDevelopmentToast()
        }
        binding.setTheme.setOnClickListener {
            showUnderDevelopmentToast()
        }

        binding.btnLogout.setOnClickListener {
            onLogoutHandler()
        }
    }

    private fun onLogoutHandler() {
        val authenticationActivityClassName = "com.mankart.eshop.auth.ui.AuthenticationHostActivity"

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.confirm_logout)
            .setPositiveButton(R.string.logout) { dialog, _ ->
                val intent = Intent(requireContext(), Class.forName(authenticationActivityClassName))

                settingViewModel.logout()
                dialog.dismiss()

                startActivity(intent)
                requireActivity().finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        alertDialog.show()
    }

    private fun showUnderDevelopmentToast() {
        Toast.makeText(requireActivity(), R.string.under_dev, Toast.LENGTH_SHORT).show()
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