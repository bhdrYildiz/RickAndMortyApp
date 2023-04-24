package com.example.rickandmorty.Apis

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object retrofit_client {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: retrofit_apis by lazy {
        retrofit.create(retrofit_apis::class.java)
    }

}