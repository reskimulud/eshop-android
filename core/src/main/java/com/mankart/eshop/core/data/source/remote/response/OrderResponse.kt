package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("quantity")
    val quantity: Int,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("yourRate")
    val yourRating: Int,

    @field:SerializedName("yourReview")
    val yourReview: String? = "",
)
