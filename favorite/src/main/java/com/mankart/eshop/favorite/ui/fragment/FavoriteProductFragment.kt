package com.mankart.eshop.favorite.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.favorite.databinding.FragmentFavoriteProductBinding
import com.mankart.eshop.favorite.di.DaggerFavoriteComponent
import com.mankart.eshop.core.di.dfm.FavoriteModuleDependencies
import com.mankart.eshop.core.utils.Constants.DETAIL_PRODUCT_URI
import com.mankart.eshop.core.utils.Helpers
import com.mankart.eshop.favorite.ui.FavoriteViewModel
import com.mankart.eshop.favorite.ui.ViewModelFactory
import com.mankart.eshop.favorite.ui.adapter.ListFavoriteProductAdapter
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Produk yang dsimpan di favorite (yang belum dihapus) akan tetap tersimpan di database
 * meskipun user telah logout. Untuk implementasi ke depannya saya akan menerapkan fitur
 * favorite product disimpan di server (API).
 */

class FavoriteProductFragment: Fragment() {
    private var _binding: FragmentFavoriteProductBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: ViewModelFactory
    private val favoriteViewModel: FavoriteViewModel by viewModels { factory }
    private val uiState = MutableStateFlow("")

    override fun onAttach(context: Context) {
        DaggerFavoriteComponent.builder()
            .context(context)
            .componentDependencies(
                EntryPointAccessors.fromApplication(
                    context.applicationContext,
                    FavoriteModuleDependencies::class.java
                )
            )
            .build()
            .inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            uiState.collect {
                getFavoriteProducts()
            }
        }
        rvTouchHelper()
    }

    private fun getFavoriteProducts() {
        binding.rvFavoriteProduct.layoutManager = LinearLayoutManager(requireActivity())
        lifecycleScope.launchWhenStarted {
            favoriteViewModel.getFavoriteProducts().collect { resource ->
                if (resource is Resource.Success) {
                    if (resource.data.isNullOrEmpty()) {
                        binding.rvFavoriteProduct.visibility = View.GONE
                        binding.tvNoFavorite.visibility = View.VISIBLE
                    } else {
                        val adapter = resource.data?.let { listFavoriteProduct ->
                            ListFavoriteProductAdapter(listFavoriteProduct,
                                onItemClickCallback = { navigateToDetailProduct(it) },
                                onFavBtnClickCallback = { deleteItemFromFavoriteProduct(it) }
                            )
                        }
                        binding.rvFavoriteProduct.adapter = adapter
                    }
                } else {
                    Timber.e("Error: ${resource.message}")
                }
            }
        }
    }

    private fun deleteItemFromFavoriteProduct(productId: String) {
        lifecycleScope.launchWhenStarted {
            favoriteViewModel.deleteFavoriteProductById(productId)
        }
        uiState.value = Helpers.getCurrentDate()
    }

    private fun navigateToDetailProduct(productId: String) {
        val request = NavDeepLinkRequest.Builder
            .fromUri("${DETAIL_PRODUCT_URI}/$productId".toUri())
            .build()
        findNavController().navigate(request)
    }

    private fun rvTouchHelper() {
        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.Callback(){
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val productId = (viewHolder as ListFavoriteProductAdapter.ViewHolder).productId
                deleteItemFromFavoriteProduct(productId)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvFavoriteProduct)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvFavoriteProduct.adapter = null
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}