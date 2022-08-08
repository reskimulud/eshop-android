package com.mankart.eshop.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity(tableName = "favProduct", primaryKeys = ["id"])
data class FavoriteProductEntity(
    @Embedded
    val product: ProductEntity,
)
