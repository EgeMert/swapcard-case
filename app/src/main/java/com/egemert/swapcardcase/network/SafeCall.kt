package com.egemert.swapcardcase.network

import com.egemert.swapcardcase.data.error.ApiErrorResponse
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (message: String?) -> Unit
): ApiResult<T> = apply {

    if (this is ApiError<T>) {
        executable(message)

    }
}

suspend fun <T : Any> ApiResult<T>.onException(
    executable: suspend (e: Throwable) -> Unit
): ApiResult<T> = apply {
    if (this is ApiException<T>) {
        executable(e)
    }
}

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiSuccess(body)
        } else {
            val errorBody = response.errorBody()?.string()
            val errorMessage = try {
                Gson().fromJson(errorBody, ApiErrorResponse::class.java)?.error
                    ?: response.message()
            } catch (e: Exception) {
                response.message()
            }
            ApiError(message = errorMessage.ifEmpty { "An unknown error occurred" })
        }
    } catch (e: HttpException) {
        val errorResponse = e.response()?.errorBody()?.string()
        val errorMessage = try {
            Gson().fromJson(errorResponse, ApiErrorResponse::class.java)?.error
                ?: e.message()
        } catch (e: Exception) {
            e.message
        }
        ApiError(message = errorMessage ?: "An unknown error occurred")
    } catch (e: Throwable) {
        ApiException(e)
    }
}