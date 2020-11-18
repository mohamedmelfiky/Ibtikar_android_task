package com.elfiky.data.repository.person

import com.elfiky.data.network.Network
import com.elfiky.data.network.entities.PersonApi
import com.elfiky.data.network.entities.PersonImagesApi
import com.elfiky.data.network.entities.ProfileApi
import com.elfiky.data.repository.person.datasource.PersonRemoteDataSource
import com.elfiky.domain.entities.Biography
import com.elfiky.domain.entities.Department
import com.elfiky.domain.entities.Gender
import com.elfiky.domain.entities.ImageOriginal
import com.elfiky.domain.entities.ImageThumbnail
import com.elfiky.domain.entities.InvalidApiKey
import com.elfiky.domain.entities.NetworkFailure
import com.elfiky.domain.entities.NotFound
import com.elfiky.domain.entities.Person
import com.elfiky.domain.entities.PersonDetails
import com.elfiky.domain.entities.PersonId
import com.elfiky.domain.entities.PersonName
import com.elfiky.domain.entities.ProfileImage
import com.elfiky.domain.entities.Result
import com.elfiky.domain.entities.UnknownFailure
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.internal.EMPTY_RESPONSE
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class PersonRepositoryImplTest {

    @Test
    fun getDetails_validInput_ResultSuccessOfPerson() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                return PersonApi(
                    id,
                    "test name",
                    emptyList(),
                    2,
                    "test department",
                    "test bio",
                    "/zLTq39cdRjS43dEzb78c1p1QcbT.jpg",
                    null,
                    null,
                    null
                )
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                return PersonImagesApi(id, listOf(ProfileApi("/zLTq39cdRjS43dEzb78c1p1QcbT.jpg")))
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Success(Person(
            details = PersonDetails(
                PersonId(1),
                PersonName("test name"),
                emptyList(),
                ImageThumbnail("${Network.imagesBaseUrl}w500/zLTq39cdRjS43dEzb78c1p1QcbT.jpg"),
                Gender.Male,
                Department("test department"),
                Biography("test bio"),
                null,
                null,
                null
            ),
            images = listOf(ProfileImage(
                thumbnail = ImageThumbnail("${Network.imagesBaseUrl}w500/zLTq39cdRjS43dEzb78c1p1QcbT.jpg"),
                original = ImageOriginal("${Network.imagesBaseUrl}original/zLTq39cdRjS43dEzb78c1p1QcbT.jpg"))
            )
        ))

        assert(result == expectedResult)
    }

    @Test
    fun getDetails_invalidApiKey_ResultFailureInvalidApiKey() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                throw HttpException(Response.error<PersonApi>(401, EMPTY_RESPONSE))
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                return PersonImagesApi(id, listOf(ProfileApi("/zLTq39cdRjS43dEzb78c1p1QcbT.jpg")))
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Failure(InvalidApiKey)

        assert(result == expectedResult)
    }

    @Test
    fun getDetails_invalidId_ResultFailureNotFound() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                throw HttpException(Response.error<PersonApi>(404, EMPTY_RESPONSE))
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                throw HttpException(Response.error<PersonApi>(404, EMPTY_RESPONSE))
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Failure(NotFound)

        assert(result == expectedResult)
    }

    @Test
    fun getDetails_unknownError_ResultFailureUnknownFailure() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                throw HttpException(Response.error<PersonApi>(405, EMPTY_RESPONSE))
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                throw HttpException(Response.error<PersonApi>(405, EMPTY_RESPONSE))
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Failure(UnknownFailure)

        assert(result == expectedResult)
    }

    @Test
    fun getDetails_IOException_ResultFailureNetworkFailure() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                throw IOException()
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                throw IOException()
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Failure(NetworkFailure)

        assert(result == expectedResult)
    }

    @Test
    fun getDetails_oneOfResponsesIsInvalidJson_ResultFailureUnknownFailure() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                throw JsonDataException("invalid json")
            }

            override suspend fun fetchImages(id: Int): PersonImagesApi {
                return PersonImagesApi(id, listOf(ProfileApi("/zLTq39cdRjS43dEzb78c1p1QcbT.jpg")))
            }
        }
        val personRepository = PersonRepositoryImpl(remoteDataSource)

        val result = personRepository.getDetails(1)

        val expectedResult = Result.Failure(UnknownFailure)

        assert(result == expectedResult)
    }
}