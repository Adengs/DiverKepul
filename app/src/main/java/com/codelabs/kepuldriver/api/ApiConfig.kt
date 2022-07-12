package com.codelabs.kepuldriver.api

import android.content.Context
import com.codelabs.kepuldriver.helper.SharedPreference
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    const val BASE_URL = "http://128.199.125.236/kepul/kepul-auth/public/"

    fun getClient(context: Context): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        var header : Interceptor = HeaderInterceptor()

        val client : OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(header)
            .addInterceptor(interceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

    }

    fun instanceRetrofit (context: Context): ApiServices {
        return getClient(context).create(ApiServices::class.java)
    }

    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("AppToken", "0ZB3PmBPxJrXxkzgdGTQxTj413jx6hHRbMK6qpdO")
                    .build()
            )
        }

    }

}