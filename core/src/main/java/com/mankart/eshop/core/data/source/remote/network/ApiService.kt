package com.mankart.eshop.core.data.source.remote.network

import com.mankart.eshop.core.data.source.remote.response.ResponseWithData
import com.mankart.eshop.core.data.source.remote.response.LoginResponse
import com.mankart.eshop.core.data.source.remote.response.ProfileResponse
import com.mankart.eshop.core.data.source.remote.response.ResponseWithoutData
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseWithData<LoginResponse>

    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseWithoutData

    @GET("user")
    fun getProfile(
        @Header("Authorization") token: String
    ) : ResponseWithData<ProfileResponse>
}