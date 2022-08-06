package com.mankart.eshop.core.data.source.remote.response

data class ResponseWithData<T>(
    val status: String,
    val message: String,
    val data: T
)
