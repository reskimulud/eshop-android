package com.mankart.eshop.setting.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mankart.eshop.setting.BuildConfig
import com.mankart.eshop.setting.databinding.FragmentSettingBinding

class SettingFragment: Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvVersionApp.text = "v${BuildConfig.VERSION_NAME}"

        binding.setLanguage.setOnClickListener {
            showUnderDevelopmentToast()
        }
        binding.setTheme.setOnClickListener {
            showUnderDevelopmentToast()
        }
    }

    private fun showUnderDevelopmentToast() {
        Toast.makeText(requireActivity(), "Under Development", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}