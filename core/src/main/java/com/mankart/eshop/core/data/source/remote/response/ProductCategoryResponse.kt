package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductCategoryResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("name")
    val name: String,
)
