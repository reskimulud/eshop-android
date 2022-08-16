package com.mankart.eshop.core.data.source.remote.network

import com.mankart.eshop.core.data.source.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseWithData<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : ResponseWithoutData

    @GET("user")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ) : ResponseWithData<ProfileResponse>

    @GET("products")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("s") search: String? = null
    ) : Response<ResponseWithData<List<ProductResponse>>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ) : ResponseWithData<ProductResponse>

    @GET("products/categories")
    suspend fun getProductCategories(): ResponseWithData<List<ProductCategoryResponse>>

    @GET("products/categories/{id}")
    suspend fun getProductsByCategoryId(
        @Path("id") categoryId: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("s") search: String? = null
    ) : Response<ResponseWithData<ProductByCategoryResponse>>

    @GET("carts")
    suspend fun getCarts(
        @Header("Authorization") token: String
    ) : ResponseWithData<CartResponse>

    @FormUrlEncoded
    @POST("carts")
    suspend fun postCart(
        @Header("Authorization") token: String,
        @Field("productId") productId: String,
        @Field("quantity") quantity: Int
    ) : ResponseWithoutData

    @PUT("carts/{id}")
    suspend fun putCart(
        @Header("Authorization") token: String,
        @Path("id") itemId: String,
        @Query("qty") quantity: Int
    ) : ResponseWithoutData

    @DELETE("carts/{id}")
    suspend fun deleteCart(
        @Header("Authorization") token: String,
        @Path("id") itemId: String
    ) : ResponseWithoutData

    @GET("transactions")
    suspend fun getTransactions(
        @Header("Authorization") token: String
    ) : ResponseWithData<List<TransactionResponse>>

    @GET("transactions/{id}")
    suspend fun getTransactionById(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ) : ResponseWithData<TransactionResponse>

    @POST("checkout")
    suspend fun postCheckout(
        @Header("Authorization") token: String
    ) : ResponseWithoutData

    @FormUrlEncoded
    @POST("products/{id}/rating")
    suspend fun postReview(
        @Header("Authorization") token: String,
        @Path("id") productId: String,
        @Field("rate") rate: Int,
        @Field("review") review: String
    ) : ResponseWithoutData

}