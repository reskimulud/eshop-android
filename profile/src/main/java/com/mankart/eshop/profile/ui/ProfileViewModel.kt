package com.mankart.eshop.profile.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.profile.ProfileUseCase
import com.mankart.eshop.core.domain.usecase.transaction.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val transactionUseCase: TransactionUseCase
) : ViewModel() {
    suspend fun getUserName() = profileUseCase.getName()
    suspend fun getEmail() = profileUseCase.getEmail()

    fun getTransactions() = transactionUseCase.getTransactions()
    fun getTransactionById(transactionId: String) =
        transactionUseCase.getTransactionById(transactionId)

    fun addProductReview(productId: String, rate: Int, review: String) =
        profileUseCase.postPreview(productId, rate, review)
}