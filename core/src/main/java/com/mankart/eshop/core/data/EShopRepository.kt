package com.mankart.eshop.core.data

import androidx.paging.*
import com.mankart.eshop.core.data.paging.ProductRemoteMediator
import com.mankart.eshop.core.data.source.NetworkBoundResource
import com.mankart.eshop.core.data.source.local.LocalDataSource
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.remote.RemoteDataSource
import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.response.*
import com.mankart.eshop.core.domain.model.*
import com.mankart.eshop.core.domain.repository.*
import com.mankart.eshop.core.utils.AppExecutors
import com.mankart.eshop.core.utils.DataMapper
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EShopRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
): IAuthenticationRepository,
    IProfileRepository,
    IProductRepository,
    ICartRepository,
    ITransactionRepository,
    IFavoriteProductRepository
{

    private suspend fun token(): String =
        "Bearer " + localDataSource.getUserToken().first()

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

    override fun getUserToken(): Flow<String> = localDataSource.getUserToken()

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

            override suspend fun createCall(): Flow<ApiResponse<ProfileResponse>> =
                remoteDataSource.getProfile(token())
        }.asFlow()

    override fun postReview(productId: String, rate: Int, review: String): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message

            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> =
                remoteDataSource.postReview(token(), productId, rate, review)
        }.asFlow()

    override suspend fun logout() = localDataSource.clearCache()

    override suspend fun getName(): Flow<String> = flow {
        emit(localDataSource.getUserName().first())
    }

    override suspend fun getEmail(): Flow<String> = flow {
        emit(localDataSource.getUserEmail().first())
    }

    override suspend fun saveName(name: String) = localDataSource.saveUserName(name)
    override suspend fun saveEmail(email: String) = localDataSource.saveUserEmail(email)


    // products
    @OptIn(ExperimentalPagingApi::class)
    private fun getProductsPager(categoryId: String?, search: String?): Flow<PagingData<ProductEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = ProductRemoteMediator(
                remoteDataSource = remoteDataSource,
                localDataSource = localDataSource,
                categoryId = categoryId,
                searchQuery = search,
            ),
            pagingSourceFactory = { localDataSource.getProducts() }
        ).flow
    }

    override fun getProducts(categoryId: String?, search: String?): Flow<PagingData<Product>> =
        getProductsPager(categoryId, search).map { pagingData ->
            pagingData.map { product ->
                DataMapper.mapProductEntityToDomain(product)
            }
        }

    override fun getProductCategories(): Flow<Resource<List<ProductCategory>>> =
        object: NetworkBoundResource<List<ProductCategory>, List<ProductCategoryResponse>>() {
            override suspend fun fetchFromApi(response: List<ProductCategoryResponse>): List<ProductCategory> =
                response.map {
                    ProductCategory(
                        id = it.id,
                        name = it.name
                    )
                }

            override suspend fun createCall(): Flow<ApiResponse<List<ProductCategoryResponse>>> =
                remoteDataSource.getProductCategories()
        }.asFlow()

    override fun getProductById(id: String): Flow<Resource<Product>> =
        object: NetworkBoundResource<Product, ProductResponse>() {
            override suspend fun fetchFromApi(response: ProductResponse): Product =
                DataMapper.mapProductResponseToDomain(response)

            override suspend fun createCall(): Flow<ApiResponse<ProductResponse>> =
                remoteDataSource.getProductById(id)
        }.asFlow()


    // carts
    override fun getCarts(): Flow<Resource<Cart>> =
        object: NetworkBoundResource<Cart, CartResponse>() {
            override suspend fun fetchFromApi(response: CartResponse): Cart =
                DataMapper.mapCartResponseToDomain(response)

            override suspend fun createCall(): Flow<ApiResponse<CartResponse>> =
                remoteDataSource.getCarts(token())
        }.asFlow()

    override fun addItemToCart(productId: String, quantity: Int): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message

            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> =
                remoteDataSource.postCart(token(), productId, quantity)
        }.asFlow()

    override fun updateItemInCart(itemId: String, quantity: Int): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message

            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> =
                remoteDataSource.putCart(token(), itemId, quantity)
        }.asFlow()

    override fun deleteItemFromCart(itemId: String): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message

            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> =
                remoteDataSource.deleteCart(token(), itemId)
        }.asFlow()


    // transactions
    override fun getTransactions(): Flow<Resource<List<Transaction>>> =
        object: NetworkBoundResource<List<Transaction>, List<TransactionResponse>>() {
            override suspend fun fetchFromApi(response: List<TransactionResponse>): List<Transaction> =
                response.map { DataMapper.mapTransactionResponseToDomain(it) }

            override suspend fun createCall(): Flow<ApiResponse<List<TransactionResponse>>> =
                remoteDataSource.getTransactions(token())
        }.asFlow()

    override fun getTransactionById(id: String): Flow<Resource<Transaction>> =
        object: NetworkBoundResource<Transaction, TransactionResponse>() {
            override suspend fun fetchFromApi(response: TransactionResponse): Transaction =
                DataMapper.mapTransactionResponseToDomain(response)

            override suspend fun createCall(): Flow<ApiResponse<TransactionResponse>> =
                remoteDataSource.getTransactionById(token(), id)
        }.asFlow()

    override fun checkout(): Flow<Resource<String>> =
        object: NetworkBoundResource<String, ResponseWithoutData>() {
            override suspend fun fetchFromApi(response: ResponseWithoutData): String = response.message
            override suspend fun createCall(): Flow<ApiResponse<ResponseWithoutData>> =
                remoteDataSource.postCheckout(token())
        }.asFlow()

    // favorite
    override fun getFavoriteProducts(): Flow<Resource<List<Product>>> = flow {
        emit(Resource.Loading())
        val loadFromDB = localDataSource.getFavouriteProducts().map {
            it.map { product ->
                DataMapper.mapFavouriteProductEntityToDomain(product)
            }
        }
        emit(Resource.Success(loadFromDB.first()))
    }

    override suspend fun addFavoriteProduct(product: Product) {
        val favouriteProductEntity = DataMapper.mapFavouriteProductDomainToEntity(product)
        localDataSource.insertFavouriteProduct(favouriteProductEntity)
    }

    override fun deleteFavoriteProductById(productId: String) =
        appExecutors.diskIO().execute {
            localDataSource.deleteFavouriteProductById(productId)
        }

    override fun isFavoriteProduct(productId: String): Flow<Boolean> =
        localDataSource.isFavoriteProduct(productId)
}