package com.mankart.eshop.core.data.source.remote.network

sealed class ApiResponse<out R> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val errorMessage: String) : ApiResponse<Nothing>()
    data class Message<out T>(val message: String) : ApiResponse<T>()
    object Empty : ApiResponse<Nothing>()
}