package com.mankart.eshop.core.domain.repository

import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.ProductCategory
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    fun getProducts(categoryId: String? = null, search: String? = null): Flow<PagingData<Product>>
    fun getProductById(id: String): Flow<Resource<Product>>
    fun getProductCategories(): Flow<Resource<List<ProductCategory>>>
}