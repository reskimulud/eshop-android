package com.mankart.eshop.core.domain.usecase.favproduct

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.repository.IFavoriteProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteProductInteractor @Inject constructor(private val repository: IFavoriteProductRepository): FavoriteProductUseCase {
    override fun getFavoriteProducts(): Flow<Resource<List<Product>>> =
        repository.getFavoriteProducts()

    override suspend fun addFavoriteProduct(product: Product) =
        repository.addFavoriteProduct(product)

    override fun deleteFavoriteProductById(productId: String) =
        repository.deleteFavoriteProductById(productId)

    override fun isFavoriteProduct(productId: String): Flow<Boolean> =
        repository.isFavoriteProduct(productId)
}