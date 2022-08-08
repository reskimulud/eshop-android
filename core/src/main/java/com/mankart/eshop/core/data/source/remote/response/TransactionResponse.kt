package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("dateCreated")
    val dateCreated: Long,

    @field:SerializedName("totalItem")
    val totalItem: Int? = 0,

    @field:SerializedName("total")
    val totalPrice: Int? = 0,

    @field:SerializedName("orders")
    val orders: List<OrderResponse>? = emptyList()
)
