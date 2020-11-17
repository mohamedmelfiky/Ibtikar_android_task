package com.elfiky.data.extinsions

import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.domain.entities.NotFound
import com.elfiky.domain.entities.UnknownFailure
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toFailure(): Failures {
    return when (this) {
        is IOException -> NetworkFailure
        is HttpException -> {
            when (this.code()) {
                401 -> InvalidApiKey
                404 -> NotFound
                else -> UnknownFailure
            }
        }
        else -> UnknownFailure
    }
}