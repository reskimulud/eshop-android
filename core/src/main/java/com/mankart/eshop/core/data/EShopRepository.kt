package com.mankart.eshop.core.data

import androidx.paging.*
import com.mankart.eshop.core.data.paging.ProductRemoteMediator
import com.mankart.eshop.core.data.source.NetworkBoundResource
import com.mankart.eshop.core.data.source.local.LocalDataSource
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.remote.RemoteDataSource
import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ProfileResponse
import com.mankart.eshop.core.data.source.remote.response.ResponseWithoutData
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.repository.IAuthenticationRepository
import com.mankart.eshop.core.domain.repository.IProductRepository
import com.mankart.eshop.core.domain.repository.IProfileRepository
import com.mankart.eshop.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
): IAuthenticationRepository, IProfileRepository, IProductRepository {
    // authentication
    override fun postLogin(email: String, password: String): Flow<Resource<User>> =
        object: NetworkBoundResource<User, LoginResponse>() {
            override suspend fun fetchFromApi(response: LoginResponse): User {
                // store the token value to dataStore
                localDataSource.saveUserToken(response.token)
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

    // profile
    override fun getProfile(): Flow<Resource<User>> =
        object: NetworkBoundResource<User, ProfileResponse>() {
            override suspend fun fetchFromApi(response: ProfileResponse): User {
                // store email and name to dataStore
                localDataSource.apply {
                    saveUserEmail(response.email)
                    saveUserName(response.name)
                }
                return DataMapper.mapProfileResponseToDomain(response)
            }

            override suspend fun createCall(): Flow<ApiResponse<ProfileResponse>> {
                val token = localDataSource.getUserToken().first()
                return remoteDataSource.getProfile(token)
            }
        }.asFlow()

    override suspend fun logout() = localDataSource.clearCache()

    // products
    @OptIn(ExperimentalPagingApi::class)
    private fun getProductsPager(): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource
            ),
            pagingSourceFactory = { localDataSource.getProducts() }
        ).flow
    }

    override fun getProducts(): Flow<PagingData<Product>> =
        getProductsPager().map { pagingData ->
            pagingData.map { product ->
                DataMapper.mapProductEntityToDomain(product)
            }
        }
}