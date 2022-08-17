package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("rating")
    val rating: Rating? = null,

    @field:SerializedName("reviews")
    val reviews: List<ReviewResponse>? = emptyList()
)

data class Rating(
    @field:SerializedName("rate")
    val rating: Double,

    @field:SerializedName("count")
    val countRate: Int
)
