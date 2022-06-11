package com.tinkoff.travelapp.data.api

import android.annotation.SuppressLint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@SuppressLint("AuthLeak")
object RetrofitInstance {
    private val client = OkHttpClient.Builder().apply {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        addInterceptor(MainInterceptor())
        addInterceptor(logging)
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.0.101:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
