package com.codelabs.kepuldriver.api

import com.codelabs.kepuldriver.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiServices {
    @POST("auth/login")
    fun hitToken(
        @Body request: TokenRequest
    ): Call<TokenResponse>

    @POST("api/v1/driver/login")
    fun login(
        @Body request: RequestUser
    ): Call<ResponseUser>

    @GET("api/v1/driver/profile")
    fun getProfile(): Call<ProfileResponse>

    @PUT("api/v1/driver/profile")
    fun updateProfile(
        @Body request: UpdateRequest
    ): Call<UpdateResponse>

    @PUT("api/v1/driver/profile")
    fun uploadFoto(
        @Body file: MultipartBody.Part?
    ): Call<UpdateResponse>

    @PUT("api/v1/driver/profile")
    fun updatePassword(
        @Body request: PasswordRequest
    ): Call<UpdateResponse>

    @GET("api/v1/driver/order/list?page=1&length=10")
    fun incomingOrder(
        @QueryMap param: Map<String, String>
    ): Call<OrderResponse>

    @GET("api/v1/driver/order/detail/{code}")
    fun getDetail(
        @Path("code") code: String?
    ): Call<DetailResponse>

    @PUT("api/v1/driver/order/accept-order/{code}")
    fun accOrder(
        @Path("code") code: String?
    ): Call<DetailResponse>

    @POST("api/v1/driver/forgot-password")
    fun email(
        @Body request: EmailRequest
    ): Call<EmailVerificationResponse>

    @POST("api/v1/driver/change-password")
    fun changePassword(
        @Body request: RequestUser
    ): Call<ResponseUser>

    @PUT("api/v1/driver/update-ready-status")
    fun hitSwitch(): Call<ResponseUser>

    @Multipart
    @POST("api/v1/driver/order/process-order/{code}")
    fun processOrder(@Path("code") code: String?,
                     @PartMap body: Map<String, @JvmSuppressWildcards RequestBody?>,
                     @Part file :MultipartBody.Part?
    ): Call<ResponseUser>
}