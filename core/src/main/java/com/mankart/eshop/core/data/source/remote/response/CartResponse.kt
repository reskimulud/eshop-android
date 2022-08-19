package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartResponse(
    @field:SerializedName("totalItem")
    val totalItem: Int,

    @field:SerializedName("subTotal")
    val subTotal: Int,

    @field:SerializedName("cart")
    val cart: List<CartItemResponse>
)

