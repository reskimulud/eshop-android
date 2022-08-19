package com.mankart.eshop.core.domain.usecase.favproduct

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoriteProductUseCase {
    fun getFavoriteProducts(): Flow<Resource<List<Product>>>
    suspend fun addFavoriteProduct(product: Product)
    fun deleteFavoriteProductById(productId: String)
    fun isFavoriteProduct(productId: String): Flow<Boolean>
}