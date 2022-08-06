package com.mankart.eshop.core.data.source.remote.network

import com.mankart.eshop.core.data.source.remote.response.*
import retrofit2.http.*

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

    @GET("products")
    fun getProducts(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ) : ResponseWithData<List<ProductResponse>>
}