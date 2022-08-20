package com.mankart.eshop.cart.ui.fragment

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mankart.eshop.cart.R
import com.mankart.eshop.cart.databinding.FragmentCartBinding
import com.mankart.eshop.cart.ui.CartViewModel
import com.mankart.eshop.cart.ui.adapter.ListCartAdapter
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.utils.Constants.DETAIL_PRODUCT_URI
import com.mankart.eshop.core.utils.Constants.PRODUCT_URI
import com.mankart.eshop.core.utils.Helpers.formatIDR
import com.mankart.eshop.core.utils.Helpers.getCurrentDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment: Fragment() {
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by viewModels()

    // UI States
    private val uiState = MutableStateFlow("")
    private val totalPriceState = MutableStateFlow(0)
    private val itemIdState = MutableStateFlow("")
    private val qtyState = MutableStateFlow(0)

    private val itemUpdatedState = combine(itemIdState, qtyState) { itemIdState, qtyState ->
        Pair(itemIdState, qtyState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCarts.layoutManager = LinearLayoutManager(requireActivity())
        totalPriceSetup()

        lifecycleScope.launch {
            uiState.collect {
                getCart()
            }
        }
        rvTouchHelper()
        onCartItemUpdatedHandler()

        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnCheckout.setOnClickListener {
            onCheckoutHandler()
        }
    }

    private fun onCheckoutHandler() {
        lifecycleScope.launch {
            cartViewModel.checkout().collect {
                if (it is Resource.Message) {
                    Toast.makeText(requireActivity(), getString(R.string.transaction_success), Toast.LENGTH_LONG).show()

                    val request = NavDeepLinkRequest.Builder
                        .fromUri(PRODUCT_URI.toUri())
                        .build()
                    findNavController().navigate(request)
                }
            }
        }
    }

    private fun onCartItemUpdatedHandler() {
        lifecycleScope.launch {
            itemUpdatedState.collect {
                val itemId: String = it.first
                val qty: Int = it.second

                if (itemId.isNotEmpty()) {
                    if (qty > 0) {
                        updateCartItem(itemId, qty)
                        itemIdState.value = ""
                    } else {
                        deleteCartItem(itemId)
                    }
                }
            }
        }
    }

    private fun totalPriceSetup() {
        lifecycleScope.launch {
            totalPriceState.collect {
                binding.tvTotalPrice.text = it.formatIDR()
            }
        }
    }

    private fun updateCartItem(itemId: String, qty: Int) {
        lifecycleScope.launch {
            cartViewModel.updateCartItem(itemId, qty).collect {
                if (it is Resource.Message) {
                    uiState.value = getCurrentDate()
                    Toast.makeText(requireActivity(), getString(R.string.item_updated), Toast.LENGTH_SHORT).show()
                } else {
                    Timber.e(it.message.toString())
                }
            }
        }
    }

    private fun deleteCartItem(itemId: String) {
        lifecycleScope.launch {
            cartViewModel.deleteCartItem(itemId).collect {
                if (it is Resource.Message) {
                    uiState.value = getCurrentDate()
                    Toast.makeText(requireActivity(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show()
                } else {
                    Timber.e(it.message.toString())
                }
            }
        }
    }

    private fun getCart() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.getCarts().collect { resource ->
                when (resource) {
                    is Resource.Loading -> isShowProgressBar(true)
                    is Resource.Success -> {
                        isShowProgressBar(false)
                        totalPriceState.value = resource.data?.subTotal?:0
                        val listCart = resource.data?.cart

                        if (!listCart.isNullOrEmpty()) {
                            val adapter = ListCartAdapter(
                                listCart,
                                onItemClickCallback = { productId -> onItemClickHandler(productId) },
                                onBtnIncreaseClickCallback = { itemId, qty -> onIncreaseQtyHandler(itemId, qty) },
                                onBtnDecreaseClickCallback = { itemId, qty -> onDecreaseQtyHandler(itemId, qty) }
                            )
                            binding.rvCarts.adapter = adapter
                        } else {
                            binding.rvCarts.visibility = View.GONE
                            binding.tvEmptyCart.visibility = View.VISIBLE
                            binding.btnCheckout.isEnabled = false
                        }
                    }
                    else -> Timber.e(resource.message.toString())
                }
            }
        }
    }

    private fun onDecreaseQtyHandler(itemId: String, qty: Int) {
        qtyState.value = qty
        itemIdState.value = itemId
    }

    private fun onIncreaseQtyHandler(itemId: String, qty: Int) {
        qtyState.value = qty
        itemIdState.value = itemId
    }

    private fun onItemClickHandler(productId: String) {
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
                val itemId = (viewHolder as ListCartAdapter.ViewHolder).itemId
                deleteCartItem(itemId)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvCarts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvCarts.adapter = null
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