package com.elfiky.data.network

import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.domain.entities.NotFound
import com.elfiky.domain.entities.Result
import com.elfiky.domain.entities.UnknownFailure
import retrofit2.HttpException
import java.io.IOException

internal suspend inline fun <T : Any> safeApiCall(
    crossinline block: suspend () -> T
): Result<T, Failures> {
    return try {
        val result = block()
        Result.Success(result)
    } catch (e: Exception) {
        val error = when (e) {
            is IOException -> NetworkFailure
            is HttpException -> {
                when (e.code()) {
                    401 -> InvalidApiKey
                    404 -> NotFound
                    else -> UnknownFailure
                }
            }
            else -> UnknownFailure
        }
        Result.Failure(error)
    }
}