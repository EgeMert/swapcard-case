package com.egemert.swapcardcase.domain.repository

import com.egemert.swapcardcase.data.local.AppDatabase
import com.egemert.swapcardcase.data.local.entity.BookmarkedUser
import com.egemert.swapcardcase.data.response.User
import com.egemert.swapcardcase.network.ApiResult
import com.egemert.swapcardcase.network.ApiService
import com.egemert.swapcardcase.network.handleApi
import com.egemert.swapcardcase.data.response.UserResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val database: AppDatabase
) : UserRepository {

    private val bookmarkedUserDao = database.bookmarkedUserDao()

    override suspend fun getUsers(page: Int, results: Int): ApiResult<UserResponse> =
        handleApi {
            apiService.getUsers(page = page, results = results)
        }

    override fun getBookmarkedUsers(): Flow<List<User>> {
        return bookmarkedUserDao.getAllBookmarkedUsers().map { bookmarkedUsers ->
            bookmarkedUsers.map { it.toUser() }
        }
    }

    override suspend fun addBookmark(user: User) {
        val bookmarkedUser = BookmarkedUser.fromUser(user)
        bookmarkedUserDao.insertBookmarkedUser(bookmarkedUser)
    }

    override suspend fun removeBookmark(user: User) {
        val bookmarkedUser = BookmarkedUser.fromUser(user)
        bookmarkedUserDao.deleteBookmarkedUser(bookmarkedUser)
    }
}