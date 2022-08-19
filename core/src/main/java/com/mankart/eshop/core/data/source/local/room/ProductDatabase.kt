package com.mankart.eshop.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mankart.eshop.core.data.source.local.entity.FavoriteProductEntity
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys
import com.mankart.eshop.core.data.source.local.room.dao.FavoriteProductDao
import com.mankart.eshop.core.data.source.local.room.dao.ProductDao
import com.mankart.eshop.core.data.source.local.room.dao.RemoteKeysDao

@Database(entities = [
    ProductEntity::class,
    RemoteKeys::class,
    FavoriteProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun remoteKeysDao(): RemoteKeysDao
    abstract fun favouriteProductDao(): FavoriteProductDao
}