package com.mankart.eshop.core.domain.repository

import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface IProductRepository {
    fun getProducts(): Flow<PagingData<Product>>
    fun getProductById(id: String): Flow<Resource<Product>>
}