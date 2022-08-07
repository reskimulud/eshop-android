package com.mankart.eshop.core.domain.usecase.cart

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Cart
import kotlinx.coroutines.flow.Flow

interface CartUseCase {
    fun getCarts(): Flow<Resource<Cart>>
    fun addItemToCart(productId: String, quantity: Int): Flow<Resource<String>>
    fun updateItemInCart(itemId: String, quantity: Int): Flow<Resource<String>>
    fun deleteItemFromCart(itemId: String): Flow<Resource<String>>
}