package com.mankart.eshop.core.utils

import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.remote.response.*
import com.mankart.eshop.core.domain.model.*

object DataMapper {
    fun mapLoginResponseToDomain(input: LoginResponse) : User = User(
        id = input.id,
        token = input.token,
    )

    fun mapProfileResponseToDomain(input: ProfileResponse) : User = User(
        name = input.name,
        email = input.email,
    )

    fun mapProductsResponseToEntity(input: ProductResponse) : ProductEntity = ProductEntity(
        id = input.id,
        title = input.title,
        price = input.price,
        description = input.description,
        image = input.image,
        category = input.category,
        rating = input.rating?.rating ?: 0.0,
        countRate = input.rating?.countRate ?: 0,
    )

    fun mapProductEntityToDomain(input: ProductEntity): Product = Product(
        id = input.id,
        title = input.title,
        price = input.price,
        description = input.description,
        image = input.image,
        category = input.category,
        rating = input.rating,
        countRate = input.countRate
    )

    fun mapProductResponseToDomain(input: ProductResponse): Product = Product(
        id = input.id,
        title = input.title,
        price = input.price,
        description = input.description,
        image = input.image,
        category = input.category,
        rating = input.rating?.rating ?: 0.0,
        countRate = input.rating?.countRate ?: 0,
        reviews = input.reviews?.map { mapUserReviewResponseToDomain(it) } ?: emptyList()
    )

    private fun mapUserReviewResponseToDomain(input: ReviewResponse): UserReview = UserReview(
        user = input.user,
        rate = input.rate,
        review = input.review,
    )

    fun mapCartResponseToDomain(input: CartResponse): Cart = Cart(
        totalItem = input.totalItem,
        subTotal = input.subTotal,
        cart = input.cart.map { mapCartItemResponseToDomain(it) }
    )

    private fun mapCartItemResponseToDomain(input: CartItemResponse): CartItem = CartItem(
        id = input.id,
        productId = input.productId,
        title = input.title,
        price = input.price,
        description = input.description,
        image = input.image,
        quantity = input.quantity,
    )
}