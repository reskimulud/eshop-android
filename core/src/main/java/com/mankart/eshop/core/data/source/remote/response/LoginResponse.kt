package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("token")
    val token: String
)
