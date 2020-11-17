package com.elfiky.ibtikarandroidtask.popularlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.domain.repository.PersonOverviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class PopularListViewModel(
    repository: PersonOverviewRepository<PagingData<PersonOverview>>
) : ViewModel() {

    val popularList = repository.getPopularStream()
        .flowOn(Dispatchers.IO)
        .cachedIn(viewModelScope)

}