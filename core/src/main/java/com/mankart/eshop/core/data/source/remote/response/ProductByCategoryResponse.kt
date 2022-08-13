package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductByCategoryResponse(
    @field:SerializedName("categoryName")
    val categoryName: String,

    @field:SerializedName("products")
    val products: List<ProductResponse>
)
