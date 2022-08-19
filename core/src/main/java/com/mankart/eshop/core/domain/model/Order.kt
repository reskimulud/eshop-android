package com.mankart.eshop.core.domain.model

data class Order(
    val id: String,
    val title: String,
    val price: Int,
    val quantity: Int,
    val image: String,
    val yourRating: Int,
    val yourReview: String? = "",
)
