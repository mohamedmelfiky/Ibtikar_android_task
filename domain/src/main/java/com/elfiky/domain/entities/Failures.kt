package com.elfiky.domain.entities

sealed class Failures
object NetworkFailure : Failures()
object InvalidApiKey : Failures()
object NotFound : Failures()
object UnknownFailure : Failures()