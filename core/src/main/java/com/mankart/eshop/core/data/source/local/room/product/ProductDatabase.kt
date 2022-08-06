package com.mankart.eshop.core.data.source.local.room.product

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys

@Database(entities = [ProductEntity::class, RemoteKeys::class], version = 1, exportSchema = false)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}