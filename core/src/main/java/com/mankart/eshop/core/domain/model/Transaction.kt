package com.mankart.eshop.core.domain.model


data class Transaction(
    val id: String,
    val dateCreated: Long,
    val totalItem: Int? = 0,
    val totalPrice: Int? = 0,
    val orders: List<Order>? = emptyList()
)
