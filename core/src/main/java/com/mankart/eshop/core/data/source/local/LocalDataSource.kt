package com.mankart.eshop.core.data.source.local

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.mankart.eshop.core.data.source.local.datastore.PreferenceDataStore
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys
import com.mankart.eshop.core.data.source.local.room.product.ProductDao
import com.mankart.eshop.core.data.source.local.room.product.ProductDatabase
import com.mankart.eshop.core.data.source.local.room.product.RemoteKeysDao
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(
    private val dataStore: PreferenceDataStore,
    private val productDao: ProductDao,
    private val remoteKeysDao: RemoteKeysDao
) {
    // interact with data store
    fun getUserToken(): Flow<String> = dataStore.getUserToken()
    suspend fun saveUserToken(token: String) = dataStore.saveUserToken(token)

    fun getUserName(): Flow<String> = dataStore.getUserName()
    suspend fun saveUserName(name: String) = dataStore.saveUserName(name)

    fun getUserEmail(): Flow<String> = dataStore.getUserEmail()
    suspend fun saveUserEmail(email: String) = dataStore.saveUserEmail(email)

    suspend fun clearCache() = dataStore.clearCache()

    // interact with product database
    fun getProducts(): PagingSource<Int, ProductEntity> = productDao.getAllProducts()
    suspend fun insertProducts(products: List<ProductEntity>) = productDao.insertAllProducts(products)
    suspend fun deleteProducts() = productDao.deleteAllProducts()

    // interact with remote keys (paging)
    suspend fun getRemoteKey(id: String): RemoteKeys? = remoteKeysDao.getRemoteKey(id)
    suspend fun insertRemoteKey(remoteKey: List<RemoteKeys>) = remoteKeysDao.insertAll(remoteKey)
    suspend fun deleteRemoteKeys() = remoteKeysDao.deleteAllRemoteKeys()
}