package com.elfiky.domain.entities

sealed class Result<out T, out E> {
    data class Success<T : Any>(val result: T) : Result<T, Nothing>()
    data class Failure<out E>(val error: E) : Result<Nothing, E>()
}

fun <T, E, R: Any> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Success -> Result.Success(transform(this.result))
        is Result.Failure -> Result.Failure(this.error)
    }
}

fun <T: Any, A: Any, R: Any, E> Result<T, E>.zip(
    another: Result<A, E>,
    transform: (T, A) -> R
): Result<R, E> {
    return when(this) {
        is Result.Success -> {
            when(another) {
                is Result.Success -> Result.Success(transform(this.result, another.result))
                is Result.Failure -> Result.Failure(another.error)
            }
        }
        is Result.Failure -> Result.Failure(this.error)
    }
}