package com.mankart.eshop.product.ui

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.usecase.products.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
): ViewModel() {
    fun getProducts(): Flow<PagingData<Product>> = productUseCase.getProducts()
    fun getProductById(productId: String): Flow<Resource<Product>> = productUseCase.getProductById(productId)
}