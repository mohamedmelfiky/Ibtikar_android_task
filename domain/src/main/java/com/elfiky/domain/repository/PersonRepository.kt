package com.elfiky.domain.repository

import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.Person
import com.elfiky.domain.entities.Result

interface PersonRepository {
    suspend fun getDetails(id: Int): Result<Person, Failures>
}