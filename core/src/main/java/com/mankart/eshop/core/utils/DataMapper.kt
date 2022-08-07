package com.mankart.eshop.core.utils

import com.mankart.eshop.core.data.source.local.entity.ProductEntity
import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ProductResponse
import com.mankart.eshop.core.data.source.remote.response.ProfileResponse
import com.mankart.eshop.core.data.source.remote.response.ReviewResponse
import com.mankart.eshop.core.domain.model.Product
import com.mankart.eshop.core.domain.model.User
import com.mankart.eshop.core.domain.model.UserReview

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
}