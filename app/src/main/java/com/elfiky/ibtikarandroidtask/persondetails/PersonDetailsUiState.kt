package com.elfiky.ibtikarandroidtask.persondetails

import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.Person

sealed class PersonDetailsState {
    object Loading : PersonDetailsState()
    data class Success(val person: Person) : PersonDetailsState()
    data class Error(val failure: Failures) : PersonDetailsState()
}