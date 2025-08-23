package com.egemert.swapcardcase.network.api

import com.egemert.swapcardcase.network.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    
    @GET("api/")
    suspend fun getUsers(
        @Query("page") page: Int = 1,
        @Query("results") results: Int = 25
    ): Response<UserResponse>
}
