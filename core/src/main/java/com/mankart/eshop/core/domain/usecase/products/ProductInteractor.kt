package com.mankart.eshop.core.domain.usecase.products

import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.ProductCategory
import com.mankart.eshop.core.domain.repository.IProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductInteractor @Inject constructor(private val repository: IProductRepository) : ProductUseCase {
    override fun getProducts(categoryId: String?, search: String?): Flow<PagingData<Product>> =
        repository.getProducts(categoryId, search)

    override fun getProductById(id: String): Flow<Resource<Product>> =
        repository.getProductById(id)

    override fun getProductCategories(): Flow<Resource<List<ProductCategory>>> =
        repository.getProductCategories()
}