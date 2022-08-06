package com.mankart.eshop.core.utils

import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ProfileResponse
import com.mankart.eshop.core.domain.model.User

object DataMapper {
    fun mapLoginResponseToDomain(input: LoginResponse) : User = User(
        id = input.id,
        token = input.token,
    )

    fun mapProfileResponseToDomain(input: ProfileResponse) : User = User(
        name = input.name,
        email = input.email,
    )
}