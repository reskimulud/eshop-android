package com.mankart.eshop.core.domain.repository

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface ITransactionRepository {
    fun getTransactions(): Flow<Resource<List<Transaction>>>
    fun getTransactionById(id: String): Flow<Resource<Transaction>>
    fun checkout(): Flow<Resource<String>>
}