package com.elfiky.data.repository.personoverview

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import com.elfiky.data.network.entities.PersonOverviewApi
import com.elfiky.data.network.entities.toPersonOverview
import com.elfiky.domain.entities.PersonOverview
import com.elfiky.domain.repository.PersonOverviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DEFAULT_PAGE_SIZE = 20

internal class PersonOverviewRepositoryImpl(
    private val pagingSource: PagingSource<Int, PersonOverviewApi>,
    private val pagingConfig: PagingConfig = PagingConfig(DEFAULT_PAGE_SIZE),
) : PersonOverviewRepository<PagingData<PersonOverview>> {

    override fun getPopularStream(): Flow<PagingData<PersonOverview>> {
        return Pager(config = pagingConfig) { pagingSource }.flow
            .map { pagingData ->
                pagingData.map { it.toPersonOverview() }
            }
    }

}