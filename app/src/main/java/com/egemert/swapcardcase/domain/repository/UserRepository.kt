package com.egemert.swapcardcase.domain.repository

import com.egemert.swapcardcase.data.response.User
import com.egemert.swapcardcase.network.ApiResult
import com.egemert.swapcardcase.data.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(
        page: Int = 1,
        results: Int = 25
    ): ApiResult<UserResponse>

    fun getBookmarkedUsers(): Flow<List<User>>
    suspend fun addBookmark(user: User)
    suspend fun removeBookmark(user: User)
}