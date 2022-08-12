package com.mankart.eshop.product.ui.fragment

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mankart.eshop.core.utils.Constants.DETAIL_PRODUCT_URI
import com.mankart.eshop.product.databinding.FragmentProductsBinding
import com.mankart.eshop.product.ui.ProductViewModel
import com.mankart.eshop.product.ui.adapter.ListCategoryAdapter
import com.mankart.eshop.product.ui.adapter.ListProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductsFragment: Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var listProductAdapter: ListProductAdapter

    private var productJob: Job = Job()

    private var hideNavView = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategory()
        setupFabToTop()
        initRecyclerView()
    }

    private fun setupFabToTop() {
        val fabToTop = binding.fabToTop

        fabToTop.visibility = View.GONE

        fabToTop.setOnClickListener {
            binding.nestedScrollView.fullScroll(View.FOCUS_UP)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.nestedScrollView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                fabToTop.visibility = View.VISIBLE
                val height = (fabToTop.height + 48).toFloat()

                if (!hideNavView && scrollY < oldScrollY) {
                    hideNavView = true
                    ObjectAnimator.ofFloat(fabToTop, "translationY", 0f, height).apply {
                        duration = 200
                        start()
                    }
                }

                if (hideNavView && scrollY > oldScrollY) {
                    hideNavView = false
                    ObjectAnimator.ofFloat(fabToTop, "translationY", height, 0f).apply {
                        duration = 200
                        start()
                    }
                }
            }
        }
    }

    private fun setupCategory() {
        binding.rvCategories.layoutManager = LinearLayoutManager(
            requireActivity(), LinearLayoutManager.HORIZONTAL, false
        )
        val categories = listOf("All", "Electronics", "Clothing", "Books", "Sports", "Movies", "Games")
        val adapter = ListCategoryAdapter(categories)
        binding.rvCategories.adapter = adapter
    }

    private fun initRecyclerView() {
        binding.rvProducts.layoutManager = GridLayoutManager(requireActivity(), 2)
        listProductAdapter = ListProductAdapter { productId ->
            // on item click
            val request = NavDeepLinkRequest.Builder
                .fromUri("$DETAIL_PRODUCT_URI/$productId".toUri())
                .build()
            findNavController().navigate(request)
        }

        lifecycleScope.launchWhenResumed {
            if (productJob.isActive) productJob.cancel()

            productJob = launch {
                productViewModel.getProducts().collect {
                    Log.e("ProductsFragment", "Products: $it")
                    listProductAdapter.submitData(it)
                }
            }
        }

        binding.rvProducts.adapter = listProductAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        listProductAdapter.submitData(lifecycle, PagingData.empty())
    }
}