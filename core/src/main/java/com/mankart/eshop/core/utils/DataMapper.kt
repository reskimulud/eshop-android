package com.mankart.eshop.core.utils

import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.domain.model.User

object DataMapper {
    fun mapLoginResponseToDomain(input: LoginResponse) : User {
        return User(
            id = input.id,
            token = input.token,
        )
    }
}