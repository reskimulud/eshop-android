package com.mankart.eshop.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("name")
    val name: String,
)