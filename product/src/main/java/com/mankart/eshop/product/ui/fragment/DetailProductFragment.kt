package com.mankart.eshop.product.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.UserReview
import com.mankart.eshop.core.utils.Constants.CART_URI
import com.mankart.eshop.core.utils.Constants.EXTRA_PRODUCT_ID
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.product.R
import com.mankart.eshop.commonui.R as commonR
import com.mankart.eshop.product.databinding.FragmentDetailProductBinding
import com.mankart.eshop.product.ui.ProductViewModel
import com.mankart.eshop.product.ui.adapter.ListUserReviewAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailProductFragment: Fragment() {
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    private var productId: String? = ""
    private val productViewModel: ProductViewModel by viewModels()
    private var productJob: Job = Job()
    private var isFavorite = false

    private lateinit var productState: Product

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

        binding.btnAdToCart.setOnClickListener {
            addItemToCartHandler()
        }

        favoriteProductToggle()

        binding.ivCartFromDetail.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri(CART_URI.toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    private fun addItemToCartHandler() {
        lifecycleScope.launch {
            productId?.let { productViewModel.addItemToCart(it, 1).collect { resource ->
                if (resource is Resource.Success || resource is Resource.Message) {
                    Toast.makeText(requireContext(), getString(R.string.item_add_to_cart), Toast.LENGTH_SHORT).show()
                } else {
                    Timber.d("Error adding to cart ${resource.message}")
                }
            } }
        }
    }

    private fun isFavoriteSetup() {
        lifecycleScope.launch {
            productId?.let { productViewModel.isFavoriteProduct(it).collect { isFavorite ->
                this@DetailProductFragment.isFavorite = isFavorite
                if (isFavorite) {
                    binding.fabAddToFavorite.setImageResource(commonR.drawable.ic_favorite)
                } else {
                    binding.fabAddToFavorite.setImageResource(commonR.drawable.ic_favorite_border)
                }
            } }
        }
    }

    private fun favoriteProductToggle() {
        binding.fabAddToFavorite.setOnClickListener {
            isFavorite = if (isFavorite) {
                binding.fabAddToFavorite.setImageResource(commonR.drawable.ic_favorite_border)
                lifecycleScope.launch {
                    productViewModel.deleteFavoriteProductById(productState.id)
                    Timber.d("deleteFavoriteProductById: ${productState.id}")
                }
                false
            } else {
                binding.fabAddToFavorite.setImageResource(commonR.drawable.ic_favorite)
                lifecycleScope.launch {
                    productViewModel.addFavoriteProduct(productState)
                    Timber.d("addFavoriteProduct: ${productState.id}")
                }
                true
            }
        }
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
                            productResource.data?.apply {
                                tvTitleProduct.text = title
                                tvPriceProduct.text = price.formatIDR()
                                rbRatingProduct.rating = rating.toFloat()
                                tvCountRating.text = "(${countRate})"
                                tvDescriptionProduct.text = description

                                val productForState = Product(
                                    id,
                                    title,
                                    price,
                                    category,
                                    description,
                                    image,
                                    rating,
                                    countRate
                                )
                                this@DetailProductFragment.productState = productForState

                                if (reviews?.isNotEmpty() == true) {
                                    tvNoReview.visibility = View.GONE
                                    rvReview.visibility = View.VISIBLE
                                    val reviews = reviews
                                    if (reviews != null) {
                                        userReviewSetup(reviews)
                                    }
                                } else {
                                    tvNoReview.visibility = View.VISIBLE
                                    rvReview.visibility = View.GONE
                                }
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
        binding.rvReview.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}