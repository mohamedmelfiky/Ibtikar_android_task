package com.elfiky.domain.entities

sealed class Result<out T, out E> {
    data class Success<T : Any>(val value: T) : Result<T, Nothing>()
    data class Failure<out E>(val failure: E) : Result<Nothing, E>()
}

fun <T, E, R: Any> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when(this) {
        is Result.Success -> Result.Success(transform(this.value))
        is Result.Failure -> Result.Failure(this.failure)
    }
}

fun <T: Any, A: Any, R: Any, E> Result<T, E>.zip(
    another: Result<A, E>,
    transform: (T, A) -> R
): Result<R, E> {
    return when(this) {
        is Result.Success -> {
            when(another) {
                is Result.Success -> Result.Success(transform(this.value, another.value))
                is Result.Failure -> Result.Failure(another.failure)
            }
        }
        is Result.Failure -> Result.Failure(this.failure)
    }
}