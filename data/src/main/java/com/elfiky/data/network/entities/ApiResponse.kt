package com.elfiky.data.network.entities

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ApiResponse<T>(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: T,
)