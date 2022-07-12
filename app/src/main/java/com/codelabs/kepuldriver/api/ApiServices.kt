package com.codelabs.kepuldriver.api

import com.codelabs.kepuldriver.model.ProfileResponse
import com.codelabs.kepuldriver.model.RequestUser
import com.codelabs.kepuldriver.model.ResponseUser
import com.codelabs.kepuldriver.model.TokenRequest
import com.codelabs.kepuldriver.model.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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
    fun getProfile() : Call<ProfileResponse>
}