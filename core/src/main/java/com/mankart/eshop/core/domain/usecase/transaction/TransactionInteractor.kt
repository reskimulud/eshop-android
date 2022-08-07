package com.mankart.eshop.core.domain.usecase.transaction

import com.mankart.eshop.core.data.Resource
import com.mankart.eshop.core.domain.model.Transaction
import com.mankart.eshop.core.domain.repository.ITransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionInteractor @Inject constructor(private val repository: ITransactionRepository): TransactionUseCase {
    override fun getTransactions(): Flow<Resource<List<Transaction>>> =
        repository.getTransactions()

    override fun getTransactionById(id: String): Flow<Resource<Transaction>> =
        repository.getTransactionById(id)

    override fun checkout(): Flow<Resource<String>> =
        repository.checkout()
}