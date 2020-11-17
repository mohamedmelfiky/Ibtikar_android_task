package com.elfiky.data.repository.personoverview.datasource

import androidx.paging.PagingSource
import com.elfiky.data.network.api.MoviesApi
import com.elfiky.data.network.entities.PersonOverviewApi

class PersonOverviewPagingSource(
    private val api: MoviesApi
) : PagingSource<Int, PersonOverviewApi>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, PersonOverviewApi> {
        return try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = api.fetchPopularPersons(nextPageNumber)
            LoadResult.Page(
                data = response.results,
                prevKey = null, // Only paging forward.
                nextKey = if (response.total_pages > nextPageNumber) nextPageNumber + 1 else null
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}