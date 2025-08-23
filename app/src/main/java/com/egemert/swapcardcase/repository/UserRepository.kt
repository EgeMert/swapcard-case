package com.egemert.swapcardcase.network.repository

import com.egemert.swapcardcase.network.ApiService
import com.egemert.swapcardcase.network.model.User
import com.egemert.swapcardcase.network.NetworkResult
import com.egemert.swapcardcase.network.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {

    suspend fun getUsers(page: Int = 1, results: Int = 10): Flow<NetworkResult<List<User>>> {
        return flow {
            safeApiCall {
                apiService.getUsers(page, results)
            }.collect { result ->
                when (result) {
                    is NetworkResult.Success -> {
                        result.data.results?.let { results ->
                            emit(NetworkResult.Success(results))
                        }
                    }

                    is NetworkResult.Error -> {
                        emit(NetworkResult.Error(result.message, result.code))
                    }

                    is NetworkResult.Loading -> {
                        emit(NetworkResult.Loading)
                    }
                }
            }
        }
    }
}
