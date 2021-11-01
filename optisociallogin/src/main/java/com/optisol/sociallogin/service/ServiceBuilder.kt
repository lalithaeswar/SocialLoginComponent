package com.optisol.sociallogin.service

import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()


    private val instraRetrofit = Retrofit.Builder()
        .baseUrl("https://api.instagram.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ApiService::class.java)
    private val graphRetrofit = Retrofit.Builder()
        .baseUrl("https://graph.instagram.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(ApiService::class.java)


    fun buildService(baseUrl:String): ApiService {
        return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)
    }
    fun buildGraphService(): ApiService {
        return graphRetrofit
    }

}