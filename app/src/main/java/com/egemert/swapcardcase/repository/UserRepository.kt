package com.egemert.swapcardcase.repository

import com.egemert.swapcardcase.network.ApiResult
import com.egemert.swapcardcase.data.response.UserResponse

interface UserRepository {
    suspend fun getUsers(
        page: Int = 1,
        results: Int = 25
    ): ApiResult<UserResponse>
}