package com.mankart.eshop.core.domain.model

data class Cart(
    val totalItem: Int,
    val subTotal: Int,
    val cart: List<CartItem>
)
