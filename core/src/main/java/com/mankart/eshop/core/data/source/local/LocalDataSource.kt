package com.mankart.eshop.core.data.source.local

import androidx.paging.PagingSource
import com.mankart.eshop.core.data.source.local.datastore.PreferenceDataStore
import com.mankart.eshop.core.data.source.local.entity.FavoriteProductEntity
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys
import com.mankart.eshop.core.data.source.local.room.dao.FavoriteProductDao
import com.mankart.eshop.core.data.source.local.room.dao.ProductDao
import com.mankart.eshop.core.data.source.local.room.dao.RemoteKeysDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: PreferenceDataStore,
    private val productDao: ProductDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val favouriteProductDao: FavoriteProductDao
) {
    // interact with data store
    fun getUserToken(): Flow<String> = dataStore.getUserToken()
    suspend fun saveUserToken(token: String) = dataStore.saveUserToken(token)

    fun getUserName(): Flow<String> = dataStore.getUserName()
    suspend fun saveUserName(name: String) = dataStore.saveUserName(name)

    fun getUserEmail(): Flow<String> = dataStore.getUserEmail()
    suspend fun saveUserEmail(email: String) = dataStore.saveUserEmail(email)

    suspend fun clearCache() = dataStore.clearCache()

    // interact with product database table product
    fun getProducts(): PagingSource<Int, ProductEntity> = productDao.getAllProducts()
    suspend fun insertProducts(products: List<ProductEntity>) = productDao.insertAllProducts(products)
    suspend fun deleteProducts() = productDao.deleteAllProducts()

    // interact with remote product database table favProduct
    fun getFavouriteProducts(): Flow<List<FavoriteProductEntity>> =
        favouriteProductDao.getFavoriteProducts()
    suspend fun insertFavouriteProduct(products: FavoriteProductEntity) =
        favouriteProductDao.insertFavoriteProduct(products)
    fun deleteFavouriteProductById(productId: String) =
        favouriteProductDao.deleteFavoriteProductById(productId)
    fun isFavoriteProduct(productId: String): Flow<Boolean> =
        favouriteProductDao.isFavoriteProduct(productId)

    // interact with remote keys (paging)
    suspend fun getRemoteKey(id: String): RemoteKeys? = remoteKeysDao.getRemoteKey(id)
    suspend fun insertRemoteKey(remoteKey: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKey)
    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteAllRemoteKeys()
}