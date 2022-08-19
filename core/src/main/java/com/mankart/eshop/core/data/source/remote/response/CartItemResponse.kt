package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartItemResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("productId")
    val productId: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("quantity")
    val quantity: Int,
)