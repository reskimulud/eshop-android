package com.mankart.eshop.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.data.source.NetworkBoundResource
import com.mankart.eshop.core.data.source.local.LocalDataSource
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys
import com.mankart.eshop.core.data.source.remote.RemoteDataSource
import com.mankart.eshop.core.data.source.remote.network.ApiResponse
import com.mankart.eshop.core.data.source.remote.response.ProductResponse
import com.mankart.eshop.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
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
            val response = getResponse(page = page, size = state.config.pageSize).first()
            val endOfPaginationReached = response.data?.isEmpty()

            if (loadType == LoadType.REFRESH) {
                localDataSource.deleteRemoteKeys()
                localDataSource.deleteProducts()
            }

            val nextKey = if (endOfPaginationReached == true) null else page + 1
            val prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
            val keys = response.data?.map {
                RemoteKeys(
                    id = it.id,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            if (keys != null) {
                localDataSource.insertRemoteKey(keys)
            }

            val emptyList: List<ProductEntity> = listOf()
            localDataSource.insertProducts(response.data?:emptyList)

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (err: Exception) {
            MediatorResult.Error(err)
        }
    }

    private suspend fun getResponse(page: Int, size: Int): Flow<Resource<List<ProductEntity>>> =
        object: NetworkBoundResource<List<ProductEntity>, List<ProductResponse>>() {
            override suspend fun fetchFromApi(response: List<ProductResponse>): List<ProductEntity> =
                response.map {
                    DataMapper.mapProductsResponseToEntity(it)
                }

            override suspend fun createCall(): Flow<ApiResponse<List<ProductResponse>>> =
                remoteDataSource.getProducts(page, size)
        }.asFlow()

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