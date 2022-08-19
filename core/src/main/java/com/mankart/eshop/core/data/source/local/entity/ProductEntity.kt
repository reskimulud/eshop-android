package com.mankart.eshop.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: String,

    @NonNull
    @ColumnInfo(name = "title")
    val title: String,

    @NonNull
    @ColumnInfo(name = "price")
    val price: Int,

    @NonNull
    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "image")
    val image: String,

    @ColumnInfo(name = "rating")
    val rating: Double,

    @ColumnInfo(name = "countRate")
    val countRate: Int
)
