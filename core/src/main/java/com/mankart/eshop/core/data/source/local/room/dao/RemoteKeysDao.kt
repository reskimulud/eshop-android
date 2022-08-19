package com.mankart.eshop.core.data.source.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mankart.eshop.core.data.source.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remoteKeys WHERE id = :id")
    suspend fun getRemoteKey(id: String): RemoteKeys?

    @Query("DELETE FROM remoteKeys")
    suspend fun deleteAllRemoteKeys()
}