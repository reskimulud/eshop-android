package com.mankart.eshop.product.ui

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.ProductCategory
import com.mankart.eshop.core.domain.usecase.cart.CartUseCase
import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductUseCase
import com.mankart.eshop.core.domain.usecase.products.ProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val cartUseCase: CartUseCase,
    private val favoriteProductUseCase: FavoriteProductUseCase,
): ViewModel() {
    fun getProducts(categoryId: String? = null, search: String? = null): Flow<PagingData<Product>> =
        productUseCase.getProducts(categoryId, search)

    fun getProductById(productId: String): Flow<Resource<Product>> =
        productUseCase.getProductById(productId)

    fun getProductCategories(): Flow<Resource<List<ProductCategory>>> =
        productUseCase.getProductCategories()

    fun addItemToCart(productId: String, quantity: Int) =
        cartUseCase.addItemToCart(productId, quantity)

    fun isFavoriteProduct(productId: String) =
        favoriteProductUseCase.isFavoriteProduct(productId)

    suspend fun addFavoriteProduct(product: Product) =
        favoriteProductUseCase.addFavoriteProduct(product)

    fun deleteFavoriteProductById(productId: String) =
        favoriteProductUseCase.deleteFavoriteProductById(productId)
}