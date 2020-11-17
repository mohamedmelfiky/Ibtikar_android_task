package com.elfiky.data.repository.person.datasource

import com.elfiky.data.network.api.MoviesApi
import com.elfiky.data.network.entities.PersonApi
import com.elfiky.data.network.entities.PersonImagesApi

class PersonRemoteDataSourceImpl(
    private val api: MoviesApi
) : PersonRemoteDataSource {
    override suspend fun fetchDetails(id: Int): PersonApi {
        return api.fetchPersonDetails(id = id)
    }

    override suspend fun fetchImages(id: Int): PersonImagesApi {
        return api.fetchPersonImages(id = id)
    }
}