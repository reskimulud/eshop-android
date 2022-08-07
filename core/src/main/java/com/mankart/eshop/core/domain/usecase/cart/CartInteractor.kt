package com.mankart.eshop.core.domain.usecase.cart

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Cart
import com.mankart.eshop.core.domain.repository.ICartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartInteractor @Inject constructor(private val repository: ICartRepository) : CartUseCase {
    override fun getCarts(): Flow<Resource<Cart>> = repository.getCarts()

    override fun addItemToCart(productId: String, quantity: Int): Flow<Resource<String>> =
        repository.addItemToCart(productId, quantity)

    override fun updateItemInCart(itemId: String, quantity: Int): Flow<Resource<String>> =
        repository.updateItemInCart(itemId, quantity)

    override fun deleteItemFromCart(itemId: String): Flow<Resource<String>> =
        repository.deleteItemFromCart(itemId)
}