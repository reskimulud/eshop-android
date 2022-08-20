package com.mankart.eshop.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mankart.eshop.core.data.source.local.LocalDataSource
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys
import com.mankart.eshop.core.data.source.remote.RemoteDataSource
import com.mankart.eshop.core.utils.DataMapper
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource,
    private val categoryId: String? = null,
    private val searchQuery: String? = null,
) : RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            suspend fun insertIntoDatabase(endOfPaginationReached: Boolean, productEntity: List<ProductEntity>) {
                if (loadType == LoadType.REFRESH) {
                    localDataSource.deleteRemoteKeys()
                    localDataSource.deleteProducts()
                }

                val nextKey = if (endOfPaginationReached) null else page + 1
                val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1

                val keys = productEntity.map { product ->
                    RemoteKeys(
                        id = product.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                localDataSource.insertRemoteKey(keys)

                localDataSource.insertProducts(productEntity)
            }

            if (!categoryId.isNullOrEmpty() && categoryId != "all") {
                val response = remoteDataSource.getProductsByCategoryId(
                    categoryId,
                    page,
                    size = state.config.pageSize,
                    searchQuery?:"")
                if (response.isSuccessful) {

                    val dataResponse = response.body()!!.data.products
                    val endOfPaginationReached = dataResponse.isEmpty()

                    val productEntity = dataResponse.map {
                        DataMapper.mapProductsResponseToEntity(it)
                    }

                    Timber.d("entity: $productEntity")
                    insertIntoDatabase(endOfPaginationReached, productEntity)
                }
            } else {
                val response = remoteDataSource.getProducts(page, size = state.config.pageSize, searchQuery?:"")
                if (response.isSuccessful) {

                    val dataResponse = response.body()!!.data
                    val endOfPaginationReached = dataResponse.isEmpty()

                    val productEntity = dataResponse.map {
                        DataMapper.mapProductsResponseToEntity(it)
                    }
                    Timber.d("entity: $productEntity")
                    insertIntoDatabase(endOfPaginationReached, productEntity)
                }
            }

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (err: Exception) {
            Timber.e("err: ${err.message}")
            MediatorResult.Error(err)
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProductEntity>) : RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            localDataSource.getRemoteKey(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProductEntity>) : RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            localDataSource.getRemoteKey(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, ProductEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                localDataSource.getRemoteKey(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}