package com.mankart.eshop.product.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mankart.eshop.core.utils.Constants
import com.mankart.eshop.product.databinding.FragmentDetailProductBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProductFragment: Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Kenapa tidak menggunakan safeArgs karena menggunakan deep link
         * untuk bernavigasi ke halaman detail produk.
         * Dan menurut dokumentasi https://developer.android.com/guide/navigation/navigation-multi-module ,
         * safeArgs hanya bisa digunakan pada fragment yang berada di module yang sama.
         * Alasan menggunakan deep link untuk navigasi supaya menjadi lebih general,
         * dan bisa di akses dari luar modul
         */
        val productId = arguments?.getString(Constants.EXTRA_ID)

        binding.tvIdProduct.text = productId
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}