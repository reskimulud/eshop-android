package com.mankart.eshop.core.data.source.remote

import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.network.ApiService
import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ResponseWithoutData
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
                val data = response.data
                emit(ApiResponse.Success(data))
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

}