package com.egemert.swapcardcase.network

import android.content.Context
import com.egemert.swapcardcase.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class NetworkConnectionInterceptor @Inject constructor(
    private val context: Context
) : Interceptor {
    
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            throw NoNetworkException(context.getString(R.string.error_no_internet_connection))
        }
        return chain.proceed(chain.request())
    }
}

class NoNetworkException(message: String) : IOException(message)
