package com.elfiky.data.network.api

import com.elfiky.data.network.entities.ApiResponse
import com.elfiky.data.network.entities.PersonApi
import com.elfiky.data.network.entities.PersonImagesApi
import com.elfiky.data.network.entities.PersonOverviewApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("person/popular")
    suspend fun fetchPopularPersons(@Query("page") page: Int = 1): ApiResponse<List<PersonOverviewApi>>

    @GET("person/{id}")
    suspend fun fetchPersonDetails(@Path("id") id: Int): PersonApi

    @GET("person/{id}/images")
    suspend fun fetchPersonImages(@Path("id") id: Int): PersonImagesApi
}