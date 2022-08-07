package com.mankart.eshop.core.domain.usecase.products

import androidx.paging.PagingData
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductInteractor @Inject constructor(private val repository: IProductRepository) : ProductUseCase {
    override fun getProducts(): Flow<PagingData<Product>> =
        repository.getProducts()
}