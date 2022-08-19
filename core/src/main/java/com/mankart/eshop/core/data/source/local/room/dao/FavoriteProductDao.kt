package com.mankart.eshop.core.data.source.local.room.dao

import androidx.room.*
import com.mankart.eshop.core.data.source.local.entity.FavoriteProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favProduct")
    fun getFavoriteProducts(): Flow<List<FavoriteProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteProduct(product: FavoriteProductEntity)

    @Query("DELETE FROM favProduct WHERE id = :id")
    fun deleteFavoriteProductById(id: String)

    @Query("SELECT EXISTS(SELECT id FROM favProduct WHERE id = :id)")
    fun isFavoriteProduct(id: String): Flow<Boolean>
}