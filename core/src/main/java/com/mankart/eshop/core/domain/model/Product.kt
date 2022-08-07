package com.mankart.eshop.core.domain.model


data class Product(
    val id: String,
    val title: String,
    val price: Int,
    val category: String,
    val description: String,
    val image: String,
    val rating: Double,
    val countRate: Int,
    val reviews: List<UserReview>? = emptyList()
)
