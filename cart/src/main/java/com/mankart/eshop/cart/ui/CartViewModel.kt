package com.mankart.eshop.cart.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.cart.CartUseCase
import com.mankart.eshop.core.domain.usecase.transaction.TransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartUseCase: CartUseCase,
    private val transactionUseCase: TransactionUseCase
): ViewModel() {
    fun getCarts() = cartUseCase.getCarts()
    fun checkout() = transactionUseCase.checkout()
}