package com.elfiky.data.repository.person

import com.elfiky.data.network.entities.toPersonDetails
import com.elfiky.data.network.entities.toProfileImage
import com.elfiky.data.network.safeApiCall
import com.elfiky.data.repository.person.datasource.PersonRemoteDataSource
import com.elfiky.domain.entities.Failures
import com.elfiky.domain.entities.Person
import com.elfiky.domain.entities.Result
import com.elfiky.domain.entities.map
import com.elfiky.domain.entities.zip
import com.elfiky.domain.repository.PersonRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

internal class PersonRepositoryImpl(
    private val remoteDataSource: PersonRemoteDataSource
) : PersonRepository {

    override suspend fun getDetails(id: Int): Result<Person, Failures> {
        return coroutineScope {
            val detailsAsync = async { safeApiCall { remoteDataSource.fetchDetails(id) } }
            val profilesAsync = async { safeApiCall { remoteDataSource.fetchImages(id) } }

            val detailsResponse = detailsAsync.await().map { it.toPersonDetails() }
            val profilesResponse = profilesAsync.await().map { it.profiles.map { profile -> profile.toProfileImage() } }

            detailsResponse.zip(profilesResponse) { details, profiles ->
                Person(details = details, images = profiles)
            }
        }
    }

}