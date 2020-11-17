package com.elfiky.data.network

import com.elfiky.data.network.api.MoviesApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object Network {

    private const val baseHost = "api.themoviedb.org/3/"
    private const val baseUrl = "https://$baseHost"
    private const val imagesHost = "image.tmdb.org/t/p/"
    internal const val imagesBaseUrl = "https://$imagesHost"

    fun okHttp(apiKeyInterceptor: Interceptor, ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    fun moshiFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    fun retrofit(client: OkHttpClient, factory: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(factory)
            .build()
    }

    fun moviesService(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }
}