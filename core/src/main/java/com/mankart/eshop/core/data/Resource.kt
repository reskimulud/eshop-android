package com.mankart.eshop.core.data

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Message<T>(message: String) : Resource<T>(message = message)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}