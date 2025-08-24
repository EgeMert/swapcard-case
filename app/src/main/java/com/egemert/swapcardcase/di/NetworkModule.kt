package com.egemert.swapcardcase.di

import android.app.Application
import android.content.Context
import com.egemert.swapcardcase.network.ApiService
import com.egemert.swapcardcase.network.NetworkConnectionInterceptor
import com.egemert.swapcardcase.network.Constants
import com.egemert.swapcardcase.domain.repository.UserRepository
import com.egemert.swapcardcase.domain.repository.UserRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideApplication(
        @ApplicationContext
        app: Context,
    ): Application {
        return app as Application
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkConnectionInterceptor)
            .connectTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            .readTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT_IN_SEC, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(userRepository: UserRepositoryImpl): UserRepository =
        userRepository
}
