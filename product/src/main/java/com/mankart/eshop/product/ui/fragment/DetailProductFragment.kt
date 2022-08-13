package com.mankart.eshop.product.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.UserReview
import com.mankart.eshop.core.utils.Constants.EXTRA_PRODUCT_ID
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.product.R
import com.mankart.eshop.product.databinding.FragmentDetailProductBinding
import com.mankart.eshop.product.ui.ProductViewModel
import com.mankart.eshop.product.ui.adapter.ListUserReviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment: Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private var productId: String? = ""
    private val productViewModel: ProductViewModel by viewModels()
    private var productJob: Job = Job()
    private var isFavorite: Boolean = false

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
        productId = arguments?.getString(EXTRA_PRODUCT_ID)

        getProductById()
        isFavoriteSetup()

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.fabAddToFavorite.setOnClickListener {
            isFavorite = if (isFavorite) {
                binding.fabAddToFavorite.setImageResource(R.drawable.ic_favorite_border)
                false
            } else {
                binding.fabAddToFavorite.setImageResource(R.drawable.ic_favorite)
                true
            }
        }
        binding.btnAdToCart.setOnClickListener {
            addItemToCartHandler()
        }

        binding.ivCartFromDetail
    }

    private fun addItemToCartHandler() {
        lifecycleScope.launch {
            productId?.let { productViewModel.addItemToCart(it, 1).collect { resource ->
                if (resource is Resource.Success || resource is Resource.Message) {
                    Toast.makeText(requireContext(), "Item has added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("DetailProductFragment", "Error adding to cart ${resource.message}")
                }
            } }
        }
    }

    private fun isFavoriteSetup() {
        isFavorite = false
    }

    @SuppressLint("SetTextI18n")
    private fun getProductById() {
        lifecycleScope.launchWhenResumed {
            if (productJob.isActive) productJob.cancel()

            productJob = launch {
                productId?.let { productViewModel.getProductById(it).collect { productResource ->
                    if (productResource is Resource.Success) {
                        Glide.with(requireActivity())
                            .load(productResource.data?.image)
                            .into(binding.ivImageProduct)
                        binding.apply {
                            tvTitleProduct.text = productResource.data?.title
                            tvPriceProduct.text = productResource.data?.price?.formatIDR()
                            rbRatingProduct.rating = productResource.data?.rating?.toFloat() ?: 0f
                            tvCountRating.text = "(${productResource.data?.countRate})"
                            tvDescriptionProduct.text = productResource.data?.description

                            if (productResource.data?.reviews?.isNotEmpty() == true) {
                                tvNoReview.visibility = View.GONE
                                rvReview.visibility = View.VISIBLE
                                val reviews = productResource.data?.reviews
                                if (reviews != null) {
                                    userReviewSetup(reviews)
                                }
                            } else {
                                tvNoReview.visibility = View.VISIBLE
                                rvReview.visibility = View.GONE
                            }
                        }
                    }
                } }
            }
        }
    }

    private fun userReviewSetup(reviews: List<UserReview>) {
        binding.rvReview.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = ListUserReviewAdapter(reviews)
        binding.rvReview.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}