package com.mankart.eshop.core.domain.model

data class CartItem(
    val id: String,
    val productId: String,
    val title: String,
    val price: Int,
    val description: String,
    val image: String,
    val quantity: Int,
)
