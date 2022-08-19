package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseWithData<T>(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("data")
    val data: T
)
