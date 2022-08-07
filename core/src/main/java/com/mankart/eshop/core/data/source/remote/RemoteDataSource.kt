package com.mankart.eshop.core.data.source.remote

import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.network.ApiService
import com.mankart.eshop.core.data.source.remote.response.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    // authentication
    suspend fun postLogin(email: String, password: String): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val response = apiService.postLogin(email, password)
                emit(ApiResponse.Success(response.data))
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
    suspend fun postRegister(name: String, email: String, password: String): Flow<ApiResponse<ResponseWithoutData>> {
        return flow {
            try {
                val response = apiService.postRegister(name, email, password)
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    // profile
    suspend fun getProfile(token: String): Flow<ApiResponse<ProfileResponse>> {
        return flow {
            try {
                val response = apiService.getProfile(token)
                emit(ApiResponse.Success(response.data))
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }


    // products
    suspend fun getProducts(page: Int, size: Int): Flow<ApiResponse<List<ProductResponse>>> {
        return flow {
            try {
                val response = apiService.getProducts(page, size)
                emit(ApiResponse.Success(response.data))
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getProductById(id: String): Flow<ApiResponse<ProductResponse>> {
        return flow {
            try {
                val response = apiService.getProductById(id)
                emit(ApiResponse.Success(response.data))
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    // carts
    suspend fun getCarts(token: String): Flow<ApiResponse<CartResponse>> {
        return flow {
            try {
                val response = apiService.getCarts(token)
                emit(ApiResponse.Success(response.data))
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postCart(token: String, productId: String, quantity: Int): Flow<ApiResponse<ResponseWithoutData>> {
        return flow {
            try {
                val response = apiService.postCart(token, productId, quantity)
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun putCart(token: String, itemId: String, quantity: Int): Flow<ApiResponse<ResponseWithoutData>> {
        return flow {
            try {
                val response = apiService.putCart(token, itemId, quantity)
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteCart(token: String, itemId: String): Flow<ApiResponse<ResponseWithoutData>> {
        return flow {
            try {
                val response = apiService.deleteCart(token, itemId)
                emit(ApiResponse.Message(response.message))
            } catch (err: Exception) {
                emit(ApiResponse.Error(err.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

}