package com.elfiky.data.repository.person.datasource

import com.elfiky.data.network.entities.PersonApi
import com.elfiky.data.network.entities.PersonImagesApi

interface PersonRemoteDataSource {
    suspend fun fetchDetails(id: Int): PersonApi
    suspend fun fetchImages(id: Int): PersonImagesApi
}