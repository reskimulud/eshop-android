package com.mankart.eshop.product.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.mankart.eshop.product.databinding.FragmentProductsBinding
import com.mankart.eshop.product.ui.ProductViewModel
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

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvProducts.layoutManager = GridLayoutManager(requireActivity(), 2)
        listProductAdapter = ListProductAdapter()

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