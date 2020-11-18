package com.elfiky.data.network

import com.elfiky.data.extinsions.toFailure
import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.Result

internal suspend inline fun <T : Any> safeApiCall(
    crossinline block: suspend () -> T
): Result<T, Failures> {
    return try {
        val result = block()
        Result.Success(result)
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Failure(e.toFailure())
    }
}