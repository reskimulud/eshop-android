package com.mankart.eshop.core.domain.model

data class UserReview(
    val user: String,
    val rate: Int,
    val review: String? = null
)
