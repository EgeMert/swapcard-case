package com.egemert.swapcardcase.domain.repository

import com.egemert.swapcardcase.network.ApiResult
import com.egemert.swapcardcase.network.ApiService
import com.egemert.swapcardcase.network.handleApi
import com.egemert.swapcardcase.data.response.UserResponse
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : UserRepository {

    override suspend fun getUsers(page: Int, results: Int): ApiResult<UserResponse> =
        handleApi {
            apiService.getUsers(page = page, results = results)
        }

}