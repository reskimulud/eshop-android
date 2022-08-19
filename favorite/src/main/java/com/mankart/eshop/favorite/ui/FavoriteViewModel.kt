package com.mankart.eshop.favorite.ui

import androidx.lifecycle.ViewModel
import com.mankart.eshop.core.domain.usecase.favproduct.FavoriteProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val favoriteProductUseCase: FavoriteProductUseCase,
): ViewModel() {
    fun getFavoriteProducts() = favoriteProductUseCase.getFavoriteProducts()
    fun deleteFavoriteProductById(productId: String) =
        favoriteProductUseCase.deleteFavoriteProductById(productId)
}