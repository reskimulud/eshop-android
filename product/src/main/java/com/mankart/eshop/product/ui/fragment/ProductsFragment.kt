package com.mankart.eshop.product.ui.fragment

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.ProductCategory
import com.mankart.eshop.core.utils.Constants.CART_URI
import com.mankart.eshop.core.utils.Constants.DETAIL_PRODUCT_URI
import com.mankart.eshop.product.databinding.FragmentProductsBinding
import com.mankart.eshop.product.ui.ProductViewModel
import com.mankart.eshop.product.ui.adapter.ListCategoryAdapter
import com.mankart.eshop.product.ui.adapter.ListProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProductsFragment: Fragment() {
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var listProductAdapter: ListProductAdapter

    private var productJob: Job = Job()

    private var hideFabToTop = true

    // state for search and category id
    private val categoryIdState = MutableStateFlow("")
    private val searchState = MutableStateFlow("")

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
        initRecyclerView()
        stateFlowCollector()
        setupFabToTop()
        searchSetup()

        binding.ibCart.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri(CART_URI.toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    private fun searchSetup() {
        binding.svSearchProduct.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                searchState.value = query ?: ""
                return true
            }
        })
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

                if (!hideFabToTop && scrollY < oldScrollY) {
                    hideFabToTop = true
                    ObjectAnimator.ofFloat(fabToTop, "translationY", 0f, height).apply {
                        duration = 200
                        start()
                    }
                }

                if (hideFabToTop && scrollY > oldScrollY) {
                    hideFabToTop = false
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
        lifecycleScope.launch {
            productViewModel.getProductCategories().collect {
                when (it) {
                    is Resource.Loading -> Timber.i("Loading")
                    is Resource.Success -> {
                        val adapter = it.data?.let { listCategory ->
                            val allCategory = ProductCategory("all", "All")
                            val newListCategory: MutableList<ProductCategory> = mutableListOf(allCategory)
                            newListCategory.addAll(listCategory)

                            ListCategoryAdapter(newListCategory as List<ProductCategory>) { categoryId, categoryName ->
                                lifecycleScope.launch {
                                    categoryIdState.value = categoryId
                                }
                                Timber.d("onClick - categoryId: $categoryId")
                                binding.tvProducts.text = categoryName
                            }
                        }
                        binding.rvCategories.adapter = adapter
                    }
                    else -> Timber.e(it.toString())
                }
            }
        }
    }

    private fun stateFlowCollector() {
        Timber.d("stateFlowCollector")
        val stateCombine = combine(categoryIdState, searchState) { categoryId, search ->
            Timber.d("stateFlowCollector - categoryId: $categoryId - search: $search")
            Pair(categoryId, search)
        }

        lifecycleScope.launch {
            stateCombine.collect {
                val categoryId = it.first
                val search = it.second

                isShowProgressBar(true)
                if (categoryId.isNotEmpty() || search.isNotEmpty()) {
                    Timber.d("collect - categoryId: $categoryId")
                    if (categoryId.isNotEmpty() || categoryId != "all") {
                        getProducts(categoryId, search)
                    } else {
                        getProducts(search = search)
                    }
                }
            }
        }
    }

    private fun getProducts(categoryId: String? = null, search: String? = null) {
        lifecycleScope.launchWhenStarted {
            if (productJob.isActive) productJob.cancel()

            productJob = launch {
                productViewModel.getProducts(categoryId, search).collect {
                    Timber.d("Products: $it")
                    listProductAdapter.submitData(it)
                    isShowProgressBar(false)
                }
            }
        }
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

        isShowProgressBar(true)
        getProducts()

        binding.rvProducts.adapter = listProductAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvProducts.adapter = null
        binding.rvCategories.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        productJob.cancel()
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