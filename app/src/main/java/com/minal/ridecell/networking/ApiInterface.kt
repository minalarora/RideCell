package com.minal.ridecell.networking

import com.minal.ridecell.models.RequestUser
import com.minal.ridecell.models.Token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiInterface {

       @POST("/api/v2/people/create")
       suspend fun createUser(@Body user: RequestUser) : Response<Token>

       @POST("/api/v2/people/authenticate")
       suspend fun loginUser(@Body user: RequestUser) : Response<Token>

       @GET("/api/v2/people/me")
       suspend fun getCurrentUser(@Header("Authorization") token: String) : Response<RequestUser>
}