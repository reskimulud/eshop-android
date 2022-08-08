package com.mankart.eshop.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remoteKeys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val nextKey: Int?,
    val prevKey: Int?
)
