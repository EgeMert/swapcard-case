package com.egemert.swapcardcase.network.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>() 
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>() 
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Flow<NetworkResult<T>> = flow {
    emit(NetworkResult.Loading)
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let { body ->
                emit(NetworkResult.Success(body))
            } ?: emit(NetworkResult.Error("Response body is null"))
        } else {
            emit(NetworkResult.Error(response.message(), response.code()))
        }
    } catch (e: HttpException) {
        emit(NetworkResult.Error(e.message ?: "An unknown error occurred", e.code()))
    } catch (e: IOException) {
        emit(NetworkResult.Error("No internet connection. Please check your network and try again."))
    } catch (e: Exception) {
        emit(NetworkResult.Error(e.message ?: "An unknown error occurred"))
    }
}
