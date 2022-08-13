package com.mankart.eshop.product.ui

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.ProductCategory
import com.mankart.eshop.core.domain.usecase.products.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
): ViewModel() {
    fun getProducts(categoryId: String? = null, search: String? = null): Flow<PagingData<Product>> =
        productUseCase.getProducts(categoryId, search)

    fun getProductById(productId: String): Flow<Resource<Product>> =
        productUseCase.getProductById(productId)

    fun getProductCategories(): Flow<Resource<List<ProductCategory>>> =
        productUseCase.getProductCategories()
}