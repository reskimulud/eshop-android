package com.mankart.eshop.core.domain.usecase.products

import androidx.paging.PagingData
import com.mankart.eshop.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductUseCase {
    fun getProducts(): Flow<PagingData<Product>>
}