package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListProductResponse(
    @field:SerializedName("products")
    val data: List<ProductResponse>
)
