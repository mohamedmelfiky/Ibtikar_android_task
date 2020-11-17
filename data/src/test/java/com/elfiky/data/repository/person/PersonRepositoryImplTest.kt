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
import com.elfiky.domain.entities.Person
import com.elfiky.domain.entities.PersonDetails
import com.elfiky.domain.entities.PersonId
import com.elfiky.domain.entities.PersonName
import com.elfiky.domain.entities.ProfileImage
import com.elfiky.domain.entities.Result
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class PersonRepositoryImplTest {

    @Test
    fun getDetails_validInput_ResultSuccessOfPerson() = runBlockingTest {
        val remoteDataSource = object : PersonRemoteDataSource {
            override suspend fun fetchDetails(id: Int): PersonApi {
                return PersonApi(
                    id,
                    "test name",
                    emptyList(),
                    0,
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
}