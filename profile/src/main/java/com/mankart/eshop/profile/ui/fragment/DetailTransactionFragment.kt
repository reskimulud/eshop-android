package com.mankart.eshop.profile.ui.fragment

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
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.utils.Constants.DETAIL_PRODUCT_URI
import com.mankart.eshop.core.utils.Constants.EXTRA_TRANSACTION_ID
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.core.utils.Helpers.getCurrentDate
import com.mankart.eshop.core.utils.Helpers.timestampToDate
import com.mankart.eshop.profile.R
import com.mankart.eshop.profile.databinding.FragmentDetailTransactionBinding
import com.mankart.eshop.profile.ui.ProfileViewModel
import com.mankart.eshop.profile.ui.adapter.ListOrderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailTransactionFragment: Fragment() {
    private var _binding: FragmentDetailTransactionBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var transactionId: String

    // UI State
    private val uiState = MutableStateFlow("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionId = arguments?.getString(EXTRA_TRANSACTION_ID) ?: ""
        binding.rvOrders.layoutManager = LinearLayoutManager(requireActivity())

        lifecycleScope.launch {
            uiState.collect {
                getOrders()
            }
        }

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getOrders() {
        lifecycleScope.launchWhenStarted {
            profileViewModel.getTransactionById(transactionId).collect {
                when (it) {
                    is Resource.Loading -> isShowProgressBar(true)
                    is Resource.Success -> {
                        isShowProgressBar(false)
                        binding.apply {
                            tvTransactionId.text = it.data?.id
                            tvTransactionDate.text = it.data?.dateCreated?.timestampToDate()
                            tvTotalPrice.text = it.data?.totalPrice?.formatIDR()
                        }
                        val adapter = it.data?.orders?.let { listOrder ->
                            ListOrderAdapter(
                                listOrder,
                                onItemClickCallback = { productId -> navigateToDetailProduct(productId) },
                                onBtnRateClickCallback = { productId, rating, review ->
                                    addRatingHandler(productId, rating, review)
                                }
                            )
                        }
                        binding.rvOrders.adapter = adapter
                    }
                    else -> Timber.e("Error: ${it.message}")
                }
            }
        }
    }

    private fun addRatingHandler(productId: String, rating: Int, review: String) {
        lifecycleScope.launch {
            profileViewModel.addProductReview(productId, rating, review).collect {
                if (it is Resource.Message) {
                    Toast.makeText(requireContext(), getString(R.string.success_add_review), Toast.LENGTH_SHORT).show()
                    uiState.value = getCurrentDate()
                } else {
                    Timber.e("Error adding review: $it")
                }
            }
        }
    }

    private fun navigateToDetailProduct(productId: String) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${DETAIL_PRODUCT_URI}/$productId".toUri())
            .build()
        findNavController().navigate(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvOrders.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isShowProgressBar(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}