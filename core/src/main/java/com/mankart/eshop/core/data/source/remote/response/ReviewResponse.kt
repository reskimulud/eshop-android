package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @field:SerializedName("user")
    val user: String,

    @field:SerializedName("rate")
    val rate: Int,

    @field:SerializedName("review")
    val review: String? = null
)