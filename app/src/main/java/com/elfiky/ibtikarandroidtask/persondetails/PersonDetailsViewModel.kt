package com.elfiky.ibtikarandroidtask.persondetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elfiky.domain.entities.Result
import com.elfiky.domain.repository.PersonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PersonDetailsViewModel(
    private val id: Int,
    private val personRepository: PersonRepository
) : ViewModel() {

    private val _state = MutableStateFlow<PersonDetailsState>(PersonDetailsState.Loading)
    val state: StateFlow<PersonDetailsState> = _state
    private var currentJob: Job? = null

    init { getPersonDetails() }

    fun getPersonDetails() {
        currentJob?.cancel()
        _state.value = PersonDetailsState.Loading
        currentJob = viewModelScope.launch {
            val result = personRepository.getDetails(id)
            _state.value = when (result) {
                is Result.Success -> PersonDetailsState.Success(result.value)
                is Result.Failure -> PersonDetailsState.Error(result.failure)
            }
        }
    }
}