package com.mankart.eshop.core.domain.usecase.transaction

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionUseCase {
    fun getTransactions(): Flow<Resource<List<Transaction>>>
    fun getTransactionById(id: String): Flow<Resource<Transaction>>
    fun checkout(): Flow<Resource<String>>
}