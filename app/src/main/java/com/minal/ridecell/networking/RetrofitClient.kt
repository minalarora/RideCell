package com.minal.ridecell.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val baseUrl = "https://blooming-stream-45371.herokuapp.com"

    private val retrofitClient: Retrofit.Builder by lazy {

        val httpClientBuilder =  OkHttpClient.Builder().retryOnConnectionFailure(true)
        val httpClient: OkHttpClient = httpClientBuilder.build()


        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }

}