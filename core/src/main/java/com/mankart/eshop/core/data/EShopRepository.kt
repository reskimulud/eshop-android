package com.mankart.eshop.core.data

import com.mankart.eshop.core.data.source.NetworkBoundResource
import com.mankart.eshop.core.data.source.local.datastore.PreferenceDataStore
import com.mankart.eshop.core.data.source.remote.RemoteDataSource
import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ResponseWithoutData
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.repository.IAuthenticationRepository
import com.mankart.eshop.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dataStore: PreferenceDataStore,
): IAuthenticationRepository {
    // authentication
    override fun postLogin(email: String, password: String): Flow<Resource<User>> =
        object: NetworkBoundResource<User, LoginResponse>() {
            override suspend fun fetchFromApi(response: LoginResponse): User {
                // store the token value to dataStore
                dataStore.saveUserToken(response.token)
                return DataMapper.mapLoginResponseToDomain(response)
            }
            override suspend fun createCall(): Flow<ApiResponse<LoginResponse>> =
                remoteDataSource.postLogin(email, password)
        }.asFlow()

    override fun postRegister(name: String, email: String, password: String): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message
            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> = remoteDataSource.postRegister(name, email, password)
        }.asFlow()

}